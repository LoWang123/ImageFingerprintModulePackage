/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules.contentviewer;

import de.fau.imagefingerprintcomparison.modules.ImageFingerprintIngestFactory;
import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import org.openide.nodes.Node;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataContentViewer;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.ingest.IngestServices;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.SleuthkitCase;
import org.sleuthkit.datamodel.TskCoreException;

/**
 *
 * @author Tobi
 */
@ServiceProvider(service = DataContentViewer.class)
public class ImageFingerprintDataContentViewer extends javax.swing.JPanel implements DataContentViewer{
    
    private Logger logger = IngestServices.getInstance().getLogger("ImageFingerprintDataContentViewer");
    
    private static int attributeIdMHO = -1;
    private static int attributeIdBMV = -1;
    private static int attributeIdCH = -1;
    
    private byte[] fingerprintMHO = null;
    private byte[] fingerprintBMV = null;
    private byte[] fingerprintCH = null;
    
    public ImageFingerprintDataContentViewer() {
        initComponents();
        
        textFieldFingerprintBMV.setEnabled(false);
        textFieldFingerprintMHO.setEnabled(false);
        textFieldFingerprintCH.setEnabled(false);
    }

    @Override
    public void setNode(Node selectedNode) {
        AbstractFile abstractFile = selectedNode.getLookup().lookup(AbstractFile.class);
        initializeAttributeIds();
        
        labelImageName.setText("File: " + abstractFile.getName());
        try {        
            for(BlackboardArtifact artifact: abstractFile.getAllArtifacts()) {
                for(BlackboardAttribute att: artifact.getAttributes()) {
                    if(att.getAttributeTypeID() == attributeIdBMV) {
                        fingerprintBMV = att.getValueBytes();
                        String out = "";
                        for(byte b: fingerprintBMV) {
                            out += b + ", ";
                        }
                        textFieldFingerprintMHO.setText(out);
                    }
                    if(att.getAttributeTypeID() == attributeIdMHO) {
                        fingerprintMHO = att.getValueBytes();
                        String out = "";
                        for(byte b: fingerprintMHO) {
                            out += b + ", ";
                        }
                        textFieldFingerprintBMV.setText(out);
                    }
                    if(att.getAttributeTypeID() == attributeIdCH) {
                        fingerprintCH = att.getValueBytes();
                        String out = "";
                        for(byte b: fingerprintCH) {
                            out += b + ", ";
                        }
                        textFieldFingerprintCH.setText(out);
                    }
                }
            }
        } catch (TskCoreException ex) {
            logger.log(Level.SEVERE, "Error accessing BlackboardAttributes (setNodes)", ex);
        }
    }

    
    @Override
    public String getTitle() {
        return "Fingerprints";
    }

    @Override
    public String getToolTip() {
        return "Displays the Fingerprints of this Image";
    }

