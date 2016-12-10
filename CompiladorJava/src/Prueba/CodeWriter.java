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
