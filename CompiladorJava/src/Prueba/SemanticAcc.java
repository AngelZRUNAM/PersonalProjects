/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prueba;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author ulises
 */
public class SemanticAcc {
    // Instancia al parser
    static Parser yyparser;
    //Instancia al TextArea donde se escribe la tabla de símbolos ( Elemento visual)
    static JTextArea tablaSim;
    //Objeto para representar la tabla de símbolos.
    static ArrayList<Simbolo> tablaSimbolos;
    static ArrayList<Simbolo> paramTemp;
    static ArrayList<Simbolo> tablaSimbolosGlobal;
    
    static ArrayList<Integer> tablaOffSet;
    //Objeto para la tabla de tipos
    static ArrayList<Tipo> tablaTipos;
    static ArrayList<Tipo> tablaTiposGlobal;
    
    static ArrayList<ArrayList<Simbolo>> oldSim;
    static ArrayList<String> funcNames;
    static ArrayList<ArrayList<Tipo>> oldTip;
    
    // Variable para contar las direcciones
    static int offset;
    // Variables para llevar la cuenta de las etiquetas, temporales e indices
    static int numLabel=0;
    static int numTemp = 0;
    static int numIndex = 0;
    
    
    static int numTipos = 0;
    static int numSimbol = 0;
    
    static boolean globalLock = false;
    
    static {
        tablaSimbolos   = new ArrayList<>();
        tablaSimbolosGlobal   = new ArrayList<>();
        tablaTipos      = new ArrayList<>();
        tablaTiposGlobal      = new ArrayList<>();
        funcNames       = new ArrayList<>();
        funcNames.add("GLOBAL");
        paramTemp = new ArrayList<>();
        oldSim = new ArrayList<>();
        oldTip = new ArrayList<>();
        initTablaTipo(tablaTipos);
        offset = 0;
        
    }
    
    /**
     * Genera los nuevos índices que después se reemplazan por etiquetas
     * @return un índice nuevo
     */
    public static String newIndex(){
        numIndex++;
        String index = "#"+numIndex;
        return index;
    }
    
    /**
     * Método para generar direcciones temporales
     * @return una nueva temporal
     */
    public static String newTemp(){
        numTemp++;
        String temp = "t" + numTemp;
        Simbolo s = new Simbolo(temp, offset, 3);
        offset += 8 ;
        tablaSimbolos.add(s);
        return temp;
    }
    
    /**
     * Método para buscar en la tabla de símbolos un id
     * @param id el id a buscar
     * @return la posición en la tabla de símbolos
     */
    private static int search(String id){
        int i= -1;
        for(Simbolo sim : tablaSimbolos){
            if(sim.lexema.equals(id))
                return i= tablaSimbolos.indexOf(sim);
        }
        return i;
    }
    
    /**
     * Método para generar etiquetas.
     * @return  La etiqueta nueva generada
     */
    public static String newLabel(){
        numLabel++;
        String label = "L"+numLabel;
        return label;
    }
    
    
    /**
     * Método que imprime el código intermedio 
     * @param code código intermedio para imprimir
     */
    public static void print(ArrayList<Code> code){
        File yyoutput;
        BufferedWriter bw;
        PrintWriter pw = null;
        try {
            yyoutput = new File("Salida.ci");
            
            pw = new PrintWriter( new FileWriter(yyoutput));
            
            for(Code cod : code){
                switch(cod.code){
                    case "Label":
                        System.out.print(cod.indexLabel+":");
                        pw.print(cod.indexLabel+":");
                        break;
                    case "goto":
                        System.out.println("goto "+cod.indexLabel);
                        pw.println("goto "+cod.indexLabel);
                        break;
                    default:
                        if(cod.indexLabel.equals("0")){
                            System.out.println(cod.code);
                            pw.println(cod.code);
                        }else{
                            System.out.println(cod.code+" "+cod.indexLabel);
                            pw.println(cod.code+" "+cod.indexLabel);
                        }
                }
            }
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SemanticAcc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SemanticAcc.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            pw.close();
        }
    }
    
    /**
     * Mérodo para unificar etiquetas
     * @param code representa el código donde se van a combinar las etiquetas
     * @param first es la etiqueta que va a reemplazar a la segunda etiqueta
     * @param second  es la etiqueta que va ser reemplazada
     */
    static void combinar(ArrayList<Code> code, String first, String second){
        for(Code code0 : code){
//            if(code0.indexLabel.equals(second)){
//                code0.indexLabel = first;
//            }
            if(code0.code.contains(first))
                code0.code = code0.code.replace(first, second);
            if(code0.cuad != null)
                if(code0.cuad.res != null)
                    if(code0.cuad.res.contains(first))
                        code0.cuad.res = code0.cuad.res.replace(first, second);                
        }
    }
    