    @Override
    public DataContentViewer createInstance() {
        return new ImageFingerprintDataContentViewer();
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void resetComponent() {
        fingerprintMHO = null;
        fingerprintBMV = null;
        fingerprintCH = null;
        
        labelImageName.setText("");
        textFieldFingerprintMHO.setText("");
        textFieldFingerprintBMV.setText("");
        textFieldFingerprintCH.setText("");
    }

    @Override
    public boolean isSupported(Node node) {
        // get a Content datamodel object out of the node
        AbstractFile abstractFile = node.getLookup().lookup(AbstractFile.class);
        
        if(abstractFile == null) {
            return false;
        }
        
        initializeAttributeIds();
        return isSupported(abstractFile);
    }
    
    private void initializeAttributeIds() {
        if(attributeIdMHO == -1 || attributeIdBMV == -1 || attributeIdCH == -1) {
            Case autopsyCase = Case.getCurrentCase();
            SleuthkitCase sleuthkitCase = autopsyCase.getSleuthkitCase();
            try {                
                if (attributeIdMHO == -1) {
                    attributeIdMHO = sleuthkitCase.getAttrTypeID(
                        ImageFingerprintIngestFactory.getAttributeTypeStringMHO());
                    if (attributeIdMHO == -1) {
                        attributeIdMHO = sleuthkitCase.addAttrType(
                            ImageFingerprintIngestFactory.getAttributeTypeStringMHO(), 
                            ImageFingerprintIngestFactory.getAttributeDescriptionMHO());
                    }
                }
                if (attributeIdBMV == -1) {
                    attributeIdBMV = sleuthkitCase.getAttrTypeID(
                        ImageFingerprintIngestFactory.getAttributeTypeStringBMV());
                    if (attributeIdBMV == -1) {
                        attributeIdBMV = sleuthkitCase.addAttrType(
                            ImageFingerprintIngestFactory.getAttributeTypeStringBMV(), 
                            ImageFingerprintIngestFactory.getAttributeDescriptionBMV());
                    }
                }
                if (attributeIdCH == -1) {
                    attributeIdCH = sleuthkitCase.getAttrTypeID(
                        ImageFingerprintIngestFactory.getAttributeTypeStringCH());
                    if (attributeIdCH == -1) {
                        attributeIdCH = sleuthkitCase.addAttrType(
                            ImageFingerprintIngestFactory.getAttributeTypeStringCH(), 
                            ImageFingerprintIngestFactory.getAttributeDescriptionCH());
                    }
                }
            } catch (TskCoreException ex) {
                logger.log(Level.SEVERE, "Error searching fingerprintAttribute ids (initializeAttributeIds)", ex);
                attributeIdMHO = -1;
                attributeIdBMV = -1;
                attributeIdCH = -1;
            }
        }
    }
    
    private boolean isSupported(AbstractFile abstractFile) {
        try {
            for(BlackboardArtifact artifact: abstractFile.getAllArtifacts()) {
                for(BlackboardAttribute att: artifact.getAttributes()) {
                    if(att.getAttributeTypeID() == attributeIdBMV) {
                        return true;
                    }
                    if(att.getAttributeTypeID() == attributeIdMHO) {
                        return true;
                    }
                    if(att.getAttributeTypeID() == attributeIdCH) {
                        return true;
                    }
                }
            }
        } catch (TskCoreException ex) {
            logger.log(Level.SEVERE, "Error accessing BlackboardAttributes (isSupported) ", ex);
        }
        return false;
    }
    
    @Override
    public int isPreferred(Node node) {
        AbstractFile abstractFile = node.getLookup().lookup(AbstractFile.class);
        
        initializeAttributeIds();
        if(isSupported(abstractFile)) {
            return 2;
        }
        return 0;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        labelImageName = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        labelFingerprintMHO = new javax.swing.JLabel();
        textFieldFingerprintMHO = new javax.swing.JTextField();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        labelFingerprintBMV = new javax.swing.JLabel();
        textFieldFingerprintBMV = new javax.swing.JTextField();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        labelFingerprintCH = new javax.swing.JLabel();
        textFieldFingerprintCH = new javax.swing.JTextField();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        filler10 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        filler11 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(labelImageName, org.openide.util.NbBundle.getMessage(ImageFingerprintDataContentViewer.class, "ImageFingerprintDataContentViewer.labelImageName.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(labelImageName, gridBagConstraints);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/fau/imagefingerprintcomparison/modules/resources/Save-ico_small.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(ImageFingerprintDataContentViewer.class, "ImageFingerprintDataContentViewer.jButton1.text")); // NOI18N
        jButton1.setBorder(null);
        jButton1.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton1.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(jButton1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        add(filler1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(labelFingerprintMHO, org.openide.util.NbBundle.getMessage(ImageFingerprintDataContentViewer.class, "ImageFingerprintDataContentViewer.labelFingerprintMHO.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(labelFingerprintMHO, gridBagConstraints);

        textFieldFingerprintMHO.setText(org.openide.util.NbBundle.getMessage(ImageFingerprintDataContentViewer.class, "ImageFingerprintDataContentViewer.textFieldFingerprintMHO.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 657;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 10.0;
        add(textFieldFingerprintMHO, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        add(filler2, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(labelFingerprintBMV, org.openide.util.NbBundle.getMessage(ImageFingerprintDataContentViewer.class, "ImageFingerprintDataContentViewer.labelFingerprintBMV.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(labelFingerprintBMV, gridBagConstraints);

        textFieldFingerprintBMV.setText(org.openide.util.NbBundle.getMessage(ImageFingerprintDataContentViewer.class, "ImageFingerprintDataContentViewer.textFieldFingerprintBMV.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 657;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 3.0;
        add(textFieldFingerprintBMV, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        add(filler3, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(labelFingerprintCH, org.openide.util.NbBundle.getMessage(ImageFingerprintDataContentViewer.class, "ImageFingerprintDataContentViewer.labelFingerprintCH.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(labelFingerprintCH, gridBagConstraints);

        textFieldFingerprintCH.setText(org.openide.util.NbBundle.getMessage(ImageFingerprintDataContentViewer.class, "ImageFingerprintDataContentViewer.textFieldFingerprintCH.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 657;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 3.0;
        add(textFieldFingerprintCH, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 6;
        add(filler4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.weighty = 2.0;
        add(filler8, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 8;
        add(filler9, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        add(filler10, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.weighty = 1.0;
        add(filler11, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(this);
        
        String name = labelImageName.getText().substring(6, labelImageName.getText().lastIndexOf('.'));
        
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            File outFile = new File(file.getAbsoluteFile() + ".txt");
            int i = 1;
            while(outFile.exists()) {
                outFile = new File(file.getAbsoluteFile() +" (" + i +  ").txt");
            }
            try {
                PrintWriter writer = new PrintWriter(outFile, "UTF-8");
                if(fingerprintMHO != null) {
                    writer.println("MHO: " + textFieldFingerprintMHO.getText());
                }
                if(fingerprintBMV != null) {
                    writer.println("BMV: " + textFieldFingerprintBMV.getText());
                }
                if(fingerprintCH != null) {
                    writer.println("CH: " + textFieldFingerprintCH.getText());
                }
                writer.close();
            }
            catch (FileNotFoundException ex) {
                logger.log(Level.SEVERE, "Error writing to File: " + outFile.getAbsolutePath(), ex);
            }
            catch (UnsupportedEncodingException ex) {
                logger.log(Level.SEVERE, "Error writing to File: " + outFile.getAbsolutePath() + " encoding UTF-8", ex);
            }
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler10;
    private javax.swing.Box.Filler filler11;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel labelFingerprintBMV;
    private javax.swing.JLabel labelFingerprintCH;
    private javax.swing.JLabel labelFingerprintMHO;
    private javax.swing.JLabel labelImageName;
    private javax.swing.JTextField textFieldFingerprintBMV;
    private javax.swing.JTextField textFieldFingerprintCH;
    private javax.swing.JTextField textFieldFingerprintMHO;
    // End of variables declaration//GEN-END:variables
}
