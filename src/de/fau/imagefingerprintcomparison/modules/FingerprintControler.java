/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules;

import de.fau.imagefingerprint.database.ImageWithFingerprint;
import de.fau.imagefingerprint.fingerprint.BlockMeanValue;
import de.fau.imagefingerprint.fingerprint.ColorHistogram;
import de.fau.imagefingerprint.fingerprint.FingerprintAlgorithms;
import de.fau.imagefingerprint.fingerprint.FingerprintComperator;
import de.fau.imagefingerprint.fingerprint.IFingerprintAlgorithm;
import de.fau.imagefingerprint.fingerprint.MarrHildrethOperator;
//import de.fau.imagefingerprintcomparison.fingerprint.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;



/**
 *
 * @author Tobias
 */
public class FingerprintControler {
    
    IFingerprintAlgorithm algorithm = null;
    private double similarityThreshold = 0.2;
    
    public double getSimilarityThreshold() {
        return similarityThreshold;
    }

    public void setSimilarityThreshold(double similarityThreshold) {
        this.similarityThreshold = similarityThreshold;
    }
    /**
     * TODO RETURN IMAGEWITHFINGERPRINT
     * 
     * 
     * @param image
     * @return
     */
    public ImageWithFingerprint calculateFingerprints(BufferedImage image) {
        
        setAlgorithm(FingerprintAlgorithms.BMV);
        ((BlockMeanValue)algorithm).calculateFingerprint(image);
        byte[] fingerprintBMV = ((BlockMeanValue)algorithm).getFingerprint();
        
        setAlgorithm(FingerprintAlgorithms.MHO);
        ((MarrHildrethOperator)algorithm).calculateFingerprint(image);
        byte[] fingerprintMHO = ((MarrHildrethOperator)algorithm).getFingerprint();
        
        setAlgorithm(FingerprintAlgorithms.CH);
        ((ColorHistogram)algorithm).calculateFingerprint(image);
        byte[] fingerprintCH = ((ColorHistogram)algorithm).getFingerprint();
        
        return new ImageWithFingerprint(image, fingerprintMHO, fingerprintBMV, fingerprintCH, null, null);
    }
    
    public double compare(FingerprintComperator fc) 
            throws IllegalArgumentException {
        return fc.compare();
    }
    
    /**
     * Compares
     * 
     * @param fcList
     * @return
     * @throws IllegalArgumentException 
     */
    public double compare(ArrayList<FingerprintComperator> fcList) 
            throws IllegalArgumentException {
        
        boolean lastAlgorithmPassedAsSimilar = true;
        double currentSimilarity = 1;
        
        for(FingerprintComperator fc : fcList)
            if(lastAlgorithmPassedAsSimilar)
                if(fc.compare() > similarityThreshold)
                    lastAlgorithmPassedAsSimilar = false;
 
        return currentSimilarity;
    }
    
    private void setAlgorithm (FingerprintAlgorithms alg) {
        switch(alg){
            case BMV:
                algorithm = new BlockMeanValue();
                break;
            case MHO:
                algorithm = new MarrHildrethOperator();
                break;
            case CH:
                algorithm = new ColorHistogram();
                break;
        }
    }
}
