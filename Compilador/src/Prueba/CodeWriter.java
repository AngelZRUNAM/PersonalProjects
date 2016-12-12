/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Prueba;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Miguel A. Zuñiga
 */
public class CodeWriter {
    
    
    public CodeWriter() {
    
    }
    
    /**
     * Escribe en el archivo filePath el codigo que contiene la lista de Codigo code.
     * @param filePath : nombre del archivo en el que se escribira el codigo intermedio.
     * @param code     : Lista de codigo intermedio.
     * @return         : true si el codigo se escribio correctamente.
     */
    public boolean writeCode(String filePath,ArrayList<Code> code){
        try{
            File file = new File(filePath);
            file.createNewFile();
            PrintWriter print = new PrintWriter(file);
            
            for (Code c : code) {
                print.println(c.code);
            }
            print.close();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * Escribe el codigo ensamblador que contiene la lista code en el archivo filePath
     * @param filePath  : Nombre del archivo a escribir
     * @param code      : Lista de cadenas en las cuales se va escribir el codigo ensamblador
     * @return          : true si el codigo se escribio correctamente.
     */
    public boolean writeAssembly(String filePath,ArrayList<String> code){
        try{
            File file = new File(filePath);
            file.createNewFile();
            PrintWriter print = new PrintWriter(file);
            for (String s : code) {
                print.println(s);
            }
            print.close();
            
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }    
        return true;
    } 
}
