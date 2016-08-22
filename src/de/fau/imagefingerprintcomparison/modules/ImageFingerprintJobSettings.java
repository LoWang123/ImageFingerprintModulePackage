/*
 * 
 */
package de.fau.imagefingerprintcomparison.modules;

import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettings;

/**
 * 
 */
public class ImageFingerprintJobSettings implements IngestModuleIngestJobSettings {

    private static final long serialVersionUID = 1L;
    
    private boolean compareCH = true;
    private boolean compareMHO = true;
    private boolean compareBMV = true;
    private boolean compareToDatabaseImages = true;
    private float threshold = 0.2f;
    private int numberOfSuccessRequired = 1;

    public ImageFingerprintJobSettings() {
        numberOfSuccessRequired = 1;
        compareCH = true;
        compareMHO = true;
        compareBMV = true;
        compareToDatabaseImages = true;
        threshold = 0.2f;
    }

    public ImageFingerprintJobSettings(
            boolean compareCH, boolean compareMHO, boolean compareBMV, 
            boolean compareToDatabaseImages, float threshold, SimilarityRequirement similarityRequirement) {
        
        this.compareCH = compareCH;
        this.compareMHO = compareMHO;
        this.compareBMV = compareBMV;
        this.numberOfSuccessRequired = 0;
        if(this.compareCH) {
            ++numberOfSuccessRequired;
        }
        if(this.compareMHO) {
            ++numberOfSuccessRequired;
        }
        if(this.compareBMV) {
            ++numberOfSuccessRequired;
        }
        
        switch(similarityRequirement) {
            case AtLeastOne:
                numberOfSuccessRequired = 1;
                break;
            case Majority:
                numberOfSuccessRequired = (int) Math.ceil(numberOfSuccessRequired / 2);
                break;
            case All:
                break;
            default:
                numberOfSuccessRequired = 1;
        }
        
        this.compareToDatabaseImages = compareToDatabaseImages;
        this.threshold = threshold;
        
    }

    @Override
    public long getVersionNumber() {
        return serialVersionUID;
    }

    public boolean compareCH() {
        return compareCH;
    }
    
    public boolean compareMHO() {
        return compareMHO;
    }
    
    public boolean compareBMV() {
        return compareBMV;
    }
    
    public boolean compareToDatabaseImages() {
        return compareToDatabaseImages;
    }
    
    public float getThreshold() {
        return threshold;
    }

    public int getNumberOfSuccessRequired() {
        return numberOfSuccessRequired;
    }
}
