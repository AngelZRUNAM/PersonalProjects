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
    String lexema;
    int dir;
    int tipo;
    int VarParFun;
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
