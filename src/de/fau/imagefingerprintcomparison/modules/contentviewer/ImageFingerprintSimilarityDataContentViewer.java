/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules.contentviewer;

import de.fau.imagefingerprint.database.IWFWithSimilarity;
import de.fau.imagefingerprint.database.ImageWithFingerprint;
import de.fau.imagefingerprint.database.Similarity;
import de.fau.imagefingerprintcomparison.modules.ImageFingerprintIngestFactory;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
public class ImageFingerprintSimilarityDataContentViewer extends javax.swing.JPanel implements DataContentViewer {

    private int artifactIdSimilarImage = -1;
    private int attributeIdSimilarImageName = -1;
    private int attributeIdSimilarImage = -1;
    private int attributeIdSimilarityMHO = -1;
    private int attributeIdSimilarityBMV = -1;
    private int attributeIdSimilarityCH = -1;
    
    private ArrayList<IWFWithSimilarity> currentSimilarImages = new ArrayList<>();
    private int currentPosition = -1;
    
    private Logger logger = IngestServices.getInstance().getLogger("ImageFingerprintSimilarityDataContentViewer");
    
    @Override
    public String getTitle() {
        return "Fingerprint Similarities";
    }

    @Override
    public String getToolTip() {
        return "Displays the similar images from the database as calculated by the corresponding Ingest Module.";
    }
    
    /**
     * Creates new form ImageFingerprintSimilarityDataContentViewer
     */
    public ImageFingerprintSimilarityDataContentViewer() {
        initComponents();
        labelSimilarityResult.setToolTipText("0 corresponds to a perfect match, 0.5 equals absolute randomness.");
        buttonNext.setEnabled(true);
        buttonPrev.setEnabled(true);
        
        buttonNext.setBackground(Color.BLACK);
        buttonNext.setContentAreaFilled(false);
        buttonNext.setOpaque(true);
        
        buttonPrev.setBackground(Color.BLACK);
        buttonPrev.setContentAreaFilled(false);
        buttonPrev.setOpaque(true);

    }
    
    @Override
    public DataContentViewer createInstance() {
        return new ImageFingerprintSimilarityDataContentViewer();
    }
    
    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void resetComponent() {
        artifactIdSimilarImage = -1;
        attributeIdSimilarImageName = -1;
        attributeIdSimilarImage = -1;
        attributeIdSimilarityMHO = -1;
        attributeIdSimilarityBMV = -1;
        attributeIdSimilarityCH = -1;
    
        currentSimilarImages = new ArrayList<>();
        currentPosition = -1;
    }
    
    @Override
    public boolean isSupported(Node node) {
        AbstractFile abstractFile = node.getLookup().lookup(AbstractFile.class);
        
        if(abstractFile == null) {
            return false;
        }
        
        initializeAttributeIds();
        return isSupported(abstractFile);
    }
    
