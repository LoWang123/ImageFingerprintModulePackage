/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules;


import java.util.logging.Logger;
import org.sleuthkit.autopsy.ingest.*;
import org.openide.util.lookup.ServiceProvider;


/**
 *
 * @author Tobias
 */
@ServiceProvider(service = IngestModuleFactory.class)
public class ImageFingerprintIngestFactory implements IngestModuleFactory{

    private static final String moduleName = "ImageFingerprintIngest";
    private static final String moduleDescription = "Calculates Fingerprints for each image in the datasource, ";
    private static final String moduleVersionNumber = "1.0.0";
    
    
    private static final String artifactTypeStringFingerprint = "ART_FINGERPRINTS";
    private static final String artifactDescriptionFingerprint = "Fingerprints";
    
    private static final String attributeTypeStringMHO = "ATTR_FINGERPRINT_MHO";
    private static final String attributeDescriptionMHO = "Fingerprint: Marr Hildreth Operator";
    private static final String attributeTypeStringBMV = "ATTR_FINGERPRINT_BMV";
    private static final String attributeDescriptionBMV = "Fingerprint: Block Mean Value";
    private static final String attributeTypeStringCH = "ATTR_FINGERPRINT_CH";
    private static final String attributeDescriptionCH = "Fingerprint: Color Histogram";
    
    private static final String artifactTypeStringSimilarImage = "ART_FINGERPRINT_SIMILARIMAGE";
    private static final String artifactDescriptionSimilarImage = "Fingerprint: Similar Image and Similarities";
    private static final String attributeTypeStringSimilarityImageName = "ATTR_FINGERPRINT_SIMILARITY_IMAGENAME";
    private static final String attributeDescriptionSimilarityImageName = "Fingerprint: Similarity Image Name";
    private static final String attributeTypeStringSimilarImage = "ATTR_FINGERPRINT_SIMILARIMAGE";
    private static final String attributeDescriptionSimilarImage = "Fingerprint: Similar Image";
    private static final String attributeTypeStringSimilarityMHO = "ATTR_FINGERPRINT_SIMILARITY_MHO";
    private static final String attributeDescriptionSimilarityMHO = "Fingerprint: Similarity Marr Hildreth Operator";
    private static final String attributeTypeStringSimilarityBMV = "ATTR_FINGERPRINT_SIMILARITY_BMV";
    private static final String attributeDescriptionSimilarityBMV = "Fingerprint: Similarity Block Mean Value";
    private static final String attributeTypeStringSimilarityCH = "ATTR_FINGERPRINT_SIMILARITY_CH";
    private static final String attributeDescriptionSimilarityCH = "Fingerprint: Similarity Color Histogram";
    
    static String getModuleName() {
        return moduleName;
    }

    /**
     * Gets the display name that identifies the family of ingest modules the
     * factory creates. Autopsy uses this string to identify the module in user
     * interface components and log messages. The module name must be unique. so
     * a brief but distinctive name is recommended.
     *
     * @return The module family display name.
     */
    @Override
    public String getModuleDisplayName() {
        return moduleName;
    }

    /**
     * Gets a brief, user-friendly description of the family of ingest modules
     * the factory creates. Autopsy uses this string to describe the module in
     * user interface components.
     *
     * ToDo: Add localization, I don't speak japanese.
     * 
     * @return The module family description.
     */
    @Override
    public String getModuleDescription() {
        // return NbBundle.getMessage(SampleIngestModuleFactory.class, "SampleIngestModuleFactory.moduleDescription");
        return moduleDescription;
    }

    /**
     * Gets the version number of the family of ingest modules the factory
     * creates.
     *
     * @return The module family version number.
     */
    @Override
    public String getModuleVersionNumber() {
        return moduleVersionNumber;
    }

    /**
     * ToDo: this module will get some kind of settings.
     *
     * @return True if the factory provides a global settings panel.
     */
    @Override
    public boolean hasGlobalSettingsPanel() {
        return true;
    }

    /**
     * ToDo: this module will get some kind of settings.
     * 
     * @return nothing for now.
     */
    @Override
    public IngestModuleGlobalSettingsPanel getGlobalSettingsPanel() {
        return new ImageFingerprintGlobalSettingsPanel();
    }

    /**
     * ToDo: this module will get some kind of settings.
     * 
     * @return nothing for now,
     */
    @Override
    public IngestModuleIngestJobSettings getDefaultIngestJobSettings() {
        ImageFingerprintJobSettings settings = new ImageFingerprintJobSettings();
        return settings;
    }

    /**
     * ToDo: this module will get some kind of settings.
     *
     * @return True if the factory provides ingest job settings panels.
     */
    @Override
    public boolean hasIngestJobSettingsPanel() {
        return true;
    }

    /**
     * ToDo: this module will get some kind of settings.
     * 
     * @param setting Per ingest job settings to initialize the panel.
     *
     * @return nothing for now.
     */
    @Override
    public IngestModuleIngestJobSettingsPanel getIngestJobSettingsPanel(IngestModuleIngestJobSettings settings) {
        if (!(settings instanceof ImageFingerprintJobSettings)) {
            throw new IllegalArgumentException("Expected settings argument to be instanceof ImageFingerprintJobSettings");
        }
        return new ImageFingerprintJobSettingsPanel((ImageFingerprintJobSettings) settings);
    }

    /**
     * Queries the factory to determine if it is capable of creating data source
     * ingest modules. If the module family does not include data source ingest
     * modules, the factory may extend IngestModuleFactoryAdapter to get an
     * implementation of this method that returns false.
     *
     * @return True if the factory can create data source ingest modules.
     */
    @Override
    public boolean isDataSourceIngestModuleFactory() {
        return false;
    }

