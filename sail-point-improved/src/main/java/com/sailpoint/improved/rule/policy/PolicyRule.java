package com.sailpoint.improved.rule.policy;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.BaseConstraint;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Policy;
import sailpoint.object.PolicyViolation;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * The Policy rules (or constraints) for Advanced policies can be defined through a Policy rule. The rule specifies
 * the conditions for determining when the policy has been violated.
 * <p>
 * NOTE: This is actually a special case of an IdentitySelector rule that is provided more arguments (the policy and
 * constraint) than a normal IdentitySelector rule and can return a full PolicyViolation object, rather than just a
 * “true” or “false” value. By returning a PolicyViolation, the rule can specify more details about the appearance
 * and structure of the violation, but this is not strictly required. If the rule returns a PolicyViolation, that violation
 * will be added for the Identity as returned. If the rule returns a “true” value, a PolicyViolation will be created
 * using the information available on the policy itself.
 * <p>
 * Output:
 * PolicyViolation object if Identity is in violation of the policy; null if no violation is detected
 */
@Slf4j
public abstract class PolicyRule
        extends AbstractJavaRuleExecutor<PolicyViolation, PolicyRule.PolicyRuleArguments> {

    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";
    /**
     * Name of policy argument name
     */
    public static final String ARG_POLICY = "policy";
    /**
     * Name of constraint argument name
     */
    public static final String ARG_CONSTRAINT = "constraint";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            PolicyRule.ARG_IDENTITY,
            PolicyRule.ARG_POLICY,
            PolicyRule.ARG_CONSTRAINT
    );

    /**
     * Default constructor
     */
    public PolicyRule() {
        super(Rule.Type.Policy.name(), PolicyRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected PolicyRule.PolicyRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return PolicyRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PolicyRule.ARG_IDENTITY))
                .policy((Policy) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PolicyRule.ARG_POLICY))
                .constraint((BaseConstraint) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PolicyRule.ARG_CONSTRAINT))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     * - policy
     * - constraint
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class PolicyRuleArguments {
        /**
         * Reference to the Identity object being inspected
         */
        @Argument(name = PolicyRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Reference to the policy object
         */
        @Argument(name = PolicyRule.ARG_POLICY)
        private final Policy policy;
        /**
         * Reference to the Constraint object that defines the policy rule
         */
        @Argument(name = PolicyRule.ARG_CONSTRAINT)
        private final BaseConstraint constraint;

    }
}
