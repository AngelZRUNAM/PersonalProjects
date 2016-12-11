/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prueba;

import java.util.ArrayList;
import java.util.Hashtable;


/**
 *
 * @author Miguel A. Zuñiga
 */
public class AssemblyCode {
    ArrayList<Code> code ;
    ArrayList<String> assembly;
    int ind = 0;
    String funcId = "";
    Hashtable<String, String> var;
    private ArrayList<ArrayList<Simbolo>> oldSim;
    private ArrayList<String> funcNames;
    private ArrayList<Simbolo> tablaSimbolosGlobal;
    
    
    public AssemblyCode() {
        assembly = new ArrayList<>();
    }

    public AssemblyCode(ArrayList<Code> code) {
        this();
        this.code = code;
    }
    
    ArrayList<String> getCode(){
        ArrayList<String> listCode = new ArrayList<>();
        Code c;
        
        while( (c = nextCode()) != null){
            if(c.cuad != null){
                String oper = c.cuad.op;
                switch(oper){
                    case "intToFloat":
                        listCode.addAll(intToFloat(c.cuad));
                        break;
                    case "=":
                        listCode.addAll(opAsig(c.cuad));
                        break;
                    case "+":
                        listCode.addAll(opEnt(c.cuad,"ADD"));
                        break;
                    case "-":
                        listCode.addAll(opEnt(c.cuad,"SUB"));
                        break;
                    case "*":
                        listCode.addAll(opEnt(c.cuad,"MUL"));
                        break;
                    case "/":
                        listCode.addAll(opEnt(c.cuad,"DIV"));
                        break;
                    case "%":
                        listCode.addAll(opEnt(c.cuad,"MOD"));
                        break;
                    case "%f":
                        listCode.addAll(opEnt(c.cuad,"MOD"));
                        break;
                    case "+f":
                        listCode.addAll(opEnt(c.cuad,"ADDF"));
                        break;
                    case "-f":
                        listCode.addAll(opEnt(c.cuad,"SUBF"));
                        break;
                    case "*f":
                        listCode.addAll(opEnt(c.cuad,"MULF"));
                        break;
                    case "/f":
                        listCode.addAll(opEnt(c.cuad,"DIVF"));
                        break;
                    case "param":
                        listCode.addAll(opParam(c.cuad));
                        break;
                    case "return":
                        listCode.addAll(ret(c.cuad));
                        break;
                    case "call":
                        listCode.addAll(opCall(c.cuad));
                        break;
                    case "goto":
                        listCode.addAll(opGoto(c.cuad));
                        break;
                    case "<":
                        listCode.addAll(opRel(c.cuad));
                        break;
                    case ">":
                        listCode.addAll(opRel(c.cuad));
                        break;
                    case "<=":
                        listCode.addAll(opRel(c.cuad));
                        break;
                    case ">=":
                        listCode.addAll(opRel(c.cuad));
                        break;
                    case "!=":
                        listCode.addAll(opRel(c.cuad));
                        break;
                    case "==":
                        listCode.addAll(opRel(c.cuad));
                        break;
                    default:
                        System.out.println(oper + " No P ");
                }
            }else{
                if( c.code.contains(":") ){
                   listCode.add(c.code);
                   //listCode.addAll(isFunc(c.code.substring(0, c.code.length()-1)));
                }else if(c.code.toLowerCase().trim().equals("halt")){
                    listCode.add("HALT");
                }
                
            }
        }
        assembly.addAll(listCode);
        return assembly;
    }

    private ArrayList<String> intToFloat(Cuadruple cuad) {
        ArrayList<String> listAux = new ArrayList<>();
        listAux.add("LD\tR0, " + isNumberGlobal(cuad.arg1) );
        listAux.add("LD\tR1, #0");
        listAux.add("ADDF\tR0, R0, R1" );
        listAux.add(new String("ST\t" + isNumberGlobal(cuad.res) + ", R0"));
        return listAux;
    }
    
    private String isNumberGlobal(String arg){
        try{
            Integer.parseInt(arg);
            Double.parseDouble(arg);
            return "#"+arg;
        }catch(Exception e){
            if(SemanticAcc.isGlobal(arg)){
                return arg;
            }
            else return arg+"(SP)";
        }
    }

    private Code nextCode() {
        if(ind<code.size())
         return code.get(ind++);
        else
         return null;   
    }

    private ArrayList<String> opEnt(Cuadruple cuad, String oper) {
        ArrayList<String> listAux = new ArrayList<>();
        listAux.add("LD\tR0, " + isNumberGlobal(cuad.arg1));
        listAux.add("LD\tR1, " + isNumberGlobal(cuad.arg2));
        listAux.add(oper+ "\tR0, R0, R1");
        listAux.add("ST\t"+isNumberGlobal(cuad.res)+",R0");
        return listAux;
    }