    private void initializeAttributeIds() {
        Case autopsyCase = Case.getCurrentCase();
        SleuthkitCase sleuthkitCase = autopsyCase.getSleuthkitCase();
        
        try {
            if (artifactIdSimilarImage == -1) {
                artifactIdSimilarImage = sleuthkitCase.getArtifactTypeID(
                            ImageFingerprintIngestFactory.getArtifactTypeStringSimilarImage());
                if (artifactIdSimilarImage == -1) {
                    artifactIdSimilarImage = sleuthkitCase.addArtifactType(
                            ImageFingerprintIngestFactory.getArtifactTypeStringSimilarImage(), 
                            ImageFingerprintIngestFactory.getArtifactDescriptionSimilarImage());
                }
            }
            if (attributeIdSimilarImageName == -1) {
                attributeIdSimilarImageName = sleuthkitCase.getAttrTypeID(
                        ImageFingerprintIngestFactory.getAttributeTypeStringSimilarityImageName());
                if (attributeIdSimilarImageName == -1) {
                    attributeIdSimilarImageName = sleuthkitCase.addAttrType(
                            ImageFingerprintIngestFactory.getAttributeTypeStringSimilarityImageName(), 
                            ImageFingerprintIngestFactory.getAttributeDescriptionSimilarityImageName());
                }
            }
            if (attributeIdSimilarImage == -1) {
                attributeIdSimilarImage = sleuthkitCase.getAttrTypeID(
                        ImageFingerprintIngestFactory.getAttributeTypeStringSimilarImage());
                if (attributeIdSimilarImage == -1) {
                    attributeIdSimilarImage = sleuthkitCase.addAttrType(
                            ImageFingerprintIngestFactory.getAttributeTypeStringSimilarImage(), 
                            ImageFingerprintIngestFactory.getAttributeDescriptionSimilarImage());
                }
            }
            if (attributeIdSimilarityMHO == -1) {
                attributeIdSimilarityMHO = sleuthkitCase.getAttrTypeID(
                        ImageFingerprintIngestFactory.getAttributeTypeStringSimilarityMHO());
                if (attributeIdSimilarityMHO == -1) {
                    attributeIdSimilarityMHO = sleuthkitCase.addAttrType(
                        ImageFingerprintIngestFactory.getAttributeTypeStringSimilarityMHO(), 
                        ImageFingerprintIngestFactory.getAttributeDescriptionSimilarityMHO());
                }
            }
            if (attributeIdSimilarityBMV == -1) {
                attributeIdSimilarityBMV = sleuthkitCase.getAttrTypeID(
                        ImageFingerprintIngestFactory.getAttributeTypeStringSimilarityBMV());
                if (attributeIdSimilarityBMV == -1) {
                    attributeIdSimilarityBMV = sleuthkitCase.addAttrType(
                        ImageFingerprintIngestFactory.getAttributeTypeStringSimilarityBMV(), 
                        ImageFingerprintIngestFactory.getAttributeDescriptionSimilarityBMV());
                }
            }
            if (attributeIdSimilarityCH == -1) {
                attributeIdSimilarityCH = sleuthkitCase.getAttrTypeID(
                        ImageFingerprintIngestFactory.getAttributeTypeStringSimilarityCH());
                if (attributeIdSimilarityCH == -1) {
                    attributeIdSimilarityCH = sleuthkitCase.addAttrType(
                            ImageFingerprintIngestFactory.getAttributeTypeStringSimilarityCH(), 
                            ImageFingerprintIngestFactory.getAttributeDescriptionSimilarityCH());
                }
            }
        }
        catch (TskCoreException ex) {
            artifactIdSimilarImage = -1;
            attributeIdSimilarImageName = -1;
            attributeIdSimilarImage = -1;
            attributeIdSimilarityMHO = -1;
            attributeIdSimilarityBMV = -1;
            attributeIdSimilarityCH = -1;
            
            logger.log(Level.SEVERE, "Error accessing BlackboardAttributes (initializeAttributeIds)", ex);
        }
    }
    
    private boolean isSupported(AbstractFile abstractFile) {
        try {
            if(abstractFile.getArtifacts(artifactIdSimilarImage).size() > 0) {
                return true;
            }
        }
        catch (TskCoreException ex) {
            logger.log(Level.SEVERE, "Error accessing BlackboardArtifact (isSupported)", ex);
            return false;
        }
        return false;
    }

    @Override
    public int isPreferred(Node node) {
        AbstractFile abstractFile = node.getLookup().lookup(AbstractFile.class);
        
        if(isSupported(abstractFile)) {
            return 2;
        }
        return 0;
    }
    
