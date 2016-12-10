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
        String tipo;
        int pos;
        int width;
        int padre;
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
