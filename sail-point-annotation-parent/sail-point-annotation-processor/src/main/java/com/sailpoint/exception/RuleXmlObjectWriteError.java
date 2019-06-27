package com.sailpoint.exception;

import com.sailpoint.annotation.Rule;

/**
 * Exception of writing rule to xml
 */
public class RuleXmlObjectWriteError extends XmlObjectWriteError {

    /**
     * Constructor with parameters:
     *
     * @param ruleName - failed rule name
     * @param cause    - real exception
     */
    public RuleXmlObjectWriteError(String ruleName, Throwable cause) {
        super(Rule.class.getSimpleName(), ruleName, cause);
    }
}
