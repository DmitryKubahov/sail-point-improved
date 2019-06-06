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
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Called when provisioning needs to be performed against logical accounts. It is
 * passed the provisioning plan built by the plan compiler and alters that plan so the request is directed at the
 * component applications, rather than the logical application. The rule is meant to build a separate modified
 * provisioning plan and return that plan. If the rule returns null, IdentityIQ uses the plan passed to the rule in
 * subsequent processing, so the rule author can choose to have the rule directly modify the plan passed to it
 * instead.
 * <p>
 * Output:
 * Converted provisioning plan that targets the applications that make up the logical application.
 */
@Slf4j
public abstract class CompositeRemediationRule
        extends AbstractJavaRuleExecutor<ProvisioningPlan, CompositeRemediationRule.CompositeRemediationRuleArguments> {

    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";
    /**
     * Name of plan argument name
     */
    public static final String ARG_PLAN = "plan";
    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CompositeRemediationRule.ARG_IDENTITY,
            CompositeRemediationRule.ARG_PLAN,
            CompositeRemediationRule.ARG_APPLICATION
    );

    /**
     * Default constructor
     */
    public CompositeRemediationRule() {
        super(Rule.Type.CompositeRemediation.name(), CompositeRemediationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CompositeRemediationRule.CompositeRemediationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CompositeRemediationRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CompositeRemediationRule.ARG_IDENTITY))
                .plan((ProvisioningPlan) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CompositeRemediationRule.ARG_PLAN))
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CompositeRemediationRule.ARG_APPLICATION))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     * - plan
     * - application
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CompositeRemediationRuleArguments {
        /**
         * Reference to the Identity object for whom the provisioning request has been made
         */
        @Argument(name = CompositeRemediationRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Reference to a provisioning plan against the logical application
         */
        @Argument(name = CompositeRemediationRule.ARG_PLAN)
        private final ProvisioningPlan plan;
        /**
         * Reference to the (logical) application object on which the rule is defined
         */
        @Argument(name = CompositeRemediationRule.ARG_APPLICATION)
        private final Application application;
    }
}
