/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules;


import de.fau.imagefingerprint.database.AlgorithmWithFingerprint;
import de.fau.imagefingerprint.database.IWFWithSimilarity;
import de.fau.imagefingerprint.database.ImageWithFingerprint;
import de.fau.imagefingerprint.database.Similarity;
import de.fau.imagefingerprint.fingerprint.FingerprintAlgorithms;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.ingest.IngestJobContext;
import org.sleuthkit.autopsy.ingest.FileIngestModule;
import org.sleuthkit.autopsy.ingest.IngestMessage;
import org.sleuthkit.autopsy.ingest.IngestModule;
import org.sleuthkit.autopsy.ingest.IngestModuleReferenceCounter;
import org.sleuthkit.autopsy.ingest.IngestServices;
import org.sleuthkit.autopsy.ingest.ModuleDataEvent;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.SleuthkitCase;
import org.sleuthkit.datamodel.TskCoreException;
import org.sleuthkit.datamodel.TskData;

/**
 *
 * @author Tobias
 */
public class ImageFingerprintModule implements FileIngestModule  {
    //TODO 
        /*
    private static final IngestModuleReferenceCounter refCounter = new IngestModuleReferenceCounter();
    private static final HashMap<Long, Long> artifactCountsForIngestJobs = new HashMap<>();
    
    private FileManager fileManager;
    
    private IngestJobContext context = null;
    private ArrayList<ImageWithFingerprint> imagesToBeAdded = new ArrayList<>();
    
    
    private SimilarityFinder similarityFinder = null;
    
    private Logger logger = null;
    */
    private static int attributeIdMHO = -1;
    private static int attributeIdBMV = -1;
    private static int attributeIdCH = -1;
    
    private static int artifactIdSimilarImage = -1;
    private static int attributeIdSimilarImageName = -1;
    private static int attributeIdSimilarImage = -1;
    private static int attributeIdSimilarityMHO = -1;
    private static int attributeIdSimilarityBMV = -1;
    private static int attributeIdSimilarityCH = -1;
   
    private static final HashMap<Long, Long> artifactCountsForIngestJobs = new HashMap<>();
    private IngestJobContext context = null;
    private static final IngestModuleReferenceCounter refCounter = new IngestModuleReferenceCounter();
    
    private boolean isCompareCH = true;
    private boolean isCompareMHO = true;
    private boolean isCompareBMV = true;
    private boolean isCompareToDatabaseImages = true;
    private float similarityThreshold = 0.2f;
    private int numberOfSuccessesRequired = 1;
    
    private FingerprintControler ctrl; 

    
    ImageFingerprintModule(ImageFingerprintJobSettings settings) {
        this.isCompareCH = settings.compareCH();
        this.isCompareBMV = settings.compareBMV();
        this.isCompareMHO = settings.compareMHO();
        this.isCompareToDatabaseImages = settings.compareToDatabaseImages();
        this.similarityThreshold = settings.getThreshold();
        this.numberOfSuccessesRequired = numberOfSuccessesRequired;

        ctrl = new FingerprintControler();
        ctrl.setSimilarityThreshold(similarityThreshold);
    }

