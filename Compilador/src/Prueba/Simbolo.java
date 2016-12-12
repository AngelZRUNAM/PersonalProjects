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
class Simbolo {
    /**
     * Valor léxico del símbolo.
     */
    String lexema;
    /**
     * Dirección en la tabla de símbolos  
     */
    int dir;
    /**
     * Tipo de dato del símbolo
     */
    int tipo;
    /**
     * Tipo de símbolo:
     *  VARIABLE   -- 0 
     *  PARAMETRO  -- 1
     *  FUNCIÓN    -- 2
     */
    int VarParFun;
    /**
     * Lista de Tipo 's de los parámetros si el símbolo es función
     */
    ArrayList<Tipo> parameters = new ArrayList<>();
    
    int VARIABLE    = 0;
    int PARAMETER   = 1;
    int FUNC        = 2;

    public Simbolo(String lexema, int dir, int tipo, int VarParFun, ArrayList<Tipo> parameters) {
        this.lexema = lexema;
        this.dir = dir;
        this.tipo = tipo;
        this.VarParFun = VarParFun;
        this.parameters = parameters;
    }
    
    public Simbolo(String lexema, int dir, int tipo){
        this.lexema = lexema;
        this.dir = dir;
        this.tipo = tipo;
    }
    
    @Override
    public String toString(){
        if(parameters != null)
            return lexema + "\t" + dir + "\t" + tipo + "\t" + VarParFun + "\t" + parameters.toString() ;
        else 
            return lexema + "\t" + dir + "\t" + tipo + "\t" + VarParFun ;
    }
    /**
     * Verifica si un objeto es del tipo ArrayList y en caso positivo,
     * compara los valores de este array con los parámetros de la función en orden.
     * @param obj Verfica si es un ArrayList de enteros con los tipos de los parámetros 
     * de la función.
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if( obj instanceof ArrayList ){
            ArrayList arrayList = (ArrayList) obj;
            if(arrayList.size() != parameters.size()) return false;
            for (int i = 0; i < arrayList.size(); i++) {
                Integer get = (Integer) arrayList.get(i);
                if(get.intValue() != parameters.get(i).pos ) return false;
            }    
            return true;
        }
        return false;
    }
    
    
}