    /**
     * Método para asignar las etiquetas
     * @param code representa el código en donde se van a colocar las etiquetas
     * @param index es el índice que indica en donde se coloca la etiqueta
     * @param label0 es la etiqueta a colocar
     */
    static void asignar(ArrayList<Code> code, String index,  String label0){
        for(Code code0: code){
//            if(code0.indexLabel.equals(index))
//                code0.indexLabel = label0;
            if(code0.code.contains(index))
                code0.code = code0.code.replace(index, label0);
            if(code0.cuad != null)
                if(code0.cuad.res != null)
                    if(code0.cuad.res.contains(index))
                        code0.cuad.res = code0.cuad.res.replace(index, label0);
            
        }
    }
    
    /**
     * Método para asignar los valores iniciales 
     * para el analizador semántico y generador de código intermedio
     * @param simbolos JtextArea donde se imprime la tabla de símbolos.
     */
    public static void init(JTextArea simbolos){
        tablaSim = simbolos;
        
    }

    /**
     * Método para insertar un símbolo en la tabla de símbolos
     * @param sval es el identificador a insertar
     * @param tipo es el tipo del id
     */
    static void putId(String sval, int tipo) {
        if(tablaSimbolos.indexOf(sval)==-1){
            tablaSimbolos.add(new Simbolo(sval, offset, tipo));
            int width = (tablaTipos.get(tipo)).width;
            offset += width;
            tablaSim.setText(tablaSim.getText()+
                    tablaSimbolos.get(tablaSimbolos.size()-1).toString()+"\n");
        }else{
            yyparser.yyerror("Error el id se ha declarado mas de una vez");
        }
    }

    /**
     * Ss-> S Ss1 
     * Genera el código intermedio para una sentencia
     * @param sentencia representa a S
     * @param sentencia0 representa a Ss1
     * @return el valor que va tomar Ss
     */
    static Sentencia Sent(Sentencia sentencia, Sentencia sentencia0) {
        Sentencia sent = new Sentencia(sentencia0.next, new ArrayList());
        sent.code.addAll(sentencia.code);
        sent.code.add(new Code("Label", sentencia.next));
        sent.code.addAll(sentencia0.code);   
        asignar(sent.code,sentencia.next,newLabel());
        return sent;
    }

    /**
     * L-> S
     * @param sentencia representa a S
     * @return el valor para L
     */
    static Sentencia Sent(Sentencia sentencia) {
        Sentencia sent = new Sentencia(newLabel(), new ArrayList());
        sent.code.addAll(sentencia.code);
        return sent;
    }

    /** 
     * S -> if(B) S1
     * @param aBool ojeto que representa a B
     * @param sentencia objeto que reprsenta a S1
     * @return objeto que representa a S
     */
    static Sentencia If(Bool aBool, Sentencia sentencia) {
        Sentencia sent = new Sentencia(aBool.lfalse, new ArrayList());
        sent.code.addAll(aBool.code);
        sent.code.add(new Code("Label", aBool.ltrue));
        sent.code.addAll(sentencia.code);
        
        combinar(sent.code, aBool.lfalse, sentencia.next);
        asignar(sent.code, aBool.ltrue, newLabel());
        
//        System.out.println("Generando código para if");
//        for(Code cod: sent.code){
//            switch(cod.code){
//                case "Label":
//                    System.out.print(cod.indexLabel+":");
//                    break;
//                case "goto":
//                    System.out.println("goto "+cod.indexLabel);
//                    break;
//                default:
//                    if(cod.indexLabel.equals("0")){
//                        System.out.println(cod.code);
//                    }else
//                        System.out.println(cod.code+" "+cod.indexLabel);
//            }
//        }
//        System.out.println("Terminando código if");
        return sent;
    }

    /**
     * S-> if( B ) S1 else S2
     * @param aBool representa a B
     * @param sentencia representa a  S1
     * @param sentencia0 representa a S2
     * @return  rerpesenta a S
     */
    static Sentencia IfElse(Bool aBool, Sentencia sentencia, Sentencia sentencia0) {
        Sentencia sent = new Sentencia(sentencia.next, new ArrayList());
        sent.code.addAll(aBool.code);
        sent.code.add(new Code("Label", aBool.ltrue));
        sent.code.addAll(sentencia.code);
        sent.code.add(new Code("goto", sentencia.next));
        sent.code.add(new Code("Label", aBool.lfalse));
        sent.code.addAll(sentencia0.code);
        
        combinar(sent.code, sentencia.next, sentencia0.next);
        asignar(sent.code, aBool.ltrue, newLabel());
        asignar(sent.code, aBool.lfalse, newLabel());
        
        return sent;
    }

