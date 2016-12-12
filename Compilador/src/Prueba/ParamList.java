/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prueba;

import java.util.ArrayList;

/**
 *
 * @author Miguel A. Zuñiga
 */
public class ParamList {
    /**
     * Lista de código para la definición de los parámetros si estos son expresiones
     */
    ArrayList<Code> code;
    /**
     * Lista de código intermedio para colocar los parámetros, Ejemplo: param x 
     */
    ArrayList<Code> paramCode;
    /**
     * Lista de los tipos de parámetros que se estan utilizando
     */
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
