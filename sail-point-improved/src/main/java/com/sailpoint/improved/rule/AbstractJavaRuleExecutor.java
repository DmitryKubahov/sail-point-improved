package com.sailpoint.improved.rule;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.JavaRuleExecutor;
import sailpoint.tools.GeneralException;

import java.util.Objects;
import java.util.Optional;

/**
 * Abstract class for java rule executor,
 */
@Slf4j
public abstract class AbstractJavaRuleExecutor implements JavaRuleExecutor {

    /**
     * Common call of java rule executor
     *
     * @param javaRuleContext - current rule context
     * @return rule execution result
     * @throws GeneralException - validation or execution error
     */
    @Override
    public Object execute(JavaRuleContext javaRuleContext) throws GeneralException {
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
    protected abstract Object internalExecute(JavaRuleContext javaRuleContext) throws GeneralException;

    /**
     * Validation rule context:
     * 1 - validate sailpoint context
     * 2 - call internal validation
     *
     * @param javaRuleContext - current rule context
     */
    protected void validate(JavaRuleContext javaRuleContext) throws GeneralException {
        log.debug("Validate sailpoint context: must not be null");
        Objects.requireNonNull(javaRuleContext.getContext(), "SailPoint context in rule is null");
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

    /**
     * Get attribute value by name
     *
     * @param javaRuleContext - java rule context
     * @param attributeName   - attribute name
     * @return attribute value. Can be null.
     */
    protected Object getAttributeByName(@NonNull JavaRuleContext javaRuleContext, @NonNull String attributeName) {
        log.debug("Start getting attribute:[{}] value from java rule context", attributeName);
        return Optional.ofNullable(javaRuleContext.getArguments()).map(attribute -> attribute.get(attributeName))
                .orElse(null);
    }
}
