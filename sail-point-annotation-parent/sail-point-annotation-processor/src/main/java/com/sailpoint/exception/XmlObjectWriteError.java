package com.sailpoint.exception;

import java.text.MessageFormat;

/**
 * Exception of writing to xml
 */
public class XmlObjectWriteError extends AnnotationProcessorException {

    /**
     * Error message of writing xml to file. Parameters:
     * 0 - object type
     * 1 - object name
     */
    private static final String OBJECT_XML_WRITE_ERROR = "{0}:[{1}] can not be written";

    /**
     * Constructor with parameters:
     *
     * @param objectType - failed object type
     * @param objectName - failed object name
     * @param cause      - real exception
     */
    public XmlObjectWriteError(String objectType, String objectName, Throwable cause) {
        super(MessageFormat.format(OBJECT_XML_WRITE_ERROR, objectType, objectName), cause);
    }
}
