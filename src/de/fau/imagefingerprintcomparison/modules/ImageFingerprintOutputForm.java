/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules;

import de.fau.imagefingerprint.database.ImageWithFingerprint;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Tobias
 */
public class ImageFingerprintOutputForm extends javax.swing.JFrame {

    private int activeImagePosition = 0;
    private ArrayList<ImageWithFingerprint> databaseImages = null;
    private ArrayList<Double> similarityRatingsMHO = null;
    private ArrayList<Double> similarityRatingsBMV = null;
    private ArrayList<Double> similarityRatingsCH = null;
    
    /**
     * Creates new form ImageFingerprintOutputForm
     */
    public ImageFingerprintOutputForm() {
        initComponents();
    }
    
    /**
     * Creates new form ImageFingerprintOutputForm
     */
    public ImageFingerprintOutputForm(
            ImageWithFingerprint inputImage, 
            ArrayList<ImageWithFingerprint> databaseImages,
            ArrayList<Double> similarityRatingsMHO,
            ArrayList<Double> similarityRatingsBMV,
            ArrayList<Double> similarityRatingsCH) {
        initComponents();
       
        this.databaseImages = databaseImages;
        this.similarityRatingsMHO = similarityRatingsMHO;
        this.similarityRatingsBMV = similarityRatingsBMV;
        this.similarityRatingsCH = similarityRatingsCH;
        this.activeImagePosition = 0;
        
        labelInputImageName.setText(inputImage.getName());
        labelInputImage.setIcon(new ImageIcon(inputImage.getImage()));
        labelNumberOfSimilarImages.setText("Number of similar Images: " + databaseImages.size());
        
        double activeSimilarityMHO = -1, activeSimilarityBMV = -1, activeSimilarityCH = -1;
        if(similarityRatingsMHO.size() > 0)
            activeSimilarityMHO = similarityRatingsMHO.get(0);
        if(similarityRatingsBMV.size() > 0)
            activeSimilarityBMV = similarityRatingsBMV.get(0);
        if(similarityRatingsCH.size() > 0)
           activeSimilarityCH = similarityRatingsCH.get(0);
        
        if(databaseImages.size() > 0) {
            setDatabaseImageFields(databaseImages.get(0), 
                    activeSimilarityMHO, 
                    activeSimilarityBMV, 
                    activeSimilarityCH);
        }
        if(databaseImages.size() == 1) {
            buttonImageBack.setEnabled(false);
            buttonImageForward.setEnabled(false);
        }
  
    }

    private void setDatabaseImageFields(ImageWithFingerprint activeDatabaseImage, 
            double similarityMHO, double similarityBMV, double similarityCH) {
        labelDatabaseImage.setIcon(new ImageIcon(activeDatabaseImage.getImage()));
        labelDatabaseImageName.setText(activeDatabaseImage.getName());
        if(similarityMHO < 0)
            labelMHOSimilarity.setText("Marr Hildreth Operator: Unknown");
        else
            labelMHOSimilarity.setText("Marr Hildreth Operator: " + similarityMHO);
        if(similarityBMV < 0)
            labelMHOSimilarity.setText("Block Mean Value: Unknown");
        else
            labelBMVSimilarity.setText("Block Mean Value: " + similarityBMV);
        if(similarityCH < 0)
            labelMHOSimilarity.setText("Color Histogram: Unknown");
        else
            labelCHSimilarity.setText("Color Histogram: " + similarityCH);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelInputImageName = new javax.swing.JLabel();
        labelDatabaseImageName = new javax.swing.JLabel();
        labelMHOSimilarity = new javax.swing.JLabel();
        labelBMVSimilarity = new javax.swing.JLabel();
        labelCHSimilarity = new javax.swing.JLabel();
        labelNumberOfSimilarImages = new javax.swing.JLabel();
        paneDatabaseImage = new javax.swing.JScrollPane();
        labelDatabaseImage = new javax.swing.JLabel();
        paneInputImage = new javax.swing.JScrollPane();
        labelInputImage = new javax.swing.JLabel();
        buttonImageBack = new javax.swing.JButton();
        buttonImageForward = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        org.openide.awt.Mnemonics.setLocalizedText(labelInputImageName, org.openide.util.NbBundle.getMessage(ImageFingerprintOutputForm.class, "ImageFingerprintOutputForm.labelInputImageName.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelDatabaseImageName, org.openide.util.NbBundle.getMessage(ImageFingerprintOutputForm.class, "ImageFingerprintOutputForm.labelDatabaseImageName.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelMHOSimilarity, org.openide.util.NbBundle.getMessage(ImageFingerprintOutputForm.class, "ImageFingerprintOutputForm.labelMHOSimilarity.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelBMVSimilarity, org.openide.util.NbBundle.getMessage(ImageFingerprintOutputForm.class, "ImageFingerprintOutputForm.labelBMVSimilarity.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelCHSimilarity, org.openide.util.NbBundle.getMessage(ImageFingerprintOutputForm.class, "ImageFingerprintOutputForm.labelCHSimilarity.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelNumberOfSimilarImages, org.openide.util.NbBundle.getMessage(ImageFingerprintOutputForm.class, "ImageFingerprintOutputForm.labelNumberOfSimilarImages.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelDatabaseImage, org.openide.util.NbBundle.getMessage(ImageFingerprintOutputForm.class, "ImageFingerprintOutputForm.labelDatabaseImage.text")); // NOI18N
        paneDatabaseImage.setViewportView(labelDatabaseImage);

        org.openide.awt.Mnemonics.setLocalizedText(labelInputImage, org.openide.util.NbBundle.getMessage(ImageFingerprintOutputForm.class, "ImageFingerprintOutputForm.labelInputImage.text")); // NOI18N
        paneInputImage.setViewportView(labelInputImage);

        org.openide.awt.Mnemonics.setLocalizedText(buttonImageBack, org.openide.util.NbBundle.getMessage(ImageFingerprintOutputForm.class, "ImageFingerprintOutputForm.buttonImageBack.text")); // NOI18N
        buttonImageBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonImageBackActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(buttonImageForward, org.openide.util.NbBundle.getMessage(ImageFingerprintOutputForm.class, "ImageFingerprintOutputForm.buttonImageForward.text")); // NOI18N
        buttonImageForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonImageForwardActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelInputImageName)
                            .addComponent(labelNumberOfSimilarImages))
                        .addGap(244, 244, 244)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelMHOSimilarity)
                            .addComponent(labelDatabaseImageName)
                            .addComponent(labelBMVSimilarity)
                            .addComponent(labelCHSimilarity))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(paneInputImage, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(paneDatabaseImage, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(buttonImageBack, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonImageForward, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonImageBack)
                    .addComponent(buttonImageForward))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(paneDatabaseImage, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                    .addComponent(paneInputImage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelInputImageName)
                    .addComponent(labelDatabaseImageName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMHOSimilarity)
                    .addComponent(labelNumberOfSimilarImages))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelBMVSimilarity)
                .addGap(11, 11, 11)
                .addComponent(labelCHSimilarity)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonImageBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonImageBackActionPerformed
        if(activeImagePosition == 0) {
            activeImagePosition = databaseImages.size() - 1;
        }
        else
            --activeImagePosition;
        
