                                                                                                                                                                                        
package ProgressBar;


import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

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
public class DefaultProgressBar extends JFrame
                                        implements PropertyChangeListener{

    private JProgressBar progressBar;
    private String progress = "progress";
    
    /**
     *
     * @param min
     * @param max
     */
    public void createProgressBar(int min, int max){
        progressBar = new JProgressBar(min, max);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
    }
    public void intiUI(){
        getContentPane().add(progressBar);
        this.pack();
        this.setVisible(true);
        
    }
    
    
    
    public void setProgress(int progress){
        progressBar.setValue(progress);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (progress.equals(evt.getPropertyName())) {
            int progressAux = (Integer) evt.getNewValue();
            progressBar.setValue(progressAux);
        }
    }
    
    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null); //turn off the wait cursor
        }
    }

}
