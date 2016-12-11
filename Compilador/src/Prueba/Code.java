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
public class Code {
    String code;
    String indexLabel;
    Cuadruple cuad;
    
    
    public Code(String code, String index){
        this.code= code;
        this.indexLabel = index;
    }
    /**
     * 
     * @param code
     * @param oper
     * @param val1
     * @param val2
     * @param dest 
     */
    public Code(String code, String oper, String val1, String val2, String dest){
        this.code= code;
        this.cuad = new Cuadruple(oper, val1, val2, dest);
    }
    
    
    public Code(String code){
        this.code = code ;
        this.indexLabel = "";
    }
    
    @Override
    public String toString() {
        return code;
    }
    
}
