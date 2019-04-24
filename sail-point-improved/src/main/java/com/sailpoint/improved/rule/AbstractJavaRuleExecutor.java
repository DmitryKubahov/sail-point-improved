package com.sailpoint.improved.rule;

import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.JavaRuleExecutor;
import sailpoint.tools.GeneralException;

import java.util.Objects;

/**
 * Abstract class for java rule executor,
 */
@Slf4j
public abstract class AbstractJavaRuleExecutor<T extends Object> implements JavaRuleExecutor {

    /**
     * Context value validation error message
     */
    public static final String VALIDATION_CONTEXT_ERROR_MESSAGE = "SailPoint context is null";

    /**
     * Common call of java rule executor
     *
     * @param javaRuleContext - current rule context
     * @return rule execution result
     * @throws GeneralException - validation or execution error
     */
    @Override
    public T execute(JavaRuleContext javaRuleContext) throws GeneralException {
        log.debug("Validate rule context");
        validate(javaRuleContext);

        log.debug("All validation passed. Execute rule");
        return internalExecute(javaRuleContext);
    }

    /**
     * Internal execution of java rule
     *
     * @param javaRuleContext - context
     * @return rule execution result
     * @throws GeneralException - execution error
     */
    protected abstract T internalExecute(JavaRuleContext javaRuleContext) throws GeneralException;

    /**
     * Validation rule context:
     * 1 - validate sailpoint context
     * 2 - call internal validation
     *
     * @param javaRuleContext - current rule context
     */
    protected void validate(JavaRuleContext javaRuleContext) throws GeneralException {
        log.debug("Validate sailpoint context: must not be null");
        Objects.requireNonNull(javaRuleContext.getContext(), VALIDATION_CONTEXT_ERROR_MESSAGE);
        log.debug("Call internal validation");
        internalValidation(javaRuleContext);
    }

    /**
     * Internal validation empty implementation. Can be overridden in certain rule implementation
     *
     * @param javaRuleContext - rule context to validate
     * @throws GeneralException - validation error
     */
    protected void internalValidation(JavaRuleContext javaRuleContext) throws GeneralException {
        log.trace("Call default (empty) implementation of validation rule context");
    }
}
