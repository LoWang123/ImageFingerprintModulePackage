/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules;

/**
 *
 * @author Tobias
 */
public class FingerprintCompareResult {
    private int numberAttempts = 0;
    private int numberSuccess = 0;

    public FingerprintCompareResult() {
    }
    
    public void addAttempt() {
        ++numberAttempts;
    }
    
    public void addSuccess() {
        ++numberSuccess;
    }

    public int getNumberAttempts() {
        return numberAttempts;
    }

    public int getNumberSuccess() {
        return numberSuccess;
    }
    
    public boolean isGoodEnoughFor(SimilarityRequirement similarityRequirement) {
        switch(similarityRequirement) {
            case AtLeastOne:
                return numberSuccess > 0;
            case Majority:
                return numberSuccess > (double)numberAttempts / 2.;
            case All:
                return numberSuccess == numberAttempts;
        }
        return false;
    }
}