    /**
     * Lleva a cabo la comparación de los tipos y la generación
     * de código intermedio para una expresion de suma
     * E-> E1 + T
     * @param expresion representa a E1
     * @param expresion0 representa a T
     * @return valor que va tomar E
     */
    static Expresion Add(Expresion expresion, Expresion expresion0) {
        Expresion exp = new Expresion();        
        if(expresion.tipo == expresion0.tipo){
            exp.dir = newTemp();
            exp.tipo = expresion.tipo;
            exp.code=  new ArrayList<Code>();
            exp.code.addAll(expresion.code);
            exp.code.addAll(expresion0.code);
            exp.code.add(new Code(exp.dir+" = "+expresion.dir+" + "+expresion0.dir, "0"));            
        }else if(expresion.tipo == 0 && expresion0.tipo ==1){
            exp.tipo = 1;
            exp.dir = newTemp();
            exp.code=  new ArrayList<Code>();
            String alfa = newTemp();
            exp.code.addAll(expresion.code);
            exp.code.addAll(expresion0.code);
            exp.code.add(new Code(alfa+"= intToFloat("+expresion.dir,"0"));
            exp.code.add(new Code(exp.dir+" = "+alfa +"+"+expresion0.dir, "0"));
        }else if(expresion.tipo == 1 && expresion0.tipo ==0){
            exp.tipo = 1;
            exp.dir = newTemp();
            exp.code=  new ArrayList<Code>();
            String alfa = newTemp();
            exp.code.addAll(expresion.code);
            exp.code.addAll(expresion0.code);
            exp.code.add(new Code(alfa+"= intToFloat("+expresion0.dir,"0"));
            exp.code.add(new Code(exp.dir+" = "+expresion.dir +"+"+alfa, "0"));
        }else{
            yyparser.yyerror("Error los tipos no coinciden");
        }
        return exp;
    }
    
    /**
     * Lleva a cabo la comprobación de tipos y genera el código intermedio
     * para la multiplicación
     * T->T1 * F
     * @param expresion rerpesenta a T1
     * @param expresion0 rerpresenta a F
     * @return el valor para T
     */
    static Object Mul(Expresion expresion, Expresion expresion0) {
        Expresion exp = null;        
        if(expresion.tipo == expresion0.tipo){
            exp = new Expresion(newTemp(),expresion.tipo, new ArrayList());
            exp.dir = newTemp();
            exp.tipo = expresion.tipo;
            exp.code.addAll(expresion.code);
            exp.code.addAll(expresion0.code);
            exp.code.add(new Code(exp.dir+" = "+expresion.dir+" * "+expresion0.dir, "0"));            
        }else if(expresion.tipo == 0 && expresion0.tipo ==1){
            exp = new Expresion(newTemp(),1, new ArrayList());
            exp.tipo = 1;
            exp.dir = newTemp();
            String alfa = newTemp();
            exp.code.addAll(expresion.code);
            exp.code.addAll(expresion0.code);
            exp.code.add(new Code(alfa+"= intToFloat("+expresion.dir,"0"));
            exp.code.add(new Code(exp.dir+" = "+alfa +"*"+expresion0.dir, "0"));
        }else if(expresion.tipo == 1 && expresion0.tipo ==0){
            exp = new Expresion(newTemp(),1,new ArrayList());
            exp.tipo = 1;
            exp.dir = newTemp();
            String alfa = newTemp();
            exp.code.addAll(expresion.code);
            exp.code.addAll(expresion0.code);
            exp.code.add(new Code(alfa+"= intToFloat("+expresion0.dir,"0"));
            exp.code.add(new Code(exp.dir+" = "+expresion.dir +"*"+alfa, "0"));
        }else{
            yyparser.yyerror("Error los tipos no coinciden");
        }
        return exp;
    }

    
    /**
     * Método que genera código intermedio en un archivo de salida
     * P-> D L
     * @param sentencia representa a L la lista de sentencias
     * @return el valor para P
     */
    static Sentencia Program(Sentencia sentencia) {
        Sentencia sent = new Sentencia(sentencia.next, new ArrayList());
        sent.code.addAll(sentencia.code);
        sent.code.add(new Code("Label", sent.next));
        asignar(sent.code, sent.next, newLabel());
        
        print(sent.code);
        
        return sent;
        
    }

    /**
     * Método para generar código intermedio para la expresión booleana or
     * B -> B1 || B2
     * @param aBool  representa a B1
     * @param aBool0 representa a B2
     * @return el valor para B
     */
    static Object Or(Bool aBool, Bool aBool0) {
        Bool bool = new Bool(aBool.ltrue,aBool0.lfalse , new ArrayList());
        bool.code.addAll(aBool.code);
        bool.code.add(new Code("Label", aBool.lfalse));
        bool.code.addAll(aBool0.code);
        combinar(bool.code, aBool.ltrue, aBool0.ltrue);
        asignar(bool.code, aBool.lfalse, newLabel());
        return bool;
    }    

