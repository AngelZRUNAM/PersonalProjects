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
public class Bool {
    String ltrue;
    String lfalse;
    ArrayList<Code> code;
    
    public Bool(String ltrue, String lfalse, ArrayList code){
        this.lfalse= lfalse;
        this.ltrue = ltrue;
        this.code = code;
    }

    void printCode() {
        for (Code code1 : code) {
            System.out.println(code1);
        }
    }
}
