package com.sailpoint.processor;

/**
 * Common dictionary for all sail point annotation processors
 */
public class SailPointAnnotationProcessorDictionary {

    /**
     * XML file patters. Parameters:
     * 0 - file name
     */
    public static final String XML_FILE_PATTERN = "{0}.xml";

    /**
     * Option name to override default path for generating xml
     */
    public static final String GENERATION_PATH = "generationPath";
    /**
     * Option name for skipping getInstance static function
     */
    public static final String CHECK_GET_INSTANCE_SKIP = "checkGetInstanceSkip";

    /**
     * Default path for generating xml rules. Can be override with {@link SailPointAnnotationProcessorDictionary#GENERATION_PATH}
     */
    public static final String DEFAULT_PATH_XML_GENERATION = "target/xml/";

    /**
     * Only dictionary stuff
     */
    private SailPointAnnotationProcessorDictionary() {
    }
}