    /**
     * Método que busca un id en la tabla de símbolos
     * E -> id
     * @param id representa el id a buscar
     * @return el valor para E
     */
    static Object getId(String id) {      
        Expresion exp = null;
        int i = search(id);
        //System.out.println(id);
        //System.out.println(""+i);
        int tipo;
        if(i!=-1){
            Simbolo sim= tablaSimbolos.get(i);
            tipo = sim.tipo;
            exp = new Expresion(id, tipo, new ArrayList());
        }else{
            yyparser.yyerror("El identificador no ha sido declarado");
        }
  
        
        return exp;
    }

    
    /**
     * Método que retorna el número
     * F-> num 
     * @param number representa a num
     * @return el valor para F
     */
    static Object getNum(Number number) {        
        Expresion exp;
        if(number.tipo.equals("int"))
            exp = new Expresion( number.ival.toString(), 0, new ArrayList());
        else
            exp = new Expresion( number.dval.toString(), 1, new ArrayList());
        return exp;
    }
    
    
    /**
     * Método para generar código intermedio para
     * S-> id opasig E ;
     * @param id representa al id
     * @param opasig representa el operador de asignación (=, += ,*=)
     * @param expresion rerpesenta a E
     * @return el valor que va tomar S
     */
    static Object Asig(String id, String opasig, Expresion expresion) {
        Sentencia sent = new Sentencia(newIndex(), new ArrayList());
        int i = search(id);
        //System.out.println(id);
        //System.out.println(""+i);
        if(i!=-1){
            Simbolo sim = tablaSimbolos.get(i);
            String temp;
            if(sim.tipo == expresion.tipo){
                switch(opasig){
                    case "=":
                        sent.code.addAll(expresion.code);
                        sent.code.add(new Code(id+"="+expresion.dir,"0"));
                        break;
                    case "+=":
                        temp = newTemp();
                        sent.code.addAll(expresion.code);
                        sent.code.add(new Code(temp+"="+id+"+"+expresion.dir,"0"));
                        sent.code.add(new Code(id+"="+temp,"0"));
                        break;
                    case "*=":
                        temp = newTemp();
                        sent.code.addAll(expresion.code);
                        sent.code.add(new Code(temp+"="+id+"*"+expresion.dir,"0"));
                        sent.code.add(new Code(id+"="+temp,"0"));
                        break;
                }
            }else{
                yyparser.yyerror("Error los tipos no son compatibles");
            }
        }else{
            yyparser.yyerror("Error se esta utilizando un id que no fue declarado ");
        }
        return sent;
    }    

    /**
     * Método para generar código intermedio para 
     * B-> B1 && B2
     * @param bool representa a B1
     * @param bool0 rerpesenta a B2
     * @return valor para B
     */
    static Object And(Bool bool, Bool bool0) {
        Bool bool1 = new Bool(bool0.ltrue, bool.lfalse , new ArrayList());
        bool1.code.addAll(bool.code);
        bool1.code.add(new Code("Label", bool.ltrue));
        bool1.code.addAll(bool0.code);
        combinar(bool1.code, bool.lfalse, bool0.lfalse);
        asignar(bool1.code, bool.ltrue, newLabel());
        return bool;
    }

    /**
     * B-> E1 oprel E2
     * @param expresion representa a E1
     * @param oprel operado relacional ( >, <, ==)
     * @param expresion0 representa a E2
     * @return valor que va tomar B
     */
    static Object Exp(Expresion expresion, String oprel, Expresion expresion0) {
        Bool bool = new Bool(newIndex(), newIndex(), new ArrayList());
        String temp = newTemp();
        bool.code.addAll(expresion.code);
        bool.code.addAll(expresion0.code);
        bool.code.add(new Code(temp+" = "+expresion.dir+" "+oprel+" "+expresion0.dir,"0"));
        bool.code.add(new Code("if "+temp+" goto ",bool.ltrue));
        bool.code.add(new Code("goto", bool.lfalse));
        return bool;
    }

    static Object Program() {
        return new Object();
    }

    static Object declVar() {
        return new Object();
    }

    static void addID(String sval, Type tipo, int parFunc) {
        
        Simbolo sim = new Simbolo(sval, offset, tipo.type, parFunc, null);
        offset = offset + tipo.width;
        switch (parFunc) {
            case 0:
                if(globalLock) { tablaSimbolos.add(sim);
                }
                else    {
                    tablaSimbolosGlobal.add(sim);
                }
                break;
            case 1:
                tablaSimbolos.add(sim);
                break;
            default:
                tablaSimbolos.add(sim);
                tablaSimbolosGlobal.add(sim);
                funcNames.add(sval);
                break;
        }
        
    }
    
    static Type insertType(Object obj1, Object obj2, int base){
        //agregar a tabla de tipos 
        Type tipo = new Type(numTipos, ((Type)obj2).width * ((Number)obj1).ival);
        Tipo t = new Tipo(numTipos++, "Aux", tipo.width, ((Type)obj2).type);
        t.base = base;
        
        addTipo(t, tablaTipos);
        return tipo;
    }

    static Sentencia listControl(Sentencia obj, Sentencia obj0) {
        Sentencia s = new Sentencia(obj0.next, new ArrayList<Code>());
        s.code.addAll(obj.code);
        s.code.add(new Code(obj.next + ":", ""));
        s.code.addAll(obj0.code);
        asignar(s.code,obj.next,newLabel());
        s.ret = obj.ret || obj0.ret ;
        return s;
    }
    
