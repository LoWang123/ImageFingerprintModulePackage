/*
 * Sample module ingest job settings panel in the public domain.  
 * Feel free to use this as a template for your module ingest job settings
 * panels.
 * 
 *  Contact: Brian Carrier [carrier <at> sleuthkit [dot] org]
 *
 *  This is free and unencumbered software released into the public domain.
 *  
 *  Anyone is free to copy, modify, publish, use, compile, sell, or
 *  distribute this software, either in source code form or as a compiled
 *  binary, for any purpose, commercial or non-commercial, and by any
 *  means.
 *  
 *  In jurisdictions that recognize copyright laws, the author or authors
 *  of this software dedicate any and all copyright interest in the
 *  software to the public domain. We make this dedication for the benefit
 *  of the public at large and to the detriment of our heirs and
 *  successors. We intend this dedication to be an overt act of
 *  relinquishment in perpetuity of all present and future rights to this
 *  software under copyright law.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE. 
 */
package de.fau.imagefingerprintcomparison.modules;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettings;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettingsPanel;

/**
 * UI component used to make per ingest job settings for sample ingest modules.
 */
public class ImageFingerprintJobSettingsPanel extends IngestModuleIngestJobSettingsPanel {
    
    /**
     * Creates new form SampleIngestModuleIngestJobSettings
     */
    public ImageFingerprintJobSettingsPanel(ImageFingerprintJobSettings settings) {
        initComponents();
        customizeComponents(settings);
    }

