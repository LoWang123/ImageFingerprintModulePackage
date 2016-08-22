/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules;

import de.fau.imagefingerprint.database.ImageDatabase;
import de.fau.imagefingerprint.database.ImageWithFingerprint;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.sleuthkit.autopsy.corecomponents.OptionsPanel;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.ingest.IngestModuleGlobalSettingsPanel;
import org.sleuthkit.autopsy.ingest.IngestServices;

/**
 *
 * @author Tobi
 */


public class ImageFingerprintGlobalSettingsPanel extends   IngestModuleGlobalSettingsPanel  implements OptionsPanel, PropertyChangeListener {

    private JFileChooser fc;
    private FingerprintControler ctrl;
    
    private Collection<FileWithFingerprints> listToBeStored;
    private Collection<String> listToBeRemoved;
    
    private TaskCreatingFingerprints taskCreatingFingerprints;
    private Collection<ListItemImage> currentList;
    private Logger logger = IngestServices.getInstance().getLogger("ImageFingerprintGlobalSettingsPanel");
    
    /**
     * Creates new form ImageFingerprintGlobalSettingsPanel
     */
    public ImageFingerprintGlobalSettingsPanel() {
        initComponents();

        listboxImages.clearSelection();

        currentList = new ArrayList<>();
        listToBeStored = new ArrayList<>();
        listToBeRemoved = new ArrayList<>();
        ctrl = new FingerprintControler();
        fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg", "bmp"));
        
        buttonAdd.setToolTipText("Adds one or multiple images and calculates their fingerprints. This might take a few seconds per image added. Database configuration might require you to run Autopsy with administory rights, depending on where it is installed.");
        labelBMV.setToolTipText("imagefingerprint calculated using block mean values. Based 'Block Mean Value Based Image Perceptual Hashing' by Bian Yang, Fan Gu and Xiamu Niu.");
        labelMHO.setToolTipText("imagefingerprint calculated using the Marr Hildreth Operator. Based on 'Implementation and Benchmarking of Perceptual Image Hash Functions' by Christoph Zauner");
        labelCH.setToolTipText("imagefingerprint calculated based on its ColorHistogram. Inspired by the absence of color features in the methods mentioned above.");
        
        listboxImages.addListSelectionListener(new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent evt) {

                for(ListItemImage listItemImage: currentList) {
                    if(listItemImage.getName().equals(listboxImages.getSelectedValue())) {
                    
                        if(listItemImage.getName().length() < 25) {
                            labelImageName.setText(listItemImage.getName());
                            labelImageName.setToolTipText(listItemImage.getName());
                        }
                        else {
                            labelImageName.setText(listItemImage.getName().substring(0,25) + "...");
                            labelImageName.setToolTipText(listItemImage.getName());
                        }
                        
                        try {
                            if(listItemImage.getCh() != null) {
                                textFieldCH.setText("calculated");
                                String out = "";
                                for(byte b: listItemImage.getCh()) {
                                    out += b + ", ";
                                }
                                textFieldCH.setToolTipText(out);
                            }
                            else {
                                textFieldCH.setText("not calculated");
                            }
                            if(listItemImage.getBmv() != null) {
                                textFieldBMV.setText("calculated");
                                String out = "";
                                for(byte b: listItemImage.getBmv()) {
                                    out += b + ", ";
                                }
                                textFieldBMV.setToolTipText(out);
                            } 
                            else {
                                textFieldCH.setText("not calculated");
                            }
                            if(listItemImage.getMho()  != null) {
                                textFieldMHO.setText("calculated");
                                String out = "";
                                for(byte b: listItemImage.getMho() ) {
                                    out += b + ", ";
                                }
                                textFieldMHO.setToolTipText(out);
                            }
                            else {
                                textFieldCH.setText("not calculated");
                            }

                            BufferedImage image = listItemImage.getImage();
                            double differenceWidth = (double)image.getWidth() / (double)labelPreviewImage.getWidth();
                            double differenceHeight = (double)image.getHeight() / (double)labelPreviewImage.getHeight();

                            Image scaledImage = null;

                            if(differenceWidth > differenceHeight) {
                                if(differenceWidth > 1) {
                                    int height = (int)((double)image.getHeight() / differenceWidth);
                                    scaledImage = image.getScaledInstance(labelPreviewImage.getWidth() - 1, height - 1, Image.SCALE_FAST);
                                }
                            }
                            else {
                                if(differenceHeight > 1) {
                                    int width = (int)((double)image.getWidth() / differenceHeight);
                                    scaledImage = image.getScaledInstance(width - 1, labelPreviewImage.getHeight() - 1, Image.SCALE_FAST);
                                }
                            }
                            if(differenceHeight <= 1 && differenceWidth <= 1) {
                                scaledImage = image;
                            }

                            labelPreviewImage.setIcon(new ImageIcon(scaledImage));
                        }
                        catch (Exception ex) {
                            logger.log(Level.SEVERE, "Exception (imageDatabase)", ex);
                        }
                    }
                }
                
            }
        });
        
        scrollPaneList.setViewportView(listboxImages);
        
        
        
        try {
            ImageDatabase.firstConnection();
            for(String name: ImageDatabase.getAllImageNames()) {
                currentList.add(new ListItemImageDatabase(name));
            }
            updateListBox();         
        }
        catch (IllegalAccessException ex) {
            logger.log(Level.SEVERE, "IllegalAccessException (imageDatabase)", ex);
        }
        catch (InstantiationException ex) {
            logger.log(Level.SEVERE, "InstantiationException (imageDatabase)", ex);
        }
        catch (ClassNotFoundException ex) {
            logger.log(Level.SEVERE, "ClassNotFoundException (imageDatabase)", ex);
        }
        catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQLException (imageDatabase)", ex);
        } 
    }

    @Override
    public void saveSettings() {
        try {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            Iterator<FileWithFingerprints> storeIt = listToBeStored.iterator();
            while(storeIt.hasNext()) {
                FileWithFingerprints fwf = storeIt.next();
                String name = fwf.getFile().getName();
                String format = fwf.getFile().getName().substring(fwf.getFile().getName().lastIndexOf('.') + 1);
                BufferedImage image = ImageIO.read(fwf.getFile());
                ImageWithFingerprint iwf = new ImageWithFingerprint(
                                image, fwf.getMho(), fwf.getBmv(), fwf.getCh(), 
                                name ,format);
                ImageDatabase.insertIfNameIsUnique(iwf);
            }
            Iterator<String> removeIt = listToBeRemoved.iterator();
            while(removeIt.hasNext()) {
                ImageDatabase.removeWithUniqueName(removeIt.next());
            }
            
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        catch (IllegalAccessException ex) {
            logger.log(Level.SEVERE, "IllegalAccessException (imageDatabase)", ex);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        catch (InstantiationException ex) {
            logger.log(Level.SEVERE, "InstantiationException (imageDatabase)", ex);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        catch (ClassNotFoundException ex) {
            logger.log(Level.SEVERE, "ClassNotFoundException (imageDatabase)", ex);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException (imageDatabase)", ex);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQLException (imageDatabase)", ex);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void updateListBox() {
        if(currentList.size() > 0) {
            String[] currentNames = new String[currentList.size()];

            for(int i = 0; i < currentList.size(); ++i) {
                currentNames[i] = ((ArrayList<ListItemImage>)currentList).get(i).getName();
            }
            listboxImages.setListData(currentNames); 
        }
        else {
            listboxImages.setListData(new String[]{});
        }
    }
    
    /**
     * Load the saved state of all options, and refresh this OptionsPanel
     * accordingly.
     */
    @Override
    public void load() {
        listboxImages.clearSelection();
        
        currentList = new ArrayList<>();
        listToBeStored = new ArrayList<>();
        listToBeRemoved = new ArrayList<>();
        ctrl = new FingerprintControler();
        
        addProgressbar.setValue(0);
        
        try {
            for(String name: ImageDatabase.getAllImageNames()) {
                currentList.add(new ListItemImageDatabase(name));
            }
            updateListBox();         
        }
        catch (IllegalAccessException ex) {
            logger.log(Level.SEVERE, "IllegalAccessException (imageDatabase)", ex);
        }
        catch (InstantiationException ex) {
            logger.log(Level.SEVERE, "InstantiationException (imageDatabase)", ex);
        }
        catch (ClassNotFoundException ex) {
            logger.log(Level.SEVERE, "ClassNotFoundException (imageDatabase)", ex);
        }
        catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQLException (imageDatabase)", ex);
        }   
    }
    
    //OptionsPanel
     /**
     * Store the current state of all options in this OptionsPanel.
     */
    @Override
    public void store() {
        //Not required since the database already fullfills the duty of persistence
    }
    
    
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            if(progress == 0) {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
           
                this.buttonAdd.setEnabled(true);
                this.buttonRemove.setEnabled(true);
                
                for(FileWithFingerprints fwf: taskCreatingFingerprints.getListOfPathsWithFingerprints()) {
                    currentList.add(new ListItemImagePath(
                            fwf.getFile().getPath(), fwf.getFile().getName(), 
                            fwf.getMho(), fwf.getBmv(), fwf.getCh()));
                    listToBeStored.add(fwf);
                }
                
                updateListBox();
            }
            else {
                addProgressbar.setValue(progress + 1);
            }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        labelImageName = new javax.swing.JLabel();
        labelMHO = new javax.swing.JLabel();
        textFieldMHO = new javax.swing.JTextField();
        labelBMV = new javax.swing.JLabel();
        labelCH = new javax.swing.JLabel();
        textFieldBMV = new javax.swing.JTextField();
        textFieldCH = new javax.swing.JTextField();
        buttonAdd = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();
        labelFingerprintComparisonDatabase = new javax.swing.JLabel();
        panelPreviewImage = new javax.swing.JPanel();
        labelPreviewImage = new javax.swing.JLabel();
        addProgressbar = new javax.swing.JProgressBar();
        scrollPaneList = new javax.swing.JScrollPane();
        listboxImages = new javax.swing.JList<>();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        org.openide.awt.Mnemonics.setLocalizedText(labelImageName, org.openide.util.NbBundle.getMessage(ImageFingerprintGlobalSettingsPanel.class, "ImageFingerprintGlobalSettingsPanel.labelImageName.text")); // NOI18N
        labelImageName.setMaximumSize(new java.awt.Dimension(174, 14));

        org.openide.awt.Mnemonics.setLocalizedText(labelMHO, org.openide.util.NbBundle.getMessage(ImageFingerprintGlobalSettingsPanel.class, "ImageFingerprintGlobalSettingsPanel.labelMHO.text")); // NOI18N

        textFieldMHO.setText(org.openide.util.NbBundle.getMessage(ImageFingerprintGlobalSettingsPanel.class, "ImageFingerprintGlobalSettingsPanel.textFieldMHO.text")); // NOI18N
        textFieldMHO.setEnabled(false);
        textFieldMHO.setMaximumSize(new java.awt.Dimension(6, 20));

        org.openide.awt.Mnemonics.setLocalizedText(labelBMV, org.openide.util.NbBundle.getMessage(ImageFingerprintGlobalSettingsPanel.class, "ImageFingerprintGlobalSettingsPanel.labelBMV.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelCH, org.openide.util.NbBundle.getMessage(ImageFingerprintGlobalSettingsPanel.class, "ImageFingerprintGlobalSettingsPanel.labelCH.text")); // NOI18N

        textFieldBMV.setText(org.openide.util.NbBundle.getMessage(ImageFingerprintGlobalSettingsPanel.class, "ImageFingerprintGlobalSettingsPanel.textFieldBMV.text")); // NOI18N
        textFieldBMV.setEnabled(false);
        textFieldBMV.setMaximumSize(new java.awt.Dimension(6, 20));
        textFieldBMV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldBMVActionPerformed(evt);
            }
        });

        textFieldCH.setText(org.openide.util.NbBundle.getMessage(ImageFingerprintGlobalSettingsPanel.class, "ImageFingerprintGlobalSettingsPanel.textFieldCH.text")); // NOI18N
        textFieldCH.setEnabled(false);
        textFieldCH.setMaximumSize(new java.awt.Dimension(6, 20));

        org.openide.awt.Mnemonics.setLocalizedText(buttonAdd, org.openide.util.NbBundle.getMessage(ImageFingerprintGlobalSettingsPanel.class, "ImageFingerprintGlobalSettingsPanel.buttonAdd.text")); // NOI18N
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(buttonRemove, org.openide.util.NbBundle.getMessage(ImageFingerprintGlobalSettingsPanel.class, "ImageFingerprintGlobalSettingsPanel.buttonRemove.text")); // NOI18N
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });

        labelFingerprintComparisonDatabase.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(labelFingerprintComparisonDatabase, org.openide.util.NbBundle.getMessage(ImageFingerprintGlobalSettingsPanel.class, "ImageFingerprintGlobalSettingsPanel.labelFingerprintComparisonDatabase.text")); // NOI18N

        panelPreviewImage.setMaximumSize(new java.awt.Dimension(174, 174));
        panelPreviewImage.setMinimumSize(new java.awt.Dimension(174, 174));
        panelPreviewImage.setPreferredSize(new java.awt.Dimension(174, 174));

        org.openide.awt.Mnemonics.setLocalizedText(labelPreviewImage, org.openide.util.NbBundle.getMessage(ImageFingerprintGlobalSettingsPanel.class, "ImageFingerprintGlobalSettingsPanel.labelPreviewImage.text")); // NOI18N

        javax.swing.GroupLayout panelPreviewImageLayout = new javax.swing.GroupLayout(panelPreviewImage);
        panelPreviewImage.setLayout(panelPreviewImageLayout);
        panelPreviewImageLayout.setHorizontalGroup(
            panelPreviewImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelPreviewImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelPreviewImageLayout.setVerticalGroup(
            panelPreviewImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelPreviewImage, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
        );

        addProgressbar.setStringPainted(true);

        listboxImages.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        scrollPaneList.setViewportView(listboxImages);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                        .addComponent(buttonRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(addProgressbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelFingerprintComparisonDatabase)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(scrollPaneList))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelPreviewImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelBMV)
                            .addComponent(labelCH)
                            .addComponent(labelMHO))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldMHO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textFieldCH, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                            .addComponent(textFieldBMV, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)))
                    .addComponent(labelImageName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelFingerprintComparisonDatabase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelPreviewImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(labelImageName, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelMHO)
                                    .addComponent(textFieldMHO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelBMV)
                                    .addComponent(textFieldBMV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(scrollPaneList, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addProgressbar, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldCH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelCH)
                            .addComponent(buttonRemove)
                            .addComponent(buttonAdd))
                        .addContainerGap())))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        
        int result = fc.showOpenDialog(this);
        
        
        if(result == JFileChooser.APPROVE_OPTION) {
            if(fc.getSelectedFiles().length > 0) {
               
               this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
               this.buttonAdd.setEnabled(false);
               this.buttonRemove.setEnabled(false);
               
               File[] files = fc.getSelectedFiles();
               addProgressbar.setMinimum(0);
               addProgressbar.setMaximum(files.length);
               addProgressbar.setValue(0);
               taskCreatingFingerprints = new TaskCreatingFingerprints();
               taskCreatingFingerprints.setLogger(logger);
               taskCreatingFingerprints.setFingerprintControler(ctrl);
               taskCreatingFingerprints.setFilesToWorkOn(files);
               taskCreatingFingerprints.addPropertyChangeListener(this);
               taskCreatingFingerprints.execute();
            }
        }
    }//GEN-LAST:event_buttonAddActionPerformed

    private void textFieldBMVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldBMVActionPerformed

    }//GEN-LAST:event_textFieldBMVActionPerformed

    private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveActionPerformed
        String nameToRemove = listboxImages.getSelectedValue();
        Iterator<FileWithFingerprints> itToBeStoredList = listToBeStored.iterator();
        
        boolean done = false;
        while(itToBeStoredList.hasNext() && !done) {
            FileWithFingerprints fwf = itToBeStoredList.next();
            if(fwf.getFile().getName().equals(nameToRemove)) {
                itToBeStoredList.remove();
                done = true;
            }
        }
        
        if(!done) {
            listToBeRemoved.add(nameToRemove);
        }

        Iterator<ListItemImage> itCurrentList = currentList.iterator();
        while(itCurrentList.hasNext()) {
            ListItemImage lii = itCurrentList.next();
            if(lii.getName().equals(nameToRemove)) {
                itCurrentList.remove();
                break;
            }
        }

        updateListBox();
    }//GEN-LAST:event_buttonRemoveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar addProgressbar;
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel labelBMV;
    private javax.swing.JLabel labelCH;
    private javax.swing.JLabel labelFingerprintComparisonDatabase;
    private javax.swing.JLabel labelImageName;
    private javax.swing.JLabel labelMHO;
    private javax.swing.JLabel labelPreviewImage;
    private javax.swing.JList<String> listboxImages;
    private javax.swing.JPanel panelPreviewImage;
    private javax.swing.JScrollPane scrollPaneList;
    private javax.swing.JTextField textFieldBMV;
    private javax.swing.JTextField textFieldCH;
    private javax.swing.JTextField textFieldMHO;
    // End of variables declaration//GEN-END:variables
}
