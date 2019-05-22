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
 * Runs to select a single scope to assign to an Identity when the scope attribute
 * correlation or scopeCorrelation rule have identified multiple possible scopes for the Identity. There is only one
 * scopeSelection rule per IdentityIQ installation.
 * <p>
 * Output:
 * Scope to be assigned to the Identity
 */
@Slf4j
public abstract class ScopeSelectionRule
        extends AbstractJavaRuleExecutor<Scope, ScopeSelectionRule.ScopeSelectionRuleArguments> {

    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY_NAME = "identity";
    /**
     * Name of scopeCorrelationAttribute argument name
     */
    public static final String ARG_SCOPE_CORRELATION_ATTRIBUTE_NAME = "scopeCorrelationAttribute";
    /**
     * Name of scopeCorrelationAttributeValue argument name
     */
    public static final String ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE_NAME = "scopeCorrelationAttributeValue";
    /**
     * Name of candidateScopes argument name
     */
    public static final String ARG_CANDIDATE_SCOPES_NAME = "candidateScopes";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ScopeSelectionRule.ARG_IDENTITY_NAME,
            ScopeSelectionRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_NAME,
            ScopeSelectionRule.ARG_CANDIDATE_SCOPES_NAME
    );

    /**
     * Default constructor
     */
    public ScopeSelectionRule() {
        super(Rule.Type.ScopeSelection.name(), ScopeSelectionRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ScopeSelectionRule.ScopeSelectionRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ScopeSelectionRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ScopeSelectionRule.ARG_IDENTITY_NAME))
                .scopeCorrelationAttribute((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                ScopeSelectionRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_NAME))
                .scopeCorrelationAttributeValue((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                ScopeSelectionRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE_NAME))
                .candidateScopes((List<Scope>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ScopeSelectionRule.ARG_CANDIDATE_SCOPES_NAME))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     * - scopeCorrelationAttribute
     * - scopeCorrelationAttributeValue
     * -
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ScopeSelectionRuleArguments {
        /**
         * Reference to the identity being assigned a scope
         */
        @Argument(name = ScopeSelectionRule.ARG_IDENTITY_NAME)
        private final Identity identity;
        /**
         * Name of the scope correlation attribute specified in the scoping configuration
         */
        @Argument(name = ScopeSelectionRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_NAME)
        private final String scopeCorrelationAttribute;
        /**
         * The value for the correlation attribute on the Identity
         */
        @Argument(name = ScopeSelectionRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE_NAME)
        private final String scopeCorrelationAttributeValue;
        /**
         * List of scopes identified as candidates for assignment to the Identity; rule should select one of these and return it as the scope to assign
         */
        @Argument(name = ScopeSelectionRule.ARG_CANDIDATE_SCOPES_NAME)
        private final List<Scope> candidateScopes;
    }
}
