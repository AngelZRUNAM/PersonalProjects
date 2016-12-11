/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prueba;

import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 *
 * @author ulises
 */
public class CodeGenerator {
    
    static PrintWriter file;
    static StringBuffer output;
    
    public static void init(){
        output = new StringBuffer();
    }
    
    public static void genetate(String cad){
        StringTokenizer strtoken = new StringTokenizer(cad, " ");
        String uno = strtoken.nextToken();
        
        switch(uno){
            case "if":
                String dos = strtoken.nextToken();
                output.append("LD RO, "+dos);
                output.append("\n");
                String tres = strtoken.nextToken();
                if(tres.equals(">")){
                    String cuatro = strtoken.nextToken();
                    output.append("LD R1, "+cuatro+"\n");
                    output.append("SUB RO, RO, R1\n");
                    strtoken.nextToken();
                    output.append("BGTZ R0, " +strtoken.nextToken()+"\n");                    
                }else if(tres.equals("<")){
                    String cuatro = strtoken.nextToken();
                    output.append("LD R1, "+cuatro+"\n");
                    output.append("SUB RO, RO, R1\n");
                    strtoken.nextToken();
                    output.append("BLTZ R0, " +strtoken.nextToken()+"\n");                    
                }else if(tres.equals("==")){
                    String cuatro = strtoken.nextToken();
                    output.append("LD R1, "+cuatro+"\n");
                    output.append("SUB RO, RO, R1\n");
                    output.append("BEZ R0, " +strtoken.nextToken()+"\n");                    
                }else{
                    strtoken.nextToken();
                    output.append("BNE R0, "+strtoken.nextToken()+"\n");
                }
        }
    }
    
    
}