    @Override
    public void setNode(Node node) {
        AbstractFile abstractFile = node.getLookup().lookup(AbstractFile.class);
        
        currentPosition = -1;
        try {
            for(BlackboardArtifact art: abstractFile.getArtifacts(artifactIdSimilarImage)) {
                BufferedImage image = null;
                String name = "";
                ArrayList<Similarity> similarities = new ArrayList<Similarity>();
                
                for(BlackboardAttribute attr: art.getAttributes()) {
                    if(attr.getAttributeTypeID() == attributeIdSimilarImageName) {
                        name = attr.getValueString();
                    }
                    if(attr.getAttributeTypeID() == attributeIdSimilarImage) {
                        byte[] imageBytes = attr.getValueBytes();
                        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                        image = ImageIO.read(bais);
                    }
                    if(attr.getAttributeTypeID() == attributeIdSimilarityMHO) {
                        similarities.add(new Similarity(attr.getValueDouble(), "MHO"));
                    }
                    if(attr.getAttributeTypeID() == attributeIdSimilarityBMV) {
                        similarities.add(new Similarity(attr.getValueDouble(), "BMV"));
                    }
                    if(attr.getAttributeTypeID() == attributeIdSimilarityCH) {
                        similarities.add(new Similarity(attr.getValueDouble(), "CH"));
                    }
                }
                
                ImageWithFingerprint iwf = new ImageWithFingerprint(image, null, null, null, name, "");
                currentSimilarImages.add(new IWFWithSimilarity(iwf, similarities));
            }
            if(currentSimilarImages.size() > 0) {
                updateByCurrentPosition(0);
                
                buttonPrev.setEnabled(false);
                if(currentSimilarImages.size() == 1) {
                    buttonNext.setEnabled(false);
                }
            }
        }
        catch(TskCoreException ex) {
            logger.log(Level.SEVERE, "Error accessing BlackboardAttribute (setNode)", ex);
        }
        catch(IOException ex) {
            logger.log(Level.SEVERE, "Error in imageOperation (setNode)", ex);
        }
    }
    
    private Image scaleImage(BufferedImage image, JPanel frame, JLabel label) {
        double differenceWidth = (double)image.getWidth() / (double)frame.getWidth();
        double differenceHeight = (double)image.getHeight() / (double)frame.getHeight();
        
        Image scaledImage = image;
        
        if(differenceWidth > differenceHeight) {
            if(differenceWidth > 1) {
                int height = (int)((double)image.getHeight() / differenceWidth);
                scaledImage = image.getScaledInstance(frame.getWidth(), height, Image.SCALE_FAST);
                label.setBounds((int)label.getBounds().getX(), (int)label.getBounds().getY(), frame.getWidth(), height);
                label.setVerticalAlignment(SwingConstants.CENTER);
            }
        }
        else {
            if(differenceHeight > 1) {
                int width = (int)((double)image.getWidth() / differenceHeight);
                scaledImage = image.getScaledInstance(width, frame.getHeight(), Image.SCALE_FAST);
                label.setBounds((int)label.getBounds().getX(), (int)label.getBounds().getY(), width, frame.getHeight());
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }
        }
        
        
        
        return scaledImage;
    } 