    static Expresion sumaresta(Expresion oper1, String oper, Expresion oper2){
        Expresion exp = new Expresion(); 
        Code c ;
        if(oper1.base != -1) oper1.tipo = oper1.base;
        if(oper2.base != -1) oper2.tipo = oper2.base;
        if(oper1.tipo == oper2.tipo){
            exp.dir = newTemp();
            exp.tipo = oper1.tipo;
            exp.code=  new ArrayList<Code>();
            exp.code.addAll(oper1.code);
            exp.code.addAll(oper2.code);
            String aux = "f";
            if(oper1.tipo == 2 ) aux = " "; 
            c = new Code(exp.dir+" = "+ oper1.dir + " "+ oper+ " "+ oper2.dir, oper+aux, oper1.dir, oper2.dir,exp.dir);
            exp.code.add(c);            
         }else if(oper1.tipo == 2 && oper2.tipo == 3){
            exp.tipo = 3;
            exp.dir = newTemp();
            exp.code=  new ArrayList<Code>();
            String alfa = newTemp();
            exp.code.addAll(oper1.code);
            exp.code.addAll(oper2.code);
            exp.code.add(new Code(alfa+" = intToFloat("+oper1.dir+")","intToFloat",oper1.dir,null,alfa));
            exp.code.add(new Code(exp.dir+" = "+alfa + " "+ oper+ " "+oper2.dir,oper+"f",alfa,oper2.dir,exp.dir));
        }else if(oper1.tipo == 3 && oper2.tipo == 2){
            exp.tipo = 3;
            exp.dir = newTemp();
            exp.code=  new ArrayList<Code>();
            String alfa = newTemp();
            exp.code.addAll(oper1.code);
            exp.code.addAll(oper2.code);
            exp.code.add(new Code(alfa+ " = intToFloat("+oper2.dir+")",oper,oper2.dir,null,alfa));
            exp.code.add(new Code(exp.dir+" = "+oper1.dir + " "+ oper + " " + alfa,oper+"f",alfa,oper2.dir,exp.dir));
        }else{
            yyparser.yyerror("Error los tipos no coinciden");
        }
       return exp;
    }

    static Expresion muldivmod(Expresion oper1, String oper, Expresion oper2){
        Expresion exp = new Expresion(); 
        if(oper1.base != -1) oper1.tipo = oper1.base;
        if(oper2.base != -1) oper2.tipo = oper2.base;
        if(oper1.tipo == oper2.tipo){
            exp.dir = newTemp();
            exp.tipo = oper1.tipo;
            exp.code=  new ArrayList<Code>();
            exp.code.addAll(oper1.code);
            exp.code.addAll(oper2.code);
            String aux = "f";
            if(oper1.tipo == 2 ) aux = " ";
            exp.code.add(new Code(exp.dir+" = "+ oper1.dir + " "+ oper+ " "+ oper2.dir, oper+aux, oper1.dir,oper2.dir,exp.dir));
        }else if(oper1.tipo == 2 && oper2.tipo == 3){
            exp.tipo = 3;
            exp.dir = newTemp();
            exp.code=  new ArrayList<Code>();
            String alfa = newTemp();
            exp.code.addAll(oper1.code);
            exp.code.addAll(oper2.code);
            exp.code.add(new Code(alfa+" = intToFloat("+oper1.dir+")","intToFloat",oper1.dir,null,alfa));
            exp.code.add(new Code(exp.dir+" = "+alfa + " "+ oper+ " "+oper2.dir, oper+"f",alfa, oper2.dir,exp.dir));
        }else if(oper1.tipo == 3 && oper2.tipo == 2){
            exp.tipo = 3;
            exp.dir = newTemp();
            exp.code=  new ArrayList<Code>();
            String alfa = newTemp();
            exp.code.addAll(oper1.code);
            exp.code.addAll(oper2.code);
            exp.code.add(new Code(alfa+" = intToFloat("+oper2.dir+")","intToFloat",oper2.dir,null,alfa));
            exp.code.add(new Code(exp.dir+" = "+oper1.dir + " "+ oper+ " "+alfa, oper+"f", oper1.dir,alfa,exp.dir));
        }else{
            yyparser.yyerror("Error los tipos no coinciden");
        }
        //exp.print();
        return exp;
    }

    private static void initTablaTipo(ArrayList<Tipo> tablaTipos) {
        addTipo( new Tipo(numTipos++, "void",0, -1),tablaTipos);
        addTipo( new Tipo(numTipos++, "char",2, -1),tablaTipos);
        addTipo( new Tipo(numTipos++, "int",4, -1),tablaTipos);
        addTipo( new Tipo(numTipos++, "float",8, -1),tablaTipos);
    }

    private static void addTipo(Tipo tipo,ArrayList<Tipo> tablaTipos) {
        if(globalLock) tablaTipos.add(tipo);
        else
            tablaTiposGlobal.add(tipo);
    }

