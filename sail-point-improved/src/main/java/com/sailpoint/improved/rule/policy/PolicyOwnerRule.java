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
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Used to determine the owner of a Policy Violation. Policy violation owners can be set
 * for the whole policy or for individual rules, or constraints, defined within the policy.
 * <p>
 * Output:
 * The identity to which ownership of the violation (and therefore responsibility for addressing it) should be assigned
 */
@Slf4j
public abstract class PolicyOwnerRule
        extends AbstractJavaRuleExecutor<Identity, PolicyOwnerRule.PolicyOwnerRuleArguments> {

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
            PolicyOwnerRule.ARG_IDENTITY,
            PolicyOwnerRule.ARG_POLICY,
            PolicyOwnerRule.ARG_CONSTRAINT
    );

    /**
     * Default constructor
     */
    public PolicyOwnerRule() {
        super(Rule.Type.PolicyOwner.name(), PolicyOwnerRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected PolicyOwnerRule.PolicyOwnerRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return PolicyOwnerRule.PolicyOwnerRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PolicyOwnerRule.ARG_IDENTITY))
                .policy((Policy) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PolicyOwnerRule.ARG_POLICY))
                .constraint((BaseConstraint) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PolicyOwnerRule.ARG_CONSTRAINT))
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
    public static class PolicyOwnerRuleArguments {
        /**
         * Reference to the identity to whom the violation relates (the policy violating identity)
         */
        @Argument(name = PolicyOwnerRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Reference to the policy to which the violation relates
         */
        @Argument(name = PolicyOwnerRule.ARG_POLICY)
        private final Policy policy;
        /**
         * Reference to the policy constraint that the Identity has violated; only passed when assigning a violation owner
         * per each specific constraint – this argument is null when the violation owner is set at the whole policy level
         */
        @Argument(name = PolicyOwnerRule.ARG_CONSTRAINT)
        private final BaseConstraint constraint;
    }
}
