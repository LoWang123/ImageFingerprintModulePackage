/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules;

import de.fau.imagefingerprint.database.ImageWithFingerprint;
import de.fau.imagefingerprint.database.NameWithFingerprint;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;
import org.sleuthkit.autopsy.coreutils.Logger;

/**
 *
 * @author Tobi
 */
public class TaskCreatingFingerprints extends SwingWorker<Void, Void> {
    
    private Collection<FileWithFingerprints> listOfFilesWithFingerprints = new ArrayList<>();
    private File[] filesToWorkOn;
    private Logger logger;
    private FingerprintControler ctrl;
    
    private Collection<Component> disabledComponents = new ArrayList<>(); 
    
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    
    public void setFingerprintControler(FingerprintControler ctrl){
        this.ctrl = ctrl;
    }
    
    public Collection<FileWithFingerprints> getListOfPathsWithFingerprints() {
        return listOfFilesWithFingerprints;
    }

    public void setFilesToWorkOn(File[] filesToWorkOn) {
        this.filesToWorkOn = filesToWorkOn;
    }

    public void setDisabledComponents(Collection<Component> disabledComponents) {
        this.disabledComponents = disabledComponents;
    }
    
    
    
    @Override
        public Void doInBackground() {
            
            //Initialize progress property.
            setProgress(0);
            for(File fileToWorkOn: filesToWorkOn) {
                try {
                    BufferedImage image = ImageIO.read(fileToWorkOn);
                    ImageWithFingerprint iwf = ctrl.calculateFingerprints(image);
                    
                    FileWithFingerprints fwf = new FileWithFingerprints(fileToWorkOn, 
                            iwf.getFingerprint(), iwf.getFingerprintBlockMean(), iwf.getFingerprintColorHistogram());
                    
                    listOfFilesWithFingerprints.add(fwf);
                }
                catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error when trying to read Image " + 
                            fileToWorkOn.getAbsolutePath() + " please make sure to only add images of type png, jpg and bmp." , ex);
                }
                
                setProgress(Math.min(getProgress() + 1, 100));
            }
            
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            setProgress(0);
        }
}
