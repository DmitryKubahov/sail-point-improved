package com.sailpoint.processor;

import com.sailpoint.processor.javadoc.JavaDocsStorageProvider;
import sailpoint.tools.xml.XMLObjectFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import java.util.Optional;

/**
 * Class contains all necessary stuff for all annotations processors
 */
public abstract class AbstractSailPointAnnotationProcessor extends AbstractProcessor {

    /**
     * Sail point object factory
     */
    protected XMLObjectFactory xmlObjectFactory;

    /**
     * Xml generation result path
     */
    protected String xmlPath;

    /**
     * Javadoc storage provider
     */
    protected JavaDocsStorageProvider javaDocsStorageProvider;

    /**
     * Init necessary properties:
     * - xml object factory
     *
     * @param processingEnv - current environment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.xmlObjectFactory = XMLObjectFactory.getInstance();
        this.xmlPath = Optional
                .ofNullable(processingEnv.getOptions().get(SailPointAnnotationProcessorDictionary.GENERATION_PATH))
                .orElse(SailPointAnnotationProcessorDictionary.DEFAULT_PATH_XML_GENERATION);
        this.javaDocsStorageProvider = new JavaDocsStorageProvider(processingEnv);
    }
}
