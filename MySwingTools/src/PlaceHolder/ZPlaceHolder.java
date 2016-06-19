/**
 * Class Name : "ZPlaceHolder" School : "UNAM FACULTAD DE INGENIERIA" Created on
 * : "18-ene-2016"
 * 
* Copyright(c) 2016 ZPlaceHolder, Inc. All Rights Reserved. This software is
 * the proprietary information of ZPlaceHolder.
 */
package PlaceHolder;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/**
 *
 * @author "ING. MIGUEL ANGEL ZUÑIGA REYES"
 * @proyect "SwingTools"
 * @version "--version--"
 * @description "--description--"
 */
public class ZPlaceHolder {

    String TEXTCOLORplomo = "#818181";
    String TEXTCOLORnegro = "#000000";

    /**
     * Constructor of ZPlaceHolder
     */
    public ZPlaceHolder() {
    }

    public void setPlaceHolderToTextField(final JTextField textField, final String text) {

        textField.setText(text);
        textField.setForeground(Color.decode(TEXTCOLORplomo));
        textField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                textField.setForeground(Color.decode(TEXTCOLORnegro));
                if (textField.getText().equals(text)) {
                    textField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().length() == 0) {
                    textField.setText(text);
                    textField.setForeground(Color.decode(TEXTCOLORplomo));
                }
            }
        });
    }

}
