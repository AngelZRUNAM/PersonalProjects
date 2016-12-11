/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prueba;

import java.util.ArrayList;

/**
 *
 * @author ulises
 */
public class Expresion {
    String dir;
    int tipo;
    ArrayList<Code> code;
    Double value;
    int base;
    int width;
    
    
    public Expresion(String dir, int tipo, ArrayList code){
        this.base = -1;
        this.dir = dir;
        this.tipo = tipo;
        this.code = code;
    }
    
    public Expresion(String value) {
        this.base = -1;
        int t = SemanticAcc.getTypeFromID(value);
        if(t == -1) throw new Error("No existe el ID " + value);
        this.tipo   = t;
        this.dir    = value;
        code = new ArrayList<>();
    }
    public Expresion(String value,String tip) {
        if(tip.equals("char")){
            this.base = -1;
            this.tipo   = 1;
            this.dir    = value.replace("'", "");
            code = new ArrayList<>();
        }    
    }
    
    public Expresion(Number value) {
        this.base = -1;
        code = new ArrayList<>();
        if(value.tipo.equals("int")) {
            this.tipo   = 2;
            this.dir  = value.ival.doubleValue()+"";
        }
        else if(value.tipo.equals("float")) {
            this.tipo = 3;
            this.dir = value.dval+"";
        }
        else throw new Error("Expresion no valida, error semantico");
    }
    

    Expresion() {
        this.base = -1;
        code = new ArrayList<>();
    }

    void print() {
        System.out.println("Prueba.Expresion.print() " + code.toString());
    }


}
