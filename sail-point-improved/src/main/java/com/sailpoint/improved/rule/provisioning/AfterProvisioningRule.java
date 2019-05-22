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
import sailpoint.object.ProvisioningResult;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * An application’s AfterProvisioning rule is executed immediately after the connector's provisioning method is
 * called, but only if the provisioning result is in a committed or queued state. This gives customers the ability to
 * customize or react to anything in the ProvisioningPlan that has been sent out to specific applications after the
 * provisioning request has been processed. This rule is not connector-specific; it runs for all applications
 * regardless of connector type.
 * <p>
 * Output:
 * Outputs: none; the rule’s actions are outside the direct provisioning process so no return value is expected or used
 */
@Slf4j
public abstract class AfterProvisioningRule
        extends AbstractJavaRuleExecutor<Object, AfterProvisioningRule.AfterProvisioningRuleArguments> {

    /**
     * Name of plan argument name
     */
    public static final String ARG_PLAN = "plan";
    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of result argument name
     */
    public static final String ARG_RESULT = "result";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            AfterProvisioningRule.ARG_PLAN,
            AfterProvisioningRule.ARG_APPLICATION,
            AfterProvisioningRule.ARG_RESULT
    );

    /**
     * Default constructor
     */
    public AfterProvisioningRule() {
        super(Rule.Type.AfterProvisioning.name(), AfterProvisioningRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected AfterProvisioningRule.AfterProvisioningRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return AfterProvisioningRuleArguments.builder()
                .plan((ProvisioningPlan) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AfterProvisioningRule.ARG_PLAN))
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AfterProvisioningRule.ARG_APPLICATION))
                .result((ProvisioningResult) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AfterProvisioningRule.ARG_RESULT))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - plan
     * - application
     * - result
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class AfterProvisioningRuleArguments {
        /**
         * Contains provisioning request details
         */
        @Argument(name = AfterProvisioningRule.ARG_PLAN)
        private final ProvisioningPlan plan;
        /**
         * Application object containing this rule reference
         */
        @Argument(name = AfterProvisioningRule.ARG_APPLICATION)
        private final Application application;
        /**
         * Contains provisioning request result
         */
        @Argument(name = AfterProvisioningRule.ARG_RESULT)
        private final ProvisioningResult result;
    }
}
