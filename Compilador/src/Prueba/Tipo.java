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
public class Tipo {
        /**
         * Descripción del tipo de dato
         */
        String tipo;
        /**
         * Posición en la tabla de tipos
         */
        int pos;
        /**
         * Tamaño del tipo
         */
        int width;
        /**
         * Padre del tipo sí este es un arreglo, en caso de ser tipo basico el valor es -1
         */
        int padre;
        /**
         * Base del tipo de dato si es un arreglo, de lo contrario el valor es -1
         */
        int base;
        
        public Tipo(int pos, String tipo, int width, int padre) {
            base = -1;
            this.pos = pos;
            this.tipo = tipo;
            this.width =width;
            this.padre = padre;
        }

    @Override
    public String toString() {
        return tipo + "\t" + pos + "\t" + width + "\t" + padre + "\t" + base ;
    }
    }