    @Override
    public void startUp(IngestJobContext context) throws IngestModuleException {

        this.context = context;
        refCounter.incrementAndGet(context.getJobId());

        synchronized (ImageFingerprintModule.class) {
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
            } catch (TskCoreException ex) {
                IngestServices ingestServices = IngestServices.getInstance();
                Logger logger = ingestServices.getLogger(ImageFingerprintIngestFactory.getModuleName());
                logger.log(Level.SEVERE, "Failed to create blackboard attribute", ex);
                attributeIdMHO = -1;
                attributeIdBMV = -1;
                attributeIdCH = -1;
                throw new IngestModuleException(ex.getLocalizedMessage());
            }
        }
    }

    
    
    @Override
    public ProcessResult process(AbstractFile file) {
        
        if(!isBlackboardInitialized()) {
            return ProcessResult.ERROR;
        }
        
        if(!isSupportedImage(file)) {
            return ProcessResult.OK;
        }   

        try {

            BlackboardArtifact art;
  
            ArrayList<BlackboardArtifact> artList = file.getArtifacts(BlackboardArtifact.ARTIFACT_TYPE.TSK_INTERESTING_FILE_HIT);
            if(artList.size() > 0) {
                art = artList.get(0);
            }
            else {
                art = file.newArtifact(BlackboardArtifact.ARTIFACT_TYPE.TSK_INTERESTING_FILE_HIT);
            }
            
            BufferedImage image = ImageFileParser.parseAbstractFile(file);
            ImageWithFingerprint iwf = ctrl.calculateFingerprints(image);
            iwf.setFormat(ImageFileParser.findImageType(file));
            iwf.setName(file.getName());
            
            if(isCompareToDatabaseImages) {
                
                SimilarityFinder similarityFinder = new SimilarityFinder(ctrl.getSimilarityThreshold());
                ArrayList<AlgorithmWithFingerprint> fingerprintsToCompare = new ArrayList<>();
                if(isCompareMHO) {
                fingerprintsToCompare.add(
                        new AlgorithmWithFingerprint(
                                iwf.getFingerprint(),
                                FingerprintAlgorithms.MHO
                        ));
                }
                if(isCompareBMV) {
                fingerprintsToCompare.add(
                        new AlgorithmWithFingerprint(
                                iwf.getFingerprintBlockMean(),
                                FingerprintAlgorithms.BMV
                        ));
                }
                if(isCompareCH) {
                fingerprintsToCompare.add(
                        new AlgorithmWithFingerprint(
                                iwf.getFingerprintColorHistogram(),
                                FingerprintAlgorithms.CH
                        ));
                }
            
                similarityFinder.findAllSimilarities(fingerprintsToCompare);
                ArrayList<IWFWithSimilarity> similarImageCandidates = similarityFinder.getIwfWithSimilarities();            
                ArrayList<IWFWithSimilarity> similarImages = new ArrayList<>();
                for(IWFWithSimilarity similarImage: similarImageCandidates) {
                    if(!similarImage.getName().equals(iwf.getName()))
                        similarImages.add(similarImage);
                }
                postSimilarImagesIfItsNotContained(similarImages, file);
            }
            
            postFingerprint(art, iwf.getFingerprint(), attributeIdMHO, false);
            postFingerprint(art, iwf.getFingerprintBlockMean(), attributeIdBMV, false);
            postFingerprint(art, iwf.getFingerprintColorHistogram(), attributeIdCH, false);
            
            // Fire an event to notify any listeners for blackboard postings.
            ModuleDataEvent event = new ModuleDataEvent(ImageFingerprintIngestFactory.getModuleName(), BlackboardArtifact.ARTIFACT_TYPE.TSK_GEN_INFO);
            IngestServices.getInstance().fireModuleDataEvent(event);
            
            return IngestModule.ProcessResult.OK;

        } catch (Exception ex) {
            IngestServices ingestServices = IngestServices.getInstance();
            Logger logger = ingestServices.getLogger(ImageFingerprintIngestFactory.getModuleName());
            logger.log(Level.SEVERE, "Error processing file (id = " + file.getId() + ")", ex);
            return IngestModule.ProcessResult.ERROR;
        }
    }

    /**
     * Add all images that were analysed to the database IF 
     * they are to be added. 
     */
    @Override
    public void shutDown() {

    }

    
    synchronized static boolean isBlackboardInitialized() {
            if (attributeIdMHO == -1 || attributeIdBMV == -1 || attributeIdCH == -1) {
                return false;
            }
            return true;
        }
    
    synchronized static boolean isSupportedImage(AbstractFile file) {
        if ((file.getType() == TskData.TSK_DB_FILES_TYPE_ENUM.UNALLOC_BLOCKS)
                || (file.getType() == TskData.TSK_DB_FILES_TYPE_ENUM.UNUSED_BLOCKS)
                || (file.isFile() == false)) {
            return false;
        }
        if (file.isFile() == false) {
            return false;
        }
        return ImageFileParser.isSupportedImageFile(file);
    }
    
    
    synchronized static void postFingerprint(BlackboardArtifact art, byte[] fingerprint, int attributeID, boolean recalculate)
            throws TskCoreException{
        if(recalculate || !doesArtifactContainAttributeType(art, attributeID)) {
            BlackboardAttribute attrCH = new BlackboardAttribute(attributeID, ImageFingerprintIngestFactory.getModuleName(), fingerprint);
            art.addAttribute(attrCH);
        }
    }   
    
    synchronized static void postSimilarImagesIfItsNotContained(ArrayList<IWFWithSimilarity> similarImages, AbstractFile file) throws TskCoreException, IOException {
        for(IWFWithSimilarity iwfws: similarImages) {
            BlackboardArtifact art = file.newArtifact(artifactIdSimilarImage);
            
            if(!containsThisSimilarityAlready(file, iwfws.getName())) {
                BlackboardAttribute attrImage = new BlackboardAttribute(
                                    attributeIdSimilarImage, 
                                    ImageFingerprintIngestFactory.getModuleName(), 
                                    convertImageToByteArray(iwfws.getImage(), iwfws.getFormat()));
                            art.addAttribute(attrImage);

                BlackboardAttribute attrName = new BlackboardAttribute(
                                    attributeIdSimilarImageName, 
                                    ImageFingerprintIngestFactory.getModuleName(), 
                                    iwfws.getName());
                            art.addAttribute(attrName);

                for(Similarity s : iwfws.getSimilarity()) {
                    switch(s.getAlgorithmUsed()) {
                        case "MHO":
                            BlackboardAttribute attrMHO = new BlackboardAttribute(
                                    attributeIdSimilarityMHO, 
                                    ImageFingerprintIngestFactory.getModuleName(), 
                                    s.getSimilarityRating());
                            art.addAttribute(attrMHO);
                            break;
                        case "BMV":
                            BlackboardAttribute attrBMV = new BlackboardAttribute(
                                    attributeIdSimilarityBMV, 
                                    ImageFingerprintIngestFactory.getModuleName(), 
                                    s.getSimilarityRating());
                            art.addAttribute(attrBMV);
                            break;
                        case "CH":
                            BlackboardAttribute attrCH = new BlackboardAttribute(
                                    attributeIdSimilarityCH, 
                                    ImageFingerprintIngestFactory.getModuleName(), 
                                    s.getSimilarityRating());
                            art.addAttribute(attrCH);
                            break;
                    }
                }
            
            }
        }
    }
    
    private static boolean containsThisSimilarityAlready(AbstractFile file, String imageName) throws TskCoreException{
        for(BlackboardArtifact art: file.getArtifacts(artifactIdSimilarImage)) {
            for(BlackboardAttribute attr: art.getAttributes()) {
                if(attr.getArtifactID() == attributeIdSimilarImageName) {
                    if(attr.getValueString().equals(imageName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private static byte[] convertImageToByteArray(BufferedImage image, String extension)
            throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, extension, baos);
        return baos.toByteArray();
    }
    
    synchronized static boolean doesArtifactContainAttributeType(BlackboardArtifact art, int attributeTypeID)
            throws TskCoreException{
        for(BlackboardAttribute ba: art.getAttributes()) {
            if(ba.getAttributeTypeID() == attributeTypeID)
                return true;
        }
        return false;
    }
    
    synchronized static void reportBlackboardCopiedRegionsCount(String imageName, ArrayList<IWFWithSimilarity> similarImages) {
        String messageString = "";
        
        if(similarImages.size() == 1) {
            messageString = "Found 1 similar image for " + imageName + ": " + similarImages.get(0).getName();
        }
        else {
            String imagesString = "";
            for(IWFWithSimilarity iwf: similarImages) {
                imagesString += iwf.getName() + ", ";
            }
            
            messageString = "Found " + similarImages.size() + " similar images for " + imageName + ": " + imagesString;
        }
        
        IngestMessage message = IngestMessage.createMessage(
                IngestMessage.MessageType.INFO,
                ImageFingerprintIngestFactory.getModuleName(),
                messageString);
        IngestServices.getInstance().postMessage(message);
    }
}