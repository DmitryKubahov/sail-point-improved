package com.sailpoint.improved.rule.logical;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Correlates tier accounts to the primary application account for a given
 * Identity. This rule is only specified when simple attribute matching is insufficient to identify the correct tier
 * account (e.g. when an Identity has multiple accounts on a tier application and only a subset of them should be
 * correlated to the tier as part of the logical application.
 * <p>
 * Output:
 * One or more links on the tier application that correlate to the tier
 */
@Slf4j
public abstract class CompositeTierCorrelationRule
        extends AbstractJavaRuleExecutor<List<Link>, CompositeTierCorrelationRule.CompositeTierCorrelationRuleArguments> {

    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";
    /**
     * Name of tierApplication argument name
     */
    public static final String ARG_TIER_APPLICATION = "tierApplication";
    /**
     * Name of primaryLink argument name
     */
    public static final String ARG_PRIMARY_LINK = "primaryLink";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CompositeTierCorrelationRule.ARG_IDENTITY,
            CompositeTierCorrelationRule.ARG_TIER_APPLICATION,
            CompositeTierCorrelationRule.ARG_PRIMARY_LINK
    );

    /**
     * Default constructor
     */
    public CompositeTierCorrelationRule() {
        super(Rule.Type.CompositeTierCorrelation.name(), CompositeTierCorrelationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CompositeTierCorrelationRule.CompositeTierCorrelationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CompositeTierCorrelationRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CompositeTierCorrelationRule.ARG_IDENTITY))
                .tierApplication((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CompositeTierCorrelationRule.ARG_TIER_APPLICATION))
                .primaryLink((Link) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CompositeTierCorrelationRule.ARG_PRIMARY_LINK))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     * - tier application
     * - primary link
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CompositeTierCorrelationRuleArguments {
        /**
         * Reference to the Identity object for which the tier correlation is being done
         */
        @Argument(name = CompositeTierCorrelationRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Reference to the application object represented by the tier being correlated
         */
        @Argument(name = CompositeTierCorrelationRule.ARG_TIER_APPLICATION)
        private final Application tierApplication;
        /**
         * Reference to the link object (account) held by the Identity on the primary application
         */
        @Argument(name = CompositeTierCorrelationRule.ARG_PRIMARY_LINK)
        private final Link primaryLink;
    }
}
