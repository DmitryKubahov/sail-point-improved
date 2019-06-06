package com.sailpoint.improved.rule.policy;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Attributes;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Policy;
import sailpoint.object.PolicyViolation;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Used to specify additional people who should be notified when a policy violation is
 * discovered. This is expressed in the policy definition as a PolicyAlert Owner rule, and the rule type
 * PolicyNotification is never referenced explicitly by IdentityIQ.
 * <p>
 * Output:
 * Specify the user or users who should be notified of the violation
 */
@Slf4j
public abstract class PolicyNotificationRule
        extends AbstractJavaRuleExecutor<List<Identity>, PolicyNotificationRule.PolicyNotificationRuleArguments> {

    /**
     * Name of environment argument name
     */
    public static final String ARG_ENVIRONMENT = "environment";
    /**
     * Name of policy argument name
     */
    public static final String ARG_POLICY = "policy";
    /**
     * Name of violation argument name
     */
    public static final String ARG_VIOLATION = "violation";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            PolicyNotificationRule.ARG_ENVIRONMENT,
            PolicyNotificationRule.ARG_POLICY,
            PolicyNotificationRule.ARG_VIOLATION
    );

    /**
     * Default constructor
     */
    public PolicyNotificationRule() {
        super(Rule.Type.PolicyNotification.name(), PolicyNotificationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected PolicyNotificationRule.PolicyNotificationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return PolicyNotificationRuleArguments.builder()
                .environment((Attributes) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PolicyNotificationRule.ARG_ENVIRONMENT))
                .policy((Policy) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PolicyNotificationRule.ARG_POLICY))
                .violation((PolicyViolation) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PolicyNotificationRule.ARG_VIOLATION))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - environment
     * - policy
     * - violation
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class PolicyNotificationRuleArguments {
        /**
         * Map of arguments passed to the policy checking process
         */
        @Argument(name = PolicyNotificationRule.ARG_ENVIRONMENT)
        private final Attributes environment;
        /**
         * Reference to the policy to which the violation relates
         */
        @Argument(name = PolicyNotificationRule.ARG_POLICY)
        private final Policy policy;
        /**
         * Reference to the policy violation created based on this policy analysis
         */
        @Argument(name = PolicyNotificationRule.ARG_VIOLATION)
        private final PolicyViolation violation;
    }
}