    private void customizeComponents(ImageFingerprintJobSettings settings) {
        checkboxBMV.setSelected(settings.compareBMV());
        checkboxMHO.setSelected(settings.compareMHO());
        checkboxCH.setSelected(settings.compareCH());
        checkboxCompareWithDatabase.setSelected(settings.compareToDatabaseImages());
        comboBoxSimilarityRequirement.addItem("AtLeastOne");
        comboBoxSimilarityRequirement.addItem("Majority");
        comboBoxSimilarityRequirement.addItem("All");
        comboBoxSimilarityRequirement.setSelectedIndex(0);
        
        textfieldThreshold.setText("0.2");
        
        //TODO
        checkboxBMV.setToolTipText("Calculate an imagefingerprint using block mean values. Based 'Block Mean Value Based Image Perceptual Hashing' by Bian Yang, Fan Gu and Xiamu Niu.");
        checkboxMHO.setToolTipText("Calculate an imagefingerprint using the Marr Hildreth Operator. Based on 'Implementation and Benchmarking of Perceptual Image Hash Functions' by Christoph Zauner");
        checkboxCH.setToolTipText("Calculate an imagefingerprint based on its ColorHistogram. Inspired by the absence of color features in the methods mentioned above.");
        checkboxCompareWithDatabase.setToolTipText("Compare the Images ingested to images in the ImageFingerprintDatabase. Access/Modify this database in 'Advanced' options.");
        labelThreshold.setToolTipText("The similarity result accepted as similar enough. 0 corresponds to a perfect match, 0.5 equals absolute randomness.");
        labelSimilarityRequirement.setToolTipText("When multiple Fingerprints are selected. How many of those need to consider the image similar in order for autopsy to consider the images similar.");
        
        checkboxCompareWithDatabase.setSelected(true);
        textfieldThreshold.setToolTipText("");
        comboBoxSimilarityRequirement.setToolTipText("");
        
        checkboxCompareWithDatabase.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                labelThreshold.setEnabled(checkboxCompareWithDatabase.isSelected());
                labelSimilarityRequirement.setEnabled(checkboxCompareWithDatabase.isSelected());
                textfieldThreshold.setEnabled(checkboxCompareWithDatabase.isSelected());
                comboBoxSimilarityRequirement.setEnabled(checkboxCompareWithDatabase.isSelected());
            }
        });

        
    }

    /**
     * Gets the ingest job settings for an ingest module.
     *
     * @return The ingest settings.
     */
    @Override
    public IngestModuleIngestJobSettings getSettings() {
        float threshold = 0.2f;
        try  
        {  
            threshold = Float.parseFloat(textfieldThreshold.getText()); 
        }  
        catch(NumberFormatException nfe)  
        {  
            threshold = 0.2f; 
        }  
        
        SimilarityRequirement requirement = SimilarityRequirement.AtLeastOne;
        switch(comboBoxSimilarityRequirement.getSelectedIndex()) {
            case 0:
                break;
            case 1:
                requirement = SimilarityRequirement.Majority;
                break;
            case 2:
                requirement = SimilarityRequirement.All;
                break;
        }
        
        return new ImageFingerprintJobSettings(
                checkboxCH.isSelected(), 
                checkboxMHO.isSelected(), 
                checkboxBMV.isSelected(), 
                checkboxCompareWithDatabase.isSelected(),
                threshold,
                requirement);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        checkboxCH = new javax.swing.JCheckBox();
        checkboxBMV = new javax.swing.JCheckBox();
        checkboxMHO = new javax.swing.JCheckBox();
        checkboxCompareWithDatabase = new javax.swing.JCheckBox();
        labelThreshold = new javax.swing.JLabel();
        textfieldThreshold = new javax.swing.JTextField();
        comboBoxSimilarityRequirement = new javax.swing.JComboBox<>();
        labelSimilarityRequirement = new javax.swing.JLabel();

        checkboxCH.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(checkboxCH, org.openide.util.NbBundle.getMessage(ImageFingerprintJobSettingsPanel.class, "ImageFingerprintJobSettingsPanel.checkboxCH.text")); // NOI18N

        checkboxBMV.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(checkboxBMV, org.openide.util.NbBundle.getMessage(ImageFingerprintJobSettingsPanel.class, "ImageFingerprintJobSettingsPanel.checkboxBMV.text")); // NOI18N

        checkboxMHO.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(checkboxMHO, org.openide.util.NbBundle.getMessage(ImageFingerprintJobSettingsPanel.class, "ImageFingerprintJobSettingsPanel.checkboxMHO.text")); // NOI18N

        checkboxCompareWithDatabase.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(checkboxCompareWithDatabase, org.openide.util.NbBundle.getMessage(ImageFingerprintJobSettingsPanel.class, "ImageFingerprintJobSettingsPanel.checkboxCompareWithDatabase.text")); // NOI18N
        checkboxCompareWithDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxCompareWithDatabaseActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(labelThreshold, org.openide.util.NbBundle.getMessage(ImageFingerprintJobSettingsPanel.class, "ImageFingerprintJobSettingsPanel.labelThreshold.text")); // NOI18N

        textfieldThreshold.setText(org.openide.util.NbBundle.getMessage(ImageFingerprintJobSettingsPanel.class, "ImageFingerprintJobSettingsPanel.textfieldThreshold.text")); // NOI18N
        textfieldThreshold.setToolTipText(org.openide.util.NbBundle.getMessage(ImageFingerprintJobSettingsPanel.class, "ImageFingerprintJobSettingsPanel.textfieldThreshold.toolTipText")); // NOI18N
        textfieldThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textfieldThresholdActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(labelSimilarityRequirement, org.openide.util.NbBundle.getMessage(ImageFingerprintJobSettingsPanel.class, "ImageFingerprintJobSettingsPanel.labelSimilarityRequirement.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelThreshold)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(textfieldThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkboxCH)
                            .addComponent(checkboxBMV)
                            .addComponent(checkboxMHO)
                            .addComponent(checkboxCompareWithDatabase))
                        .addGap(0, 15, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelSimilarityRequirement)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboBoxSimilarityRequirement, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(checkboxBMV)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkboxMHO)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkboxCH)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(checkboxCompareWithDatabase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textfieldThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelThreshold))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxSimilarityRequirement, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSimilarityRequirement))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void textfieldThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textfieldThresholdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textfieldThresholdActionPerformed

    private void checkboxCompareWithDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxCompareWithDatabaseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkboxCompareWithDatabaseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkboxBMV;
    private javax.swing.JCheckBox checkboxCH;
    private javax.swing.JCheckBox checkboxCompareWithDatabase;
    private javax.swing.JCheckBox checkboxMHO;
    private javax.swing.JComboBox<String> comboBoxSimilarityRequirement;
    private javax.swing.JLabel labelSimilarityRequirement;
    private javax.swing.JLabel labelThreshold;
    private javax.swing.JTextField textfieldThreshold;
    // End of variables declaration//GEN-END:variables
}
