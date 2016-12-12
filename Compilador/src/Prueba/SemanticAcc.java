/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prueba;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author ulises - Miguel A. Zuñiga
 */
public class SemanticAcc {
    // Instancia al parser
    static Parser yyparser;
    //Instancia al TextArea donde se escribe la tabla de símbolos ( Elemento visual)
    static JTextArea tablaSim;
    //Objeto para representar la tabla de símbolos.
    /**
     * Tabla de símbolos local
     */
    static ArrayList<Simbolo> tablaSimbolos;
    /**
     * Tabla de símbolo globlal
     */
    static ArrayList<Simbolo> tablaSimbolosGlobal;
    /**
     * Tabla de tipos local
     */
    static ArrayList<Tipo> tablaTipos;
    /**
     * Tabla de tipos global
     */
    static ArrayList<Tipo> tablaTiposGlobal;
    /**
     *Lista de tablas de símbolos locales 
     */
    static ArrayList<ArrayList<Simbolo>> oldSim;
    /**
     * Lista de Nombres de funciones declaradas, sirve para hacer match con la lista de tabla de símbolos
     */
    static ArrayList<String> funcNames;
    /**
     * Lista de tablas de tipos locales
     */
    static ArrayList<ArrayList<Tipo>> oldTip;
    
    /** 
     * Variable para contar las direcciones
     */
    static int offset;
    /** 
     * Variables para llevar la cuenta de las etiquetas, temporales e indices
     */
    static int numLabel=0;
    static int numTemp = 0;
    static int numIndex = 0;
    
    /**
     * Variable para llevar la cuenta de tipos creados. 
     */
    static int numTipos = 0;
    /**
     * Variable para llevar la cuenta de símbolos creados.
     */
    static int numSimbol = 0;
    
    /**
     * Variable para saber si el programa esta en fase de declaraciones globales o locales.
     */
    static boolean globalLock = false;
    
    public static File file;
    
