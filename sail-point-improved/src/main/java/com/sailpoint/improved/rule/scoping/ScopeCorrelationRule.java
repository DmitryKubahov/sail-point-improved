package com.sailpoint.improved.rule.scoping;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Scope;

import java.util.Arrays;
import java.util.List;

/**
 * Evaluates one or more Identity attributes to select a scope or list of scopes that
 * applies to the Identity. If it returns multiple scopes, the ScopeSelection rule chooses which of the scopes to
 * assign. There is only one ScopeCorrelation rule per IdentityIQ installation.
 * <p>
 * Output:
 * Scopes that meet the rule’s criteria for assignment to the Identity
 */
@Slf4j
public abstract class ScopeCorrelationRule
        extends AbstractJavaRuleExecutor<List<Scope>, ScopeCorrelationRule.ScopeCorrelationRuleArguments> {

    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";
    /**
     * Name of scopeCorrelationAttribute argument name
     */
    public static final String ARG_SCOPE_CORRELATION_ATTRIBUTE = "scopeCorrelationAttribute";
    /**
     * Name of scopeCorrelationAttributeValue argument name
     */
    public static final String ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE = "scopeCorrelationAttributeValue";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ScopeCorrelationRule.ARG_IDENTITY,
            ScopeCorrelationRule.ARG_SCOPE_CORRELATION_ATTRIBUTE
    );

    /**
     * Default constructor
     */
    public ScopeCorrelationRule() {
        super(Rule.Type.ScopeCorrelation.name(), ScopeCorrelationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ScopeCorrelationRule.ScopeCorrelationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ScopeCorrelationRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ScopeCorrelationRule.ARG_IDENTITY))
                .scopeCorrelationAttribute((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                ScopeCorrelationRule.ARG_SCOPE_CORRELATION_ATTRIBUTE))
                .scopeCorrelationAttributeValue((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                ScopeCorrelationRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     * - scopeCorrelationAttribute
     * - scopeCorrelationAttributeValue
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ScopeCorrelationRuleArguments {
        /**
         * Reference to the identity being assigned a scope
         */
        @Argument(name = ScopeCorrelationRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Name of the scope correlation attribute specified in the scoping configuration
         */
        @Argument(name = ScopeCorrelationRule.ARG_SCOPE_CORRELATION_ATTRIBUTE)
        private final String scopeCorrelationAttribute;
        /**
         * The value for the correlation attribute on the Identity
         */
        @Argument(name = ScopeCorrelationRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE)
        private final String scopeCorrelationAttributeValue;
    }
}
