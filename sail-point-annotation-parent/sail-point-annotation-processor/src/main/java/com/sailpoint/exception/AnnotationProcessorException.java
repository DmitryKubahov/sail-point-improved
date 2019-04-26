package com.sailpoint.exception;

/**
 * Common exception for all annotation processors
 */
public class AnnotationProcessorException extends RuntimeException {

    /**
     * Pass only error message
     *
     * @param message - error message
     */
    public AnnotationProcessorException(String message) {
        super(message);
    }

    /**
     * Pass error and main cause
     *
     * @param message - error message
     * @param cause   - main cause
     */
    public AnnotationProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