    static void print() {
        int i= 0;
        nuevoEntorno();
        System.out.println("Tabla de simbolos Global:");
        System.out.println("lex   |dir  |tipo");
        
        
        for (Simbolo s : tablaSimbolosGlobal) {
            System.out.println(s);
        }
        System.out.println(funcNames);
        
        for (ArrayList<Simbolo> tablaSimbolo : oldSim) {
            System.out.println("Tabla de simbolos local de Funcion  : " + funcNames.get(i++));
            System.out.println("lex   |dir  |tipo");
            for (Simbolo s : tablaSimbolo) {
                System.out.println(s);
            }
        }    
       
        
        
        
    }

    static int getTypeFromID(String value) {

        for (Simbolo simbolos : tablaSimbolos) {
            if(value.equals(simbolos.lexema))
                return simbolos.tipo;
        }
        
        for (Simbolo simbolos : tablaSimbolosGlobal) {
            if(value.equals(simbolos.lexema))
                return simbolos.tipo;
        }
        
        
        return -1;
    }
    
    

    static Expresion arrayValue(String string, Expresion expresion) {
        int t = getTypeFromID(string);
        if( t == -1 ) {
            yyparser.yyerror("Error: No existe el ID " + string);
        }
        Tipo p = getType(t);
        if( p.padre < 0 ) yyparser.yyerror("Error: ID no es un arreglo " + string);
        if(expresion.tipo != 2 ) yyparser.yyerror("Error: indice debe ser entero " );
        Expresion exp = new Expresion();
        exp.base = p.base;
        exp.tipo = p.padre;
        exp.width = getType(exp.tipo).width;
        exp.dir = newTemp();
        exp.code.add(new Code(exp.dir + " = " + expresion.dir + " * " + exp.width ,"*",expresion.dir,exp.width+"",exp.dir));
        return exp;
    }

    static Expresion arrayValue(Expresion arrayValue, Expresion expresion) {
        if(expresion.tipo != 2) yyparser.yyerror("Error: indice debe ser entero " );
        Expresion exp = new Expresion();
        exp.base = arrayValue.base;   
        Tipo t = getType(arrayValue.tipo);
        exp.tipo = t.padre;
        exp.width = getType(exp.tipo).width;
        String temp = newTemp();
        exp.dir = newTemp();
        exp.code.addAll(arrayValue.code);
        exp.code.add(new Code(temp + " = " + expresion.dir + " * " + exp.width,"*",expresion.dir,exp.width+"",temp));
        exp.code.add(new Code(exp.dir + " = " + arrayValue.dir + " + " + temp,"+",arrayValue.dir,temp,exp.dir));
        return exp;
    }

    private static Tipo getType(int t) {
        for (Tipo tipo : tablaTipos) {
            if(tipo.pos == t) return tipo;
        }
        for (Tipo tipo : tablaTiposGlobal) {
            if(tipo.pos == t) return tipo;
        }
        return null;
    }

    static Sentencia controlReturn(Expresion expresion, Type funcType) {
        Sentencia s = new Sentencia(newLabel(), new ArrayList<Code>());
        int base = funcType.type;
        if(expresion.base == -1) expresion.base = expresion.tipo;
        if(funcType.type > 3) 
            base = getTypeFromPos(funcType.type).base ;
        if(!compatible(expresion.tipo,base)) yyparser.yyerror("Error: return no compatible"); 
        s.ret = true;    
        s.code.addAll(expresion.code);
        s.code.add(new Code("return "+ expresion.dir ,"return",expresion.dir,null,null));
        s.type = expresion.tipo;
        return s;
    }

    static Sentencia controlAsig(String id, String oper, Expresion expresion) {
        Sentencia s = new Sentencia(newLabel(), new ArrayList<Code>());
        Simbolo l = getSimboloFromID(id);
        if(l == null) yyparser.yyerror("Error: No existe identificador");
        if(l.VarParFun != 0) yyparser.yyerror("Error: Problemas con el identificador, no es una variable " + id);
        s.code.addAll(expresion.code);
        if(oper.equals("=")){
            Code c = new Code(id + " = " + expresion.dir,""); 
            c.cuad = new Cuadruple("=", expresion.dir,"", id);
            s.code.add(c);
        }else{
            Code c = new Code(id + " = " + id + " " + oper + " " + expresion.dir , "");
            c.cuad = new Cuadruple(oper, id, expresion.dir, id);
            s.code.add(c);
        }
        return s;
    }

    public static Simbolo getSimboloFromID(String value) {
         for (Simbolo simbolos : tablaSimbolos) {
            if(value.equals(simbolos.lexema))
                return simbolos;
        }
         for (Simbolo simbolos : tablaSimbolosGlobal) {
            if(value.equals(simbolos.lexema))
                return simbolos;
        }
        return null;
    }
    
    public static Bool boolValue(Expresion exp1, String oper, Expresion exp2 ){ 
        Bool b = new Bool(newIndex(), newIndex(), new ArrayList());
        b.code.addAll(exp1.code);
        b.code.addAll(exp2.code);
        String t = newTemp();
        Code c = new Code(t + " = " + exp1.dir + " " + oper + " " + exp2.dir, "");
        c.cuad = new Cuadruple(oper, exp1.dir, exp2.dir, t);
        b.code.add(c);
        Code c2 = new Code("if " + t + " goto " + b.ltrue, "");
        c2.cuad = new Cuadruple("goto", "if", t, b.ltrue);
        b.code.add(c2);
        Code c3 = new Code("goto " + b.lfalse);
        c3.cuad = new Cuadruple("goto", null, null, b.lfalse);
        b.code.add(c3);
        return b;
    }  

