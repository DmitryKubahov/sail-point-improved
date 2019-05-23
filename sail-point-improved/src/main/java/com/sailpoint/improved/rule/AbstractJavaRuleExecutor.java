package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.JavaRuleExecutor;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

import java.util.List;
import java.util.Objects;

/**
 * Abstract class for java rule executor,
 */
@Slf4j
@Getter
public abstract class AbstractJavaRuleExecutor<T extends Object, C> implements JavaRuleExecutor {

    /**
     * Context value validation error message
     */
    public static final String VALIDATION_CONTEXT_ERROR_MESSAGE = "SailPoint context is null";

    /**
     * Current rule type
     */
    private final String ruleType;
    /**
     * List of none-null check attributes
     */
    private final List<String> noneNullArguments;

    /**
     * Default constructor for all rules
     *
     * @param ruleType          - current rule type value
     * @param noneNullArguments - list of arguments for none null checking
     */
    protected AbstractJavaRuleExecutor(String ruleType, List<String> noneNullArguments) {
        this.ruleType = ruleType;
        this.noneNullArguments = noneNullArguments;
    }

    /**
     * Common call of java rule executor
     *
     * @param javaRuleContext - current rule context
     * @return rule execution result
     * @throws GeneralException - validation or execution error
     */
    @Override
    public T execute(JavaRuleContext javaRuleContext) throws GeneralException {
        log.debug("Rule:[{}], stage: validate rule context", ruleType);
        validate(javaRuleContext);
        log.debug("Rule:[{}], validation rule context passed", ruleType);

        log.debug("Rule:[{}], stage: validate rule context arguments", ruleType);
        validateArguments(javaRuleContext);
        log.debug("Rule:[{}], validation rule context arguments passed", ruleType);

        log.trace("Rule:[{}], raw parameters:[{}]", ruleType, javaRuleContext.getArguments());
        log.debug("Rule:[{}], stage: start rule arguments container building", ruleType);
        C containerArguments = buildContainerArguments(javaRuleContext);
        log.trace("Rule:[{}], arguments container:[{}]", ruleType, containerArguments);

        log.trace("Rule:[{}], stage: execute rule", ruleType);

        return internalExecute(javaRuleContext, containerArguments);
    }

    /**
     * Build container arguments for current rule
     *
     * @param javaRuleContext - current rule context
     * @return container arguments instance
     */
    protected abstract C buildContainerArguments(JavaRuleContext javaRuleContext);

    /**
     * Internal execution of java rule
     *
     * @param javaRuleContext    - java rule context
     * @param containerArguments - argument container for current rule
     * @return rule execution result
     * @throws GeneralException - execution error
     */
    protected abstract T internalExecute(JavaRuleContext javaRuleContext, C containerArguments)
            throws GeneralException;

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
     * Validation rule context arguments. Runs none-null checks for all {@link AbstractJavaRuleExecutor#noneNullArguments) values
     *
     * @param javaRuleContext - current rule context
     */
    protected void validateArguments(JavaRuleContext javaRuleContext) throws GeneralException {
        log.debug("Rule:[{}], none-null arguments:[{}]", ruleType, noneNullArguments);
        if (!Util.isEmpty(noneNullArguments)) {
            for (String noneNullArgument : noneNullArguments) {
                log.debug("Rule:[{}], stage: none-null check for argument:[{}]", ruleType, noneNullArgument);
                JavaRuleExecutorUtil.notNullArgumentValidation(javaRuleContext, noneNullArgument);
            }
        }
        log.debug("Rule:[{}], stage: internal arguments check", ruleType);
        internalValidateArguments(javaRuleContext);
    }

    /**
     * Internal validation.
     * Default: empty implementation
     *
     * @param javaRuleContext - rule context to validate
     * @throws GeneralException - validation error
     */
    protected void internalValidation(JavaRuleContext javaRuleContext) throws GeneralException {
        log.trace("Call default (empty) implementation of validation rule context");
    }

    /**
     * Internal arguments validation.
     * Default: empty implementation.
     *
     * @param javaRuleContext - rule context to validate
     * @throws GeneralException - arguments validation error
     */
    protected void internalValidateArguments(JavaRuleContext javaRuleContext) throws GeneralException {
        log.trace("Call default (empty) implementation of arguments validation");
    }
}
