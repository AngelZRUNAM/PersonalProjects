                                                                                                                                                                                        
package ValidationTools;

import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
*Class Name :   "_NAME_"
*Company    :   "ABACO-RT"
*Created on :   "_DATE_"
*
*Copyright(c) {YEAR!!!} ABACO-RT, Inc.  All Rights Reserved.
*This software is the proprietary information of ABACO-RT.
*/

/**
 * 
 * @author      "ING. MIGUEL ANGEL ZUÑIGA REYES"
 * @proyect     "_PROY_"    
 */
public class ValidaTextFields {
    public int NUMERIC  = 1;
    public int INTEGER  = 2;
    public int DECIMAL  = 3;
    public int ALFA     = 4;
    public int ALFANUMERIC = 5;
    public int ASCII    = 6;
    
    public boolean validaEmptyAndPlaceHolder(JTextField textField, String placeValue){
        if(textField.getText().equals(placeValue)) return false;
        if(textField.getText().trim().length()==0) return false;
        return !textField.getText().trim().equals("");
    }
    
    public boolean setKeyPressedValidate(int typeValidetion, JTextField textField){
        switch(typeValidetion){
            case 1:
                
                break;
            case 2:
                setIntegerValidateTool(textField);
                break;
            default:
                break;
        }
        return true;
    }

    private void setIntegerValidateTool(final JTextField textField) {
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println(".keyTyped() " + e.getKeyChar());
                JTextField textF    = (JTextField) e.getSource();
                if(!(e.getKeyChar() >='0' && e.getKeyChar() <= '9' ) &&  printableCharacter(e)){
                    try {
                        textF.setText(textF.getText().replaceAll(""+e.getKeyChar(), ""));                    
                    } catch (Exception ex) {
                        textF.setText("");
                    }
                    cleanNumeric(textF);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                JTextField textF    = (JTextField) e.getSource();
                if(!(e.getKeyChar() >='0' && e.getKeyChar() <= '9' ) &&  printableCharacter(e)){
                    try {
                        textF.setText(textF.getText().replaceAll(""+e.getKeyChar(), ""));                    
                    } catch (Exception ex) {
                        textF.setText("");
                    }
                    cleanNumeric(textF);
                }
            }

           

            
        });
    }
    public boolean printableCharacter(KeyEvent e) {
        char key  =  e.getKeyChar();
        return (key >= 32 && key < 127 ) ;
    }
    
    public void cleanNumeric(JTextField textF) {
        String aux = textF.getText();
        for (int i = 0; i < aux.length(); i++) {
            char a = aux.charAt(i);
            if(a < '0' || a > '9'){
                aux = aux.replaceAll(""+a, "");
            }
        }
        textF.setText(aux);
    }


}
