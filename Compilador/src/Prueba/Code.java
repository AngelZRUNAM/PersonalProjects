/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prueba;

/**
 *
 * @author ulises - Miguel A. Zuñiga
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
     * Crea un objeto Codigo con todos los valores necesarios para crear un cuadruple
     * @param code  : String con el codigo intermedio.
     * @param oper  : String con el operador
     * @param val1  : String con el argumento 1 
     * @param val2  : String con el argumento 2
     * @param dest  : String con el destino de la operación.
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
