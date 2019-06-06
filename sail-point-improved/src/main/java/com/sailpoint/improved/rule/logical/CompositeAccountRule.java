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
 * Used to generate logical application account links. This is an alternative to
 * specifying a correlation strategy on the logical application’s tiers. If the rule exists, it will be used in place of the
 * tier definition correlation; if it does not exist, the Identity’s logical application accounts are determined by
 * matching the Identity to the tiers using the IdentitySelector object defined on the Tier. Given an Identity, the
 * rule determines if the Identity should have an account on the logical application, and if so, creates and returns a
 * Link object or list of Links.
 * <p>
 * Output:
 * Rule returns a list of one or more Links objects that will be connected to the Identity
 */
@Slf4j
public abstract class CompositeAccountRule
        extends AbstractJavaRuleExecutor<List<Link>, CompositeAccountRule.CompositeAccountRuleArguments> {

    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";
    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CompositeAccountRule.ARG_IDENTITY,
            CompositeAccountRule.ARG_APPLICATION
    );

    /**
     * Default constructor
     */
    public CompositeAccountRule() {
        super(Rule.Type.CompositeAccount.name(), CompositeAccountRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CompositeAccountRule.CompositeAccountRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CompositeAccountRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CompositeAccountRule.ARG_IDENTITY))
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CompositeAccountRule.ARG_APPLICATION))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     * - application
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CompositeAccountRuleArguments {
        /**
         * Reference to the Identity to evaluate and determine whether or not it should have an account (link) on the logical application
         */
        @Argument(name = CompositeAccountRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Reference to the application object representing the logical application
         */
        @Argument(name = CompositeAccountRule.ARG_APPLICATION)
        private final Application application;
    }
}