    /**
     * Creates a data source ingest module instance.
     * <p>
     * Autopsy will generally use the factory to several instances of each type
     * of module for each ingest job it performs. Completing an ingest job
     * entails processing a single data source (e.g., a disk image) and all of
     * the files from the data source, including files extracted from archives
     * and any unallocated space (made to look like a series of files). The data
     * source is passed through one or more pipelines of data source ingest
     * modules. The files are passed through one or more pipelines of file
     * ingest modules.
     * <p>
     * The ingest framework may use multiple threads to complete an ingest job,
     * but it is guaranteed that there will be no more than one module instance
     * per thread. However, if the module instances must share resources, the
     * modules are responsible for synchronizing access to the shared resources
     * and doing reference counting as required to release those resources
     * correctly. Also, more than one ingest job may be in progress at any given
     * time. This must also be taken into consideration when sharing resources
     * between module instances. modules.
     * <p>
     * If the module family does not include data source ingest modules, the
     * factory may extend IngestModuleFactoryAdapter to get an implementation of
     * this method that throws an UnsupportedOperationException.
     *
     * @param settings The settings for the ingest job.
     *
     * @return A data source ingest module instance.
     */
    @Override
    public DataSourceIngestModule createDataSourceIngestModule(IngestModuleIngestJobSettings settings) {
        if (!(settings instanceof ImageFingerprintJobSettings)) {
            throw new IllegalArgumentException("Expected settings argument to be instanceof ImageFingerprintJobSettings");
        }
        throw new UnsupportedOperationException();
        //return new SampleDataSourceIngestModule((SampleModuleIngestJobSettings) settings);
    }

    /**
     * Queries the factory to determine if it is capable of creating file ingest
     * modules. If the module family does not include file ingest modules, the
     * factory may extend IngestModuleFactoryAdapter to get an implementation of
     * this method that returns false.
     *
     * @return True if the factory can create file ingest modules.
     */
    @Override
    public boolean isFileIngestModuleFactory() {
        return true;
    }

    /**
     * Creates a file ingest module instance.
     * <p>
     * Autopsy will generally use the factory to several instances of each type
     * of module for each ingest job it performs. Completing an ingest job
     * entails processing a single data source (e.g., a disk image) and all of
     * the files from the data source, including files extracted from archives
     * and any unallocated space (made to look like a series of files). The data
     * source is passed through one or more pipelines of data source ingest
     * modules. The files are passed through one or more pipelines of file
     * ingest modules.
     * <p>
     * The ingest framework may use multiple threads to complete an ingest job,
     * but it is guaranteed that there will be no more than one module instance
     * per thread. However, if the module instances must share resources, the
     * modules are responsible for synchronizing access to the shared resources
     * and doing reference counting as required to release those resources
     * correctly. Also, more than one ingest job may be in progress at any given
     * time. This must also be taken into consideration when sharing resources
     * between module instances. modules.
     * <p>
     * If the module family does not include file ingest modules, the factory
     * may extend IngestModuleFactoryAdapter to get an implementation of this
     * method that throws an UnsupportedOperationException.
     *
     * @param settings The settings for the ingest job.
     *
     * @return A file ingest module instance.
     */
    @Override
    public FileIngestModule createFileIngestModule(IngestModuleIngestJobSettings settings) {
        if (!(settings instanceof ImageFingerprintJobSettings)) {
            throw new IllegalArgumentException("Expected settings argument to be instanceof ImageFingerprintJobSettings");
        }
        return new ImageFingerprintModule((ImageFingerprintJobSettings) settings);
    }

    public static String getArtifactTypeStringFingerprint() {
        return artifactTypeStringFingerprint;
    }

    public static String getArtifactDescriptionFingerprint() {
        return artifactDescriptionFingerprint;
    }

    public static String getAttributeTypeStringMHO() {
        return attributeTypeStringMHO;
    }

    public static String getAttributeDescriptionMHO() {
        return attributeDescriptionMHO;
    }

    public static String getAttributeTypeStringBMV() {
        return attributeTypeStringBMV;
    }

    public static String getAttributeDescriptionBMV() {
        return attributeDescriptionBMV;
    }

    public static String getAttributeTypeStringCH() {
        return attributeTypeStringCH;
    }

    public static String getAttributeDescriptionCH() {
        return attributeDescriptionCH;
    }

    public static String getAttributeTypeStringSimilarImage() {
        return attributeTypeStringSimilarImage;
    }

    public static String getAttributeDescriptionSimilarImage() {
        return attributeDescriptionSimilarImage;
    }

    public static String getArtifactTypeStringSimilarImage() {
        return artifactTypeStringSimilarImage;
    }

    public static String getArtifactDescriptionSimilarImage() {
        return artifactDescriptionSimilarImage;
    }

    public static String getAttributeTypeStringSimilarityMHO() {
        return attributeTypeStringSimilarityMHO;
    }

    public static String getAttributeDescriptionSimilarityMHO() {
        return attributeDescriptionSimilarityMHO;
    }

    public static String getAttributeTypeStringSimilarityBMV() {
        return attributeTypeStringSimilarityBMV;
    }

    public static String getAttributeDescriptionSimilarityBMV() {
        return attributeDescriptionSimilarityBMV;
    }

    public static String getAttributeTypeStringSimilarityCH() {
        return attributeTypeStringSimilarityCH;
    }

    public static String getAttributeDescriptionSimilarityCH() {
        return attributeDescriptionSimilarityCH;
    }

    public static String getAttributeTypeStringSimilarityImageName() {
        return attributeTypeStringSimilarityImageName;
    }

    public static String getAttributeDescriptionSimilarityImageName() {
        return attributeDescriptionSimilarityImageName;
    }
    
}
