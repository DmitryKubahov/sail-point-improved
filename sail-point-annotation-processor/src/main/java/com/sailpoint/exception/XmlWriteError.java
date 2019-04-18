package com.sailpoint.exception;

import java.text.MessageFormat;

/**
 * Exception of error writing xml of sail point object
 */
public class XmlWriteError extends RuntimeException {

    /**
     * Error message of writing xml rule to file. Parameters:
     * 0 - rule name
     */
    private static final String RULE_XML_WRITE_ERROR = "Rule:[{0}] can not be written";

    /**
     * Constructor with parameters:
     *
     * @param ruleName - failed rule name
     * @param cause    - real exception
     */
    public XmlWriteError(String ruleName, Throwable cause) {
        super(MessageFormat.format(RULE_XML_WRITE_ERROR, ruleName), cause);
    }
}
