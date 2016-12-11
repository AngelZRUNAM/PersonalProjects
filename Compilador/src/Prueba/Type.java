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
public class Type {
    int type;
    int width;
    
    public Type(int tipo, int width){
        this.type = tipo;
        this.width = width;
    }
    
    public Type(int tipo){
        switch(tipo){
            case Parser.INT: 
                this.type = 2;
                this.width = 4;
                break;
            case Parser.FLOAT:
                this.type = 3;
                this.width = 8;
                break;
            case Parser.CHAR:
                this.type = 1;
                this.width = 2;
                break;
            case Parser.VOID:
                this.type = 0;
                this.width = 0;
                break;
            default:
                System.out.println("Prueba.Type.<init>() Error de tipo ");
        }
    }

    @Override
    public String toString() {
        return "(" + type + "," + width + ")"; 
    }
    
    
}
