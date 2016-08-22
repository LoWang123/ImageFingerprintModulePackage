/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.fingerprint;

import de.fau.imagefingerprint.database.ImageWithFingerprint;
import java.util.LinkedList;
import org.sleuthkit.autopsy.casemodule.services.FileManager;
import org.sleuthkit.autopsy.ingest.IngestJobContext;
import org.sleuthkit.autopsy.ingest.IngestServices;
import org.sleuthkit.autopsy.ingest.ModuleContentEvent;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.DerivedFile;
import org.sleuthkit.datamodel.TskCoreException;

/**
 *
 * @author Tobias
 */
public class ImageWithSimilaritiesHandler {
    private final String moduleName;
    private final IngestJobContext ingestJobContext;
    private final AbstractFile imageFile;   
    private final FileManager fileManager;
    private IngestServices services;
    private AbstractFile similarityFile;  
    
    public ImageWithSimilaritiesHandler(String moduleName, IngestJobContext ijc, 
            AbstractFile imageFile, FileManager fileManager) {
        this.moduleName = moduleName;
        this.ingestJobContext = ijc;
        this.imageFile = imageFile;
        this.fileManager = fileManager;       
    }
    
    public void commit() {
        synchronized(this) {
            //this.ingestJobContext.addFilesToJob(this._newFiles);
            this.services.fireModuleContentEvent(new ModuleContentEvent(this.imageFile));                      
           // this.newFiles = new LinkedList<AbstractFile>();
        }                
    }

    private void handleNewFileAdded() {
        this.commit();
    }
    
    public AbstractFile addTestImage(ImageWithFingerprint iwf, String fsPath, long size, long mtime, AbstractFile parent) throws TskCoreException{
        DerivedFile df;
        df = this.fileManager.addDerivedFile(
            iwf.getName(), 
            fsPath, 
            size,
            0, 0, 0, mtime,
            true, 
            parent, 
            "", this.moduleName, "", "");
        
        handleNewFileAdded();  
        return df;
    }
    
    
}