        double activeSimilarityMHO = -1, activeSimilarityBMV = -1, activeSimilarityCH = -1;
        if(similarityRatingsMHO.size() > activeImagePosition)
            activeSimilarityMHO = similarityRatingsMHO.get(activeImagePosition);
        if(similarityRatingsBMV.size() > activeImagePosition)
            activeSimilarityBMV = similarityRatingsBMV.get(activeImagePosition);
        if(similarityRatingsCH.size() > activeImagePosition)
            activeSimilarityCH = similarityRatingsCH.get(activeImagePosition);
        
        if(databaseImages.size() > 0) {
            setDatabaseImageFields(databaseImages.get(activeImagePosition), 
                    activeSimilarityMHO, 
                    activeSimilarityBMV, 
                    activeSimilarityCH);
        }
    }//GEN-LAST:event_buttonImageBackActionPerformed

    private void buttonImageForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonImageForwardActionPerformed
        activeImagePosition = (activeImagePosition + 1) % databaseImages.size();
        
        double activeSimilarityMHO = -1, activeSimilarityBMV = -1, activeSimilarityCH = -1;
        if(similarityRatingsMHO.size() > activeImagePosition)
            activeSimilarityMHO = similarityRatingsMHO.get(activeImagePosition);
        if(similarityRatingsBMV.size() > activeImagePosition)
            activeSimilarityBMV = similarityRatingsBMV.get(activeImagePosition);
        if(similarityRatingsCH.size() > activeImagePosition)
            activeSimilarityCH = similarityRatingsCH.get(activeImagePosition);
        
        if(databaseImages.size() > 0) {
            setDatabaseImageFields(databaseImages.get(activeImagePosition), 
                    activeSimilarityMHO, 
                    activeSimilarityBMV, 
                    activeSimilarityCH);
        }
    }//GEN-LAST:event_buttonImageForwardActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ImageFingerprintOutputForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImageFingerprintOutputForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImageFingerprintOutputForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImageFingerprintOutputForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ImageFingerprintOutputForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonImageBack;
    private javax.swing.JButton buttonImageForward;
    private javax.swing.JLabel labelBMVSimilarity;
    private javax.swing.JLabel labelCHSimilarity;
    private javax.swing.JLabel labelDatabaseImage;
    private javax.swing.JLabel labelDatabaseImageName;
    private javax.swing.JLabel labelInputImage;
    private javax.swing.JLabel labelInputImageName;
    private javax.swing.JLabel labelMHOSimilarity;
    private javax.swing.JLabel labelNumberOfSimilarImages;
    private javax.swing.JScrollPane paneDatabaseImage;
    private javax.swing.JScrollPane paneInputImage;
    // End of variables declaration//GEN-END:variables
}
