/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prueba;

/**
 *
 * @author ulises
 */
public class Cuadruple {
    String op;
    String arg1;
    String arg2;
    String res;
    String label;
    
    public Cuadruple(String op, String arg1, String arg2, String res){
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.res = res;
    }

    @Override
    public String toString() {
        return op + "\t" + arg1 + "\t" + arg2 + "\t" + res;
    }
    
    
}
