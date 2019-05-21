package com.sailpoint.improved.rule.provisioning;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Identity;
import sailpoint.object.IntegrationConfig;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningResult;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * An Integration rule is the rule type for a plan initializer rule, which contains custom logic that is executed
 * immediately before the provisioning plan is sent to a writeable connector or PIM/SIM to be executed.
 * <p>
 * This rule can be used to economize what data gets passed across to the integration or connector, instead of
 * sending lots of unneeded data (e.g. - loading just the name of the person being remediated or of the requester,
 * instead of passing the entire Identity object to the integration).
 * <p>
 * Output:
 * Result indicating success or failure; failure halts the provisioning action Any other return type (including no return value)
 * allows provisioning processing to continue
 */
@Slf4j
public abstract class IntegrationRule
        extends AbstractJavaRuleExecutor<ProvisioningResult, IntegrationRule.IntegrationRuleArguments> {
    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY_NAME = "identity";
    /**
     * Name of integration argument name
     */
    public static final String ARG_INTEGRATION_NAME = "integration";
    /**
     * Name of plan argument name
     */
    public static final String ARG_PLAN_NAME = "plan";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            IntegrationRule.ARG_IDENTITY_NAME,
            IntegrationRule.ARG_INTEGRATION_NAME,
            IntegrationRule.ARG_PLAN_NAME
    );

    /**
     * Default constructor
     */
    public IntegrationRule() {
        super(Rule.Type.Integration.name(), IntegrationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected IntegrationRule.IntegrationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return IntegrationRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IntegrationRule.ARG_IDENTITY_NAME))
                .integration((IntegrationConfig) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IntegrationRule.ARG_INTEGRATION_NAME))
                .plan((ProvisioningPlan) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IntegrationRule.ARG_PLAN_NAME))

                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     * - integration
     * - plan
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class IntegrationRuleArguments {
        /**
         * Reference to the Identity object for which the provisioning request has been made
         */
        @Argument(name = IntegrationRule.ARG_IDENTITY_NAME)
        private final Identity identity;
        /**
         * Reference to the integrationConfig (or ProvisioningConfig cast as an integrationConfig) that defines provisioning to the application
         */
        @Argument(name = IntegrationRule.ARG_INTEGRATION_NAME)
        private final IntegrationConfig integration;
        /**
         * Reference to the ProvisioningPlan object containing the requested provisioning action
         */
        @Argument(name = IntegrationRule.ARG_PLAN_NAME)
        private final ProvisioningPlan plan;
    }
}