    /**
     * Inicializador statico, crea una instancia para todas las listas.
     */
    static {
        tablaSimbolos   = new ArrayList<>();
        tablaSimbolosGlobal   = new ArrayList<>();
        tablaTipos      = new ArrayList<>();
        tablaTiposGlobal      = new ArrayList<>();
        funcNames       = new ArrayList<>();
        funcNames.add("GLOBAL");
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
     * Método para generar etiquetas.
     * @return  La etiqueta nueva generada
     */
    public static String newLabel(){
        numLabel++;
        String label = "L"+numLabel;
        return label;
    }
    
    /**
     * Mérodo para unificar etiquetas
     * @param code representa el código donde se van a combinar las etiquetas
     * @param first es la etiqueta que va a reemplazar a la segunda etiqueta
     * @param second  es la etiqueta que va ser reemplazada
     */
    static void combinar(ArrayList<Code> code, String first, String second){
        for(Code code0 : code){
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
     * Agrega un nuevo símbolo a la tabla de símbolo global si es una variable global o una función 
     * en caso contrario lo agrega a la tabla de símbolo local en turno.
     * 
     * @param sval String con el lexema del símbolo
     * @param tipo Type con el tipo de símbolo
     * @param parFunc int que indica si el símbolo es variable (0), parámetro (1) o función (2)
     */
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
    
    /**
     * Genera un nuevo tipo para un arreglo y lo inserta en la tabla de tipos.
     * @param obj1 Object que se castea a Type con el tipo padre del arreglo  
     * @param obj2 Object que se castea a Number con el valor entero del índice del arreglo
     * @param base int con la base del nuevo tipo.
     * @return Type para el arreglo.
     */
    static Type insertType(Object obj1, Object obj2, int base){
        Type tipo = new Type(numTipos, ((Type)obj2).width * ((Number)obj1).ival);
        Tipo t = new Tipo(numTipos++, "Aux", tipo.width, ((Type)obj2).type);
        t.base = base;
        
        addTipo(t, tablaTipos);
        return tipo;
    }

    /**
     * Arreglo para manejar la lista de diferentes sentencias de control.
     * L = L S
     * @param obj Sentencia que representa a la lista de control 
     * @param obj0 Sentencia que representa a una sentencia de control.
     * @return Sentencia representando la unión de la lista con la sentencia de control.
     */
    static Sentencia listControl(Sentencia obj, Sentencia obj0) {
        Sentencia s = new Sentencia(obj0.next, new ArrayList<Code>());
        s.code.addAll(obj.code);
        s.code.add(new Code(obj.next + ":", ""));
        s.code.addAll(obj0.code);
        asignar(s.code,obj.next,newLabel());
        s.ret = obj.ret || obj0.ret ;
        return s;
    }
    
    /**
     * Evalua una operacion de suma o resta con dos expresiones generando una expresión como resultado
     * @param oper1 Expresion del operador 1
     * @param oper String tipo de operación 
     * @param oper2 Expresion del operador 2
     * @return Expresion de la operacion entre los dos operandos.
     */
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
    
    /**
     * Evalua una operacion de multiplicación, divición o modulo con dos expresiones generando una expresión como resultado
     * @param oper1 Expresion del operador 1
     * @param oper String tipo de operación 
     * @param oper2 Expresion del operador 2
     * @return Expresion de la operacion entre los dos operandos.
     */    
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
    /**
     * Inicializador de la tabla de tipos.
     * @param tablaTipos 
     */
    private static void initTablaTipo(ArrayList<Tipo> tablaTipos) {
        addTipo( new Tipo(numTipos++, "void",0, -1),tablaTipos);
        addTipo( new Tipo(numTipos++, "char",2, -1),tablaTipos);
        addTipo( new Tipo(numTipos++, "int",4, -1),tablaTipos);
        addTipo( new Tipo(numTipos++, "float",8, -1),tablaTipos);
    }

    /**
     * Agrega un tipo a la tabla de tipos dependiendo si es global o local.
     * @param tipo Tipo a agregar en la tabla
     * @param tablaTipos  ArrayList de Tipo representando la tabla de tabla de tipos local
     */
    private static void addTipo(Tipo tipo,ArrayList<Tipo> tablaTipos) {
        if(globalLock) tablaTipos.add(tipo);
        else
            tablaTiposGlobal.add(tipo);
    }

    /**
     * Imprime las tablas de símbolos en la salida estandar
     */
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

    /**
     * Obtiene la posición en la tabla de tipos (global o local) de un tipo según su identificador
     * @param value String con el lexema del identificador a buscar
     * @return int con la posición en la tabla de tipos, -1 si no existe el símbolo.
     */    
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
    
    
    /**
     * Ar -> id[num]
     * Obtiene el valor de un arreglo como un Expresion
     * @param string String identificador del arreglo
     * @param expresion Numero con el valor del arreglo.
     * @return Expresion con el valor del arreglo 
     */
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
    /**
     * Ar -> Ar [num]
     * Encadena el valor de un arreglo de multiples dimensiones.
     * @param arrayValue Expresion de la cadena para las dimensiones del arreglo.
     * @param expresion Expresion con el numero del indice para el arreglo.
     * @return Expresion con la cadena + la dimension del arreglo en turno
     */
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
    /**
     * Obtiene el Tipo de la tabla de tipos local o global 
     * @param t int con la posición en la tabla de tipos del tipo que se requiere
     * @return Tipo requerido de la tabla local o global de tipos, null si no se encuentra un tipo adecuado.
     */
    private static Tipo getType(int t) {
        for (Tipo tipo : tablaTipos) {
            if(tipo.pos == t) return tipo;
        }
        for (Tipo tipo : tablaTiposGlobal) {
            if(tipo.pos == t) return tipo;
        }
        return null;
    }

    /**
     * S -> return E
     * Realiza el codigo intermedio para la sentencia de control de return E;
     * @param expresion Expresion que representa el valor de retorno
     * @param funcType Type representa el tipo de la función
     * @return Sentencia con el codigo y la bandera de retorno encendida
     */
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

    /**
     * S -> id Oasig E 
     * Agrega el codigo intermedio para la sentencia de asignacion dependiendo del tipo de asignador.
     * @param id String con el identificador de la variable en donde se va a realizar la asignación
     * @param oper String con el valor del operador de asignación
     * @param expresion Expresion con el valor a asignar
     * @return Sentencia con el codigo intermedio de la asignación.
     */    
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

    /**
     * Obtiene un símbolo de la tabla de simbolos segun su lexema
     * @param value String con el lexema del símbolo a obtener.
     * @return Simbolo requerido o null en caso de que no exista. 
     */
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
    
    /**
     * Procesa la evaluacion de operadores relacionelas entre expresiones para generar un valor booleano
     * B -> E Or E 
     * @param exp1 Expresion operando 1
     * @param oper String con el operador relacional 
     * @param exp2 Expresion operando 2
     * @return Bool valor de la operacion entre operandos.
     */
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
    
    /**
     * Procesa la operacion de negación a un valor booleano
     * B -> !B
     * @param bool Bool operando
     * @return Bool resultado de la negación del operando.
     */
    static Bool boolValueNot(Bool bool) {
        Bool b = new Bool(bool.lfalse, bool.ltrue, bool.code);
        return b;
    }
    
    /**
     * Procesa la operación OR de dos operandos Booleanos
     * B -> B || B
     * @param b1 Bool operando 1
     * @param b2 Bool operando 2
     * @return Bool resultado de la opración OR 
     */
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

    /**
     * Procesa la operación AND de dos operandos booleanos 
     * B -> B && B
     * @param b1 Bool operando 1
     * @param b2 Bool operando 2
     * @return Bool resultado de la operación 
     */
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
    
    /**
     * Procesa la sentencia de control IF 
     * S -> if ( B ) S
     * @param bool Bool para la condicional de la estructura IF
     * @param sentencia Sentencia en caso de la condicional sea afirmativa
     * @return Sentencia con el codigo intermedio de la estructura IF
     */
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
    
    /**
     * Procesa la sentencia de control IF ELSE
     * S -> if ( B ) S else S
     * @param bool Bool condición a evaluar
     * @param s1 Sentencia codigo a ejecutar en caso positivo 
     * @param s2 Sentencia codigo a ejecutar en caso negativo
     * @return Sentencia codigo de la estructura IF ELSE
     */
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
    
    /**
     * Procesa la sentencia de control WHILE
     * S -> while ( B ) S
     * @param bool Bool condición a evaluar 
     * @param sentencia Sentencia codigo a ejecutar en el loop
     * @return Sentencia codigo intermedio de la sentencia WHILE
     */
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

    /**
     * Procesa la sentencia de control DO WHILE
     * S -> do S while( B ) ; 
     * @param sent Sentencia codigo a ejecutar en loop 
     * @param b Bool condición de repetición del loop
     * @return Sentencia con el código de DO WHILE
     */
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

    /**
     * Procesa la sentencia de control FOR 
     * S -> for ( As ; B ; I ) S
     * @param decl Sentencia código de la asignación 
     * @param b Bool condición a evaluar 
     * @param incr Code código del incremento
     * @param sent Sentencia código a ejecutar en el loop
     * @return Sentencia código de la sentencia de control FOR
     */
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
    
    /**
     * Realiza el código de incremento de una variable.
     * I -> id ( ++ | -- )
     * @param id String lexema de la variable a incrementar.
     * @param oper String con el tipo de operador, + para incrementar, - para decrementar.
     * @return Code código intermedio del incremento.
     */
    static Code increment(String id, String oper) {
        int type = getTypeFromID(id);
        if( type == -1 ) throw new Error("Error: identificador no existe");
        Tipo t = getType(type);
        if(t.pos != 3 && t.pos != 2 ) throw new Error("Error: identificador no numerico");
        Code c = new Code(id + " = " + id + " " + oper + " 1" );
        c.cuad = new Cuadruple(oper, id, "1", id);
        return c;
    }
    
    /**
     * Realiza la inserción de una función en la tabla de símbolos local y global
     * 
     * @param tipo Type representando el tipo de retorno de la función
     * @param id String con el lexema de la función
     * @param params Lista de Tipo's de los parámetros de la función
     * @return Type con el tipo de la función
     */
    static Type setFuncType(Type tipo, String id, ArrayList<Tipo> params) {
        Simbolo func = new Simbolo(id, offset, tipo.type, 2, params);
        offset = offset + tipo.width;
        tablaSimbolosGlobal.add(func);
        tablaSimbolos.add(func);
        funcNames.add(id);
        globalLock = true;
        return tipo;
    }
    
    /**
     * Realiza la inserción en la tabla de símbolos de los parámetros de una función
     * Lp -> T id
     * @param type Type tipo del parámetro
     * @param id String lexema del parámetro
     * @return Lista de Tipo's con el parametro que se agregó. 
     */
    static ArrayList<Tipo> parameters(Type type, String id) {
        addID(id, type,1);
        ArrayList<Tipo> params = new ArrayList<>();
        params.add(getType(getTypeFromID(id)));
        return params;
    }
    
    /**
     * Realiza la inserción y el encadenamiento de los parámetros de una función.
     * Lp -> Lp, T id
     * @param type Type tipo del parámetro a insertar
     * @param id String lexema del parámetro
     * @param params Lista de Tipo's con los tipos de los parámetros previos
     * @return Lista de Tipo's previos mas el el Tipo del parámetro recien insertado.
     */
    static ArrayList<Tipo> parameters(Type type, String id, ArrayList<Tipo> params) {
        addID(id,type,1);
        params.add(getType(getTypeFromID(id)));
        return params;
    }
    
    /**
     * Realiza una comparación entre dos tipos decidiendo si son compatibles o no.
     * @param t1 int tipo 1
     * @param t2 int tipo 2
     * @return true si los tipos son compatibles, false en caso contrario. 
     */
    private static boolean compatible(int t1, int t2) {
        switch(t1){
            case 2:
                return ( t2 == 3 || t2 == 2 );
                
            case 3:
                return ( t2 == 2 || t2 == 3 );
        }
        return false;
    }

    /**
     * Obtiene el Tipo de la tabla de Tipos dependiendo de la posición.
     * @param type int con la posición de tipo 
     * @return Tipo requerido
     */
    private static Tipo getTypeFromPos(int type) {
        for (Tipo tipo : tablaTipos) {
            if(tipo.pos == type) return tipo;
        }
        for (Tipo tipo : tablaTiposGlobal) {
            if(tipo.pos == type) return tipo;
        }
        return null;
    }
    
    /**
     * Procesa la declaración de una función y las funciones previamente definidas
     * S -> define T ID ( Lp ) { D S }
     * @param sents Sentencia código de las sentencias de control 
     * @param funcs Sentencia código de las funciones definidas.
     * @param tipo Type de la función a procesar
     * @param id String Lexema de la función a procesar
     * @return Sentencia código de la función a procesar y las previamente procesadas.
     */
    static Sentencia checkFunc(Sentencia sents, Sentencia funcs, Type tipo,String id) {
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

    /**
     * Lista de parametros para la llamade de una función
     * Al -> Al, E
     * @param paramList
     * @param expresion
     * @return 
     */
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
    
    /**
     * Parámetro de la llamada de una función.
     * Al - > E 
     * @param expresion
     * @return 
     */
    static ParamList parmList(Expresion expresion) {
        ParamList p = new ParamList();
        p.code.addAll(expresion.code);
        p.paramCode.add(new Code("param " + expresion.dir,"param",expresion.dir,null,null));
        p.lista.add(expresion.tipo);
        return p;
    }

    /**
     * Procesa la llamada de una función
     * E -> id( Al )
     * @param id String lexema de la función.
     * @param params ParamList lista de parametros en la llamada
     * @return Expresión valor de regreso de la función 
     */
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
    
    /**
     * Creación de un nuevo Entorno:
     * Guarda la tabla de símbolos y tipos locales en sus respectivas listas de historial 
     * Vacia las tablas locales.
     * Agrega el contenido de las tablas globales en las nuevas tablas locales.
     * 
     */
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
    
    /**
     * Busca si un lexema es global
     * @param arg
     * @return 
     */
    static boolean isGlobal(String arg) {
        for (Simbolo sim : tablaSimbolosGlobal) {
            if(sim.lexema.equals(arg)) return true;
        }
        return false;
    }

    /**
     * 
     */
    static void closeGlobal() {
        globalLock = true;
    }
    
    /**
     * Escribe los programas en codigo intermedio y en codigo Ensamblador
     * @param sent
     * @return 
     */
    static boolean getProgram(Sentencia sent){
        CodeWriter codeWriter = new CodeWriter();
        if(codeWriter.writeCode(file.getPath() + ".inter", sent.code)){
           AssemblyCode asm = new AssemblyCode(sent.code);
           asm.setSimbolos(tablaSimbolosGlobal,oldSim,funcNames);
           if(codeWriter.writeAssembly(file.getPath() + ".asm", asm.getCode()))
               JOptionPane.showMessageDialog(null,"Traduccion exitosa !!!! ");
        }
        return true;
    }
    
    /**
     * 
     * @param file 
     */
    static void setFile(File file) {
        SemanticAcc.file = file;
    }
    
    
    
}
