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
public class Number {
    Integer ival;
    Double dval;
    String tipo;
    
    public Number(Integer ival, String tipo){
        this.ival = ival;
        this.tipo = tipo;
    }
    
    public Number(Double dval, String tipo){
        this.dval = dval;
        this.tipo = tipo;
    }
}
