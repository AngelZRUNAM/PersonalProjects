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
public class Sentencia {
    String next;
    ArrayList<Code> code;
    boolean ret = false;
    int type;
    
    public Sentencia(String next, ArrayList<Code> code){
        this.next = next;
        this.code = code;
    }
    
    public Sentencia(){
        code = new ArrayList<>();
    }

    void printCode() {
        System.out.println(" Codigo Intermedio ");
        for (Code code1 : code) {
            System.out.println(code1);
        }
        System.out.println(" Codigo Ensamblador ");
        AssemblyCode as = new AssemblyCode(code);
        for(String str : as.getCode()){
            System.out.println(str);
        }
    }
}
