/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules;

import de.fau.imagefingerprint.database.AlgorithmWithFingerprint;
import de.fau.imagefingerprint.database.IWFWithSimilarity;
import de.fau.imagefingerprint.database.ImageDatabase;
import de.fau.imagefingerprint.database.NameWithFingerprint;
import de.fau.imagefingerprint.database.Similarity;
import de.fau.imagefingerprint.fingerprint.FingerprintComperator;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Tobias
 */
public class SimilarityFinder {
    private ArrayList<IWFWithSimilarity> iwfWithSimilarities = null;
    private double threshold = 0.2;

    public ArrayList<IWFWithSimilarity> getIwfWithSimilarities() {
        return iwfWithSimilarities;
    }

    public void setIwfWithSimilarities(ArrayList<IWFWithSimilarity> iwfWithSimilarities) {
        this.iwfWithSimilarities = iwfWithSimilarities;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    
    public SimilarityFinder() {
        iwfWithSimilarities = new ArrayList<>();
        this.threshold = 0.2;
    }
    
    public SimilarityFinder(double threshold) {
        iwfWithSimilarities = new ArrayList<>();
        this.threshold = threshold;
    }

    
    
    public void findAllSimilarities (ArrayList<AlgorithmWithFingerprint> fingerprintsToCompare) 
            throws SQLException, IOException, Exception {
        ArrayList<IWFWithSimilarity> imageList = new ArrayList<>();

        for(AlgorithmWithFingerprint fingerpintToCompare : fingerprintsToCompare) {
            FingerprintComperator fingerprintComperator = new FingerprintComperator();
            fingerprintComperator.setAlgorithm(fingerpintToCompare.getAlgorithm());
            fingerprintComperator.setFirstFingerprint(fingerpintToCompare.getFingerprint());
            
            ArrayList<NameWithFingerprint> nwfs = 
                    ImageDatabase.getAllNamesWithFingerprint(
                            ImageDatabase.getColumnName(fingerpintToCompare.getAlgorithm()));     
            for (NameWithFingerprint nwf : nwfs) {
                fingerprintComperator.setSecondFingerprint(nwf.getFingerprint());
                double similarityRating = fingerprintComperator.compare();
                if(similarityRating < this.threshold) {
                    int index = listContainsNameAt(imageList, nwf.getName());
                    if(index >= 0) {
                        imageList.get(index).getSimilarity().add(
                                new Similarity(
                                        similarityRating, 
                                        fingerpintToCompare.getAlgorithm().toString()));
                    }
                    else {
                        IWFWithSimilarity image = new IWFWithSimilarity(
                                ImageDatabase.getImageByName(nwf.getName()), 
                                new ArrayList<Similarity>());
                        image.getSimilarity().add(
                                new Similarity(similarityRating, fingerpintToCompare.getAlgorithm().toString()));
                        imageList.add(image);
                    }
                }
            }
        }
        iwfWithSimilarities = imageList;
    }
    
    
    private int listContainsNameAt(ArrayList<IWFWithSimilarity> images, String name) {
        for(int i = 0; i < images.size(); ++i) {
            if(images.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }
    
}
