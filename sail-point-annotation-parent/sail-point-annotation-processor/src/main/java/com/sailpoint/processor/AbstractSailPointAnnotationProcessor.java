package com.sailpoint.processor;

import sailpoint.tools.xml.XMLObjectFactory;

import javax.annotation.processing.AbstractProcessor;

/**
 * Class contains all necessary stuff for all annotations processors
 */
public abstract class AbstractSailPointAnnotationProcessor extends AbstractProcessor {

    /**
     * Sail point object factory
     */
    public static final XMLObjectFactory XML_OBJECT_FACTORY = XMLObjectFactory.getInstance();

}