    private void updateByCurrentPosition(int newCurrentPosition) {
        if(newCurrentPosition < currentSimilarImages.size()) {
            currentPosition = newCurrentPosition;
            
            IWFWithSimilarity iwfws = currentSimilarImages.get(currentPosition);
            
            Double similarityMHO = -1.;
            Double similarityBMV = -1.;
            Double similarityCH = -1.;
            
            labelSimilarImageName.setText(iwfws.getName() + " (" + (currentPosition+1) + " of " + currentSimilarImages.size() + ")");
            labelImage.setIcon(new ImageIcon(scaleImage(iwfws.getImage(), panelImage, labelImage)));
        //    panelImage.repaint();
        //    panelImage.revalidate();
            for(Similarity sim: iwfws.getSimilarity()) {
                switch(sim.getAlgorithmUsed()) {
                    case "MHO":
                        similarityMHO = sim.getSimilarityRating();
                        break;
                    case "BMV":
                        similarityBMV = sim.getSimilarityRating();
                        break;
                    case "CH":
                        similarityCH = sim.getSimilarityRating();
                        break;
                }
            }
            
            String similarityString = "Similarity Results: ";
            double similaritySum = .0;
            int number = 0;
            if(similarityMHO > -0.5) {
                similarityString += "[MHO: " + similarityMHO.toString() + "] ";
                similaritySum += similarityMHO;
                ++number;
            }
            if(similarityBMV > -0.5) {
                similarityString += "[BMV: " + similarityBMV.toString() + "] ";
                similaritySum += similarityBMV;
                ++number;
            }
            if(similarityCH > -0.5) {
                similarityString += "[CH: " + similarityCH.toString() + "] ";
                similaritySum += similarityCH;
                ++number;
            }
            
            
            if(number > 0) {
                similarityString += "[AVG: "+ similaritySum / (double)number + "]";
            }
            labelSimilarityResult.setText(similarityString);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelSimilarImageName = new javax.swing.JLabel();
        buttonPrev = new javax.swing.JButton();
        panelImage = new javax.swing.JPanel();
        labelImage = new javax.swing.JLabel();
        buttonNext = new javax.swing.JButton();
        labelSimilarityResult = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setLayout(new java.awt.BorderLayout());

        labelSimilarImageName.setBackground(new java.awt.Color(0, 0, 0));
        labelSimilarImageName.setForeground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(labelSimilarImageName, org.openide.util.NbBundle.getMessage(ImageFingerprintSimilarityDataContentViewer.class, "ImageFingerprintSimilarityDataContentViewer.labelSimilarImageName.text")); // NOI18N
        add(labelSimilarImageName, java.awt.BorderLayout.PAGE_START);

        buttonPrev.setBackground(java.awt.SystemColor.controlShadow);
        buttonPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/fau/imagefingerprintcomparison/modules/resources/arrow-left.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(buttonPrev, org.openide.util.NbBundle.getMessage(ImageFingerprintSimilarityDataContentViewer.class, "ImageFingerprintSimilarityDataContentViewer.buttonPrev.text")); // NOI18N
        buttonPrev.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        buttonPrev.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/de/fau/imagefingerprintcomparison/modules/resources/arrow-left-disabled.png"))); // NOI18N
        buttonPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrevActionPerformed(evt);
            }
        });
        add(buttonPrev, java.awt.BorderLayout.WEST);

        panelImage.setBackground(new java.awt.Color(0, 0, 0));
        panelImage.setMaximumSize(new java.awt.Dimension(900, 520));
        panelImage.setLayout(new java.awt.BorderLayout());

        labelImage.setForeground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(labelImage, org.openide.util.NbBundle.getMessage(ImageFingerprintSimilarityDataContentViewer.class, "ImageFingerprintSimilarityDataContentViewer.labelImage.text")); // NOI18N
        panelImage.add(labelImage, java.awt.BorderLayout.CENTER);

        add(panelImage, java.awt.BorderLayout.CENTER);

        buttonNext.setBackground(java.awt.SystemColor.controlShadow);
        buttonNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/fau/imagefingerprintcomparison/modules/resources/arrow-right.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(buttonNext, org.openide.util.NbBundle.getMessage(ImageFingerprintSimilarityDataContentViewer.class, "ImageFingerprintSimilarityDataContentViewer.buttonNext.text")); // NOI18N
        buttonNext.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        buttonNext.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/de/fau/imagefingerprintcomparison/modules/resources/arrow-right-disabled.png"))); // NOI18N
        buttonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNextActionPerformed(evt);
            }
        });
        add(buttonNext, java.awt.BorderLayout.EAST);

        labelSimilarityResult.setForeground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(labelSimilarityResult, org.openide.util.NbBundle.getMessage(ImageFingerprintSimilarityDataContentViewer.class, "ImageFingerprintSimilarityDataContentViewer.labelSimilarityResult.text")); // NOI18N
        add(labelSimilarityResult, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrevActionPerformed
        if(currentPosition - 1 >= 0) {
            --currentPosition;
            updateByCurrentPosition(currentPosition);
            buttonNext.setEnabled(true);
            
            if(currentPosition - 1 < 0) {
                buttonPrev.setEnabled(false);
            }
        }
    }//GEN-LAST:event_buttonPrevActionPerformed

    private void buttonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNextActionPerformed
        if(currentPosition + 1 < currentSimilarImages.size()) {
            ++currentPosition;
            updateByCurrentPosition(currentPosition);
            buttonPrev.setEnabled(true);
            
            if(currentPosition + 1 >= currentSimilarImages.size()) {
                buttonNext.setEnabled(false);
            }
        }
    }//GEN-LAST:event_buttonNextActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonNext;
    private javax.swing.JButton buttonPrev;
    private javax.swing.JLabel labelImage;
    private javax.swing.JLabel labelSimilarImageName;
    private javax.swing.JLabel labelSimilarityResult;
    private javax.swing.JPanel panelImage;
    // End of variables declaration//GEN-END:variables
}