    private ArrayList<String> ret(Cuadruple cuad) {
        ArrayList<String> list = new ArrayList<>();
        list.add("LD\tR0,"+isNumberGlobal(cuad.arg1));
        list.add("ST\t1(SP),R0");
        list.add("BR\t*0(SP)");
        return list;
    }

    private ArrayList<String> opAsig(Cuadruple cuad) {
        ArrayList<String> list = new ArrayList<>();
        if(cuad.arg1.contains(funcId)){
          list.add("ST\t"+isNumberGlobal(cuad.res)+",R0");
          return list;
        } 
        list.add("LD\tR0,"+isNumberGlobal(cuad.arg1));
        list.add("ST\t"+isNumberGlobal(cuad.res)+",R0");
        return list;
    }

    private ArrayList<String> opParam(Cuadruple cuad) {
        ArrayList<String> list = new ArrayList<>();
        list.add("LD\tR0,"+isNumberGlobal(cuad.arg1));
        list.add("ST\t"+cuad.arg1+"(SP),R0" );
        return list;
    }

    private ArrayList<String> opCall(Cuadruple cuad) {
        ArrayList<String> list = new ArrayList<>();
        list.add("ADD\tSP,SP,#TAM");
        list.add("ST\t0(SP),20(PC)");
        list.add("BR\t"+cuad.arg1);
        list.add("LD\tR0,1(SP)");
        list.add("SUB\tSP,SP,#TAM");
        list.add("LD\tR4,R0");
        funcId = cuad.arg1;
        return list;
    }

    private ArrayList<String> opGoto(Cuadruple cuad) {
        ArrayList<String> list = new ArrayList<>();
        if(cuad.arg1 == null){
            list.add("BR\t"+cuad.res);
            return list;
        }else{
            
        }    
        return list;
    }

    private ArrayList<String> opRel(Cuadruple cuad) {
        ArrayList<String> list = new ArrayList<>();
        list.add("LD\tR0,"+isNumberGlobal(cuad.arg1));
        list.add("LD\tR1,"+isNumberGlobal(cuad.arg2));
        list.add("SUB\tR0,R0,R1 ; R0 = R0 - R1 ");
        Code c = nextCode();
        if(c.cuad.op.equals("goto")){
            switch(cuad.op){
                case "==":
                    list.add("BEZ\tR0, " + c.cuad.res );
                    break;
                case "!=":
                    list.add("BLTZ\tR0, " + c.cuad.res);
                    list.add("BGTZ\tR0, " + c.cuad.res);
                    break;
                case "<":
                    list.add("BLTZ\tR0, " + c.cuad.res);
                    break;
                case ">":
                    list.add("BGTZ\tR0, " + c.cuad.res);
                    break;
                case "<=":
                    list.add("BLTZ\tR0, " + c.cuad.res);
                    list.add("BEZ\tR0, " + c.cuad.res );
                    break;
                case ">=":
                    list.add("BGTZ\tR0, " + c.cuad.res);
                    list.add("BEZ\tR0, " + c.cuad.res );
                    break;
            }
        }else{
            System.err.println("Error en la estructura de condiciones");
        }    
        
        return list;
    }

    void setSimbolos(ArrayList<Simbolo> tablaSimbolosGlobal, ArrayList<ArrayList<Simbolo>> oldSim, ArrayList<String> funcNames) {
        for (Simbolo glob : tablaSimbolosGlobal) {
            if(glob.VarParFun == 0)
                assembly.add("EQU\t"+glob.lexema +", #" + glob.dir);
        }
        this.tablaSimbolosGlobal = tablaSimbolosGlobal;
        this.oldSim = oldSim; 
        this.funcNames = funcNames;
        
//        for (int i = 1; i < funcNames.size(); i++) {
//            String name = funcNames.get(i);
//            ArrayList<Simbolo> list = oldSim.get(i);
//            for (Simbolo simbolo : list) {
//                if(tablaSimbolosGlobal.contains(simbolo)) continue;
//
//            }
//        }

    }

    private ArrayList<String> isFunc(String name) {
        ArrayList<String> l = new ArrayList<>();
        int a = -1;
        for (int i = 0; i < funcNames.size(); i++) {
            if(funcNames.get(i).equals(name)) {
                a = i;
                break;
            }
        }
        if(a != -1 ){
            int c = 2;
            ArrayList<Simbolo> list = oldSim.get(a);
            for (Simbolo simbolo : list) {
                if(tablaSimbolosGlobal.contains(simbolo)) continue;
                l.add("EQU\t"+simbolo.lexema+", #"+c);
                c++;
            }    
        }
        return l;
    }
    
}
