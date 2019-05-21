package com.sailpoint.improved.rule.provisioning;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Rule is executed immediately before the connector's provisioning method is called. This
 * gives customer the ability to customize or react to anything in the ProvisioningPlan before the requests are sent
 * to the underlying connectors used in provisioning. This rule is not connector-specific; it runs for all applications
 * regardless of connector type.
 * <p>
 * Output:
 * None. The rule should directly update the ProvisioningPlan passed to it.
 */
@Slf4j
public abstract class BeforeProvisioningRule
        extends AbstractJavaRuleExecutor<Object, BeforeProvisioningRule.BeforeProvisioningRuleArguments> {

    /**
     * Name of plan argument name
     */
    public static final String ARG_PLAN_NAME = "plan";
    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION_NAME = "application";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            BeforeProvisioningRule.ARG_PLAN_NAME,
            BeforeProvisioningRule.ARG_APPLICATION_NAME
    );

    /**
     * Default constructor
     */
    public BeforeProvisioningRule() {
        super(Rule.Type.BeforeProvisioning.name(), BeforeProvisioningRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected BeforeProvisioningRule.BeforeProvisioningRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return BeforeProvisioningRuleArguments.builder()
                .plan((ProvisioningPlan) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, BeforeProvisioningRule.ARG_PLAN_NAME))
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, BeforeProvisioningRule.ARG_APPLICATION_NAME))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - plan
     * - application
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class BeforeProvisioningRuleArguments {
        /**
         * Contains provisioning request details
         */
        @Argument(name = BeforeProvisioningRule.ARG_PLAN_NAME)
        private final ProvisioningPlan plan;
        /**
         * Application object containing this rule reference
         */
        @Argument(name = BeforeProvisioningRule.ARG_APPLICATION_NAME)
        private final Application application;
    }
}
