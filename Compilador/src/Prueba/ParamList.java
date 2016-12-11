/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prueba;

import java.util.ArrayList;

/**
 *
 * @author aluca
 */
public class ParamList {
    ArrayList<Code> code;
    ArrayList<Code> paramCode;
    ArrayList<Integer> lista;

    public ParamList(ArrayList<Code> code, ArrayList<Code> paramCode, ArrayList<Integer> lista) {
        this.code = code;
        this.paramCode = paramCode;
        this.lista = lista;
    }

    public ParamList() {
        code = new ArrayList<>();
        paramCode = new ArrayList<>();
        lista = new ArrayList<>();
    }
    
    
}
