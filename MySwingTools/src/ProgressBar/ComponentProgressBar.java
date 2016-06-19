                                                                                                                                                                                        
package ProgressBar;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JProgressBar;

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
public class ComponentProgressBar extends JProgressBar
                                  implements PropertyChangeListener{
    private String progress = "progress";

    public ComponentProgressBar(int min, int max, String stringToShow, boolean stringPainted) {
        super(min,max);
        this.setValue(min);
        this.setStringPainted(stringPainted);
        this.setString(stringToShow);
    }

    public ComponentProgressBar() {
        super(0,100);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (progress.equals(evt.getPropertyName())) {
            int progressAux = (Integer) evt.getNewValue();
            this.setValue(progressAux);
        }
    }

}
