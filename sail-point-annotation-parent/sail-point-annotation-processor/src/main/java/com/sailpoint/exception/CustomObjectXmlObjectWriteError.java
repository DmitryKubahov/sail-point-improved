package com.sailpoint.exception;

import com.sailpoint.annotation.Custom;

/**
 * Exception of writing custom object to xml
 */
public class CustomObjectXmlObjectWriteError extends XmlObjectWriteError {

    /**
     * Constructor with parameters:
     *
     * @param customObjectName - failed custom object name
     * @param cause            - real exception
     */
    public CustomObjectXmlObjectWriteError(String customObjectName, Throwable cause) {
        super(Custom.class.getSimpleName(), customObjectName, cause);
    }
}