    static Bool boolValueNot(Bool bool) {
        Bool b = new Bool(bool.lfalse, bool.ltrue, bool.code);
        return b;
    }

    static Bool boolValueOR(Bool b1, Bool b2) {
        Bool b = new Bool(b2.ltrue,b2.lfalse,new ArrayList());
        b.code.addAll(b1.code);
        Code c1 = new Code(b1.lfalse + ":");
        b.code.add(c1);
        b.code.addAll(b2.code);
        combinar(b.code, b1.ltrue, b2.ltrue);
        asignar(b.code, b1.lfalse, newLabel());
        return b;
    }


    static Bool boolValueAND(Bool b1, Bool b2) {
        Bool b = new Bool(b2.ltrue,b1.lfalse,new ArrayList());
        b.code.addAll(b1.code);
        Code c1 = new Code(b1.ltrue + ":");
        b.code.add(c1);
        b.code.addAll(b2.code);
        combinar(b.code, b2.lfalse, b1.lfalse);
        asignar(b.code, b1.ltrue, newLabel());
        return b;
    }

    static Sentencia controIF(Bool bool, Sentencia sentencia) {
        Sentencia s = new Sentencia();
        s.next = bool.lfalse;
        s.code = new ArrayList<>();
        s.code.addAll(bool.code);
        s.code.add(new Code(bool.ltrue + ":"));
        s.code.addAll(sentencia.code);
        combinar(s.code, sentencia.next,bool.lfalse);
        asignar(s.code,bool.ltrue,newLabel());
        return s;
    }

    static Sentencia controlIFEl(Bool bool, Sentencia s1, Sentencia s2) {
        Sentencia s = new Sentencia();
        s.next = s1.next;
        s.code = new ArrayList<>();
        s.code.addAll(bool.code);
        s.code.add(new Code(bool.ltrue + ":"));
        s.code.addAll(s1.code);
        s.code.add(new Code("goto " + s1.next,"goto",null,null,s1.next));
        s.code.add(new Code(bool.lfalse + ":"));
        s.code.addAll(s2.code);
        combinar(s.code, s1.next, s2.next);
        asignar(s.code, bool.ltrue, newLabel());
        asignar(s.code, bool.lfalse, newLabel());
        return s;
    }

    static Sentencia controlWhile(Bool bool, Sentencia sentencia) {
        Sentencia s = new Sentencia();
        s.next = bool.lfalse;
        s.code = new ArrayList<>();
        s.code.add(new Code(sentencia.next + ":"));
        s.code.addAll(bool.code);
        s.code.add(new Code(bool.ltrue + ":"));
        s.code.addAll(sentencia.code);
        s.code.add(new Code("goto " + sentencia.next,"goto",null,null,sentencia.next));
        asignar(s.code,sentencia.next, newLabel());
        asignar(s.code,bool.ltrue,newLabel());
        return s;
    }

    static Sentencia controlDO(Sentencia sent, Bool b) {
        Sentencia s = new Sentencia();
        s.code = new ArrayList<>();
        s.next = b.lfalse;
        s.code.add(new Code(b.ltrue + ":"));
        s.code.addAll(sent.code);
        s.code.add(new Code(sent.next + ":"));
        s.code.addAll(b.code);
        asignar(s.code, sent.next, newLabel());
        asignar(s.code, b.ltrue, newLabel());
        return s;
    }

    static Sentencia controlFor(Sentencia decl, Bool b, Code incr, Sentencia sent) {
        Sentencia s = new Sentencia();
        s.code = new ArrayList<>();
        s.next = b.lfalse;
        s.code.addAll(decl.code);
        s.code.add(new Code(sent.next +":"));
        s.code.addAll(b.code);
        s.code.add(new Code(b.ltrue + ":"));
        s.code.addAll(sent.code);
        s.code.add(incr);
        s.code.add(new Code("goto " + sent.next,"goto",null,null,sent.next));
        asignar(s.code,sent.next,newLabel());
        asignar(s.code,b.ltrue,newLabel());
        return s;
    }

    static Code increment(String id, String oper) {
        int type = getTypeFromID(id);
        if( type == -1 ) throw new Error("Error: identificador no existe");
        Tipo t = getType(type);
        if(t.pos != 3 && t.pos != 2 ) throw new Error("Error: identificador no numerico");
        Code c = new Code(id + " = " + id + " " + oper + " 1" );
        c.cuad = new Cuadruple(oper, id, "1", id);
        return c;
    }

    static Type setFuncType(Type tipo, String id, ArrayList<Tipo> params) {
        Simbolo func = new Simbolo(id, offset, tipo.type, 2, params);
        offset = offset + tipo.width;
        tablaSimbolosGlobal.add(func);
        tablaSimbolos.add(func);
        funcNames.add(id);
        globalLock = true;
        return tipo;
    }

    static ArrayList<Tipo> parameters(Type type, String id) {
        addID(id, type,1);
        ArrayList<Tipo> params = new ArrayList<>();
        params.add(getType(getTypeFromID(id)));
        return params;
    }

    static ArrayList<Tipo> parameters(Type type, String id, ArrayList<Tipo> params) {
        addID(id,type,1);
        params.add(getType(getTypeFromID(id)));
        return params;
    }

    private static boolean compatible(int t1, int t2) {
        switch(t1){
            case 2:
                return ( t2 == 3 || t2 == 2 );
                
            case 3:
                return ( t2 == 2 || t2 == 3 );
        }
        return false;
    }

    private static Tipo getTypeFromPos(int type) {
        for (Tipo tipo : tablaTipos) {
            if(tipo.pos == type) return tipo;
        }
        for (Tipo tipo : tablaTiposGlobal) {
            if(tipo.pos == type) return tipo;
        }
        return null;
    }

    static Sentencia chackFunc(Sentencia sents, Sentencia funcs, Type tipo,String id) {
        if( !sents.ret ) throw new Error("Error: falta valor de return");
        Sentencia s = new Sentencia();
        s.code = new ArrayList<>();
        s.code.add(new Code(id + ":"));
        s.code.addAll(sents.code);
        s.code.add(new Code(sents.next + ":"));
        s.code.add(new Code("halt"));
        if(funcs != null){
            s.code.addAll(funcs.code);
        }
        asignar(s.code, sents.next , newLabel());
        return s;
    }

    static ParamList parmList(ParamList paramList, Expresion expresion) {
        ParamList p = new ParamList();
        p.code.addAll(paramList.code);
        p.code.addAll(expresion.code);
        p.paramCode.addAll(paramList.paramCode);
        p.paramCode.add(new Code("param "+ expresion.dir,"param",expresion.dir,null,null));
        p.lista.addAll(paramList.lista);
        p.lista.add(expresion.tipo);
        return p;
    }
    static ParamList parmList(Expresion expresion) {
        ParamList p = new ParamList();
        p.code.addAll(expresion.code);
        p.paramCode.add(new Code("param " + expresion.dir,"param",expresion.dir,null,null));
        p.lista.add(expresion.tipo);
        return p;
    }

    static Expresion callFunc(String id, ParamList params) {
        Expresion e = new Expresion();
        Simbolo s = getSimboloFromID(id);
        if( s == null) throw new Error("Error: No existe la funcion " + id);
        if(s.VarParFun != s.FUNC ) throw new Error("Error: No existe la funcion "+ id);
        if(!s.equals(params.lista)) throw new Error("Error: Parametros no coinciden " );
        Tipo t = getType(s.tipo);
        e.dir = id;
        e.tipo = s.tipo;
        e.code = new ArrayList<>();
        e.code.addAll(params.code);
        e.code.addAll(params.paramCode);
        e.code.add(new Code("call " + id + "," + params.lista.size(),"call",id,params.lista.size()+"",null));
        
        return e;
    }

    public static void nuevoEntorno() {
//        System.out.println(tablaSimbolos);
//        System.out.println(tablaSimbolosGlobal);
        ArrayList<Simbolo> l = tablaSimbolos;
        oldSim.add(l);
        tablaSimbolos = new ArrayList<>();    
        ArrayList<Tipo> t = tablaTipos;
        oldTip.add(t);
        tablaTipos = new ArrayList<>();
        
        tablaSimbolos.addAll(tablaSimbolosGlobal);
        tablaTipos.addAll(tablaTiposGlobal);
        
        if(tablaSimbolosGlobal.isEmpty()) offset = 0;
        else 
            offset = tablaSimbolosGlobal.get(tablaSimbolosGlobal.size()-1).dir + getType(tablaSimbolosGlobal.get(tablaSimbolosGlobal.size()-1).tipo).width;
        
        
//        System.out.println(tablaSimbolos);
//        System.out.println(tablaSimbolosGlobal);        
    }

    static boolean isGlobal(String arg) {
        for (Simbolo sim : tablaSimbolosGlobal) {
            if(sim.lexema.equals(arg)) return true;
        }
        return false;
    }

    static void closeGlobal() {
        globalLock = true;
    }
    
    static boolean getProgram(Sentencia sent){
        CodeWriter codeWriter = new CodeWriter();
        if(codeWriter.writeCode(file.getPath() + file.getName()+ ".inter", sent.code)){
           AssemblyCode asm = new AssemblyCode(sent.code);
           asm.setSimbolos(tablaSimbolosGlobal,oldSim,funcNames);
           if(codeWriter.writeAssembly(file.getPath() + file.getName()+ ".asm", asm.getCode()))
               JOptionPane.showMessageDialog(null,"Traduccion exitosa !!!! ");
        }
        return true;
    }

    static void setFile(File file) {
        SemanticAcc.file = file;
    }
    public static File file;
    
    
}
