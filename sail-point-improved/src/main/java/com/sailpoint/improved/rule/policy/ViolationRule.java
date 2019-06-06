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
import java.util.Map;

/**
 * A Violation rule specifies the formatting for a policy violation. This generally means that it alters the description
 * attribute on the PolicyViolation object. This is often used to describe the violation in user-friendly terms. In the
 * case of Role and Entitlement SOD policies, this can be used to summarize a set of violations detected into a
 * multi-line string description.
 * <p>
 * Output:
 * Rule returns the altered policyViolation object
 */
@Slf4j
public abstract class ViolationRule
        extends AbstractJavaRuleExecutor<PolicyViolation, ViolationRule.ViolationRuleArguments> {

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
     * Name of violation argument name
     */
    public static final String ARG_VIOLATION = "violation";
    /**
     * Name of state argument name
     */
    public static final String ARG_STATE = "state";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ViolationRule.ARG_IDENTITY,
            ViolationRule.ARG_POLICY,
            ViolationRule.ARG_CONSTRAINT,
            ViolationRule.ARG_VIOLATION,
            ViolationRule.ARG_STATE
    );

    /**
     * Default constructor
     */
    public ViolationRule() {
        super(Rule.Type.Violation.name(), ViolationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ViolationRule.ViolationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ViolationRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ViolationRule.ARG_IDENTITY))
                .policy((Policy) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ViolationRule.ARG_POLICY))
                .constraint((BaseConstraint) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ViolationRule.ARG_CONSTRAINT))
                .violation((PolicyViolation) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ViolationRule.ARG_VIOLATION))
                .state((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ViolationRule.ARG_STATE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     * - policy
     * - constraint
     * - violation
     * - state
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ViolationRuleArguments {
        /**
         * Reference to the Identity object to whom the violation applies
         */
        @Argument(name = ViolationRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Reference to the policy object that has been violated
         */
        @Argument(name = ViolationRule.ARG_POLICY)
        private final Policy policy;
        /**
         * Reference to the constraint, or policy rule, with in the policy that has been violated
         */
        @Argument(name = ViolationRule.ARG_CONSTRAINT)
        private final BaseConstraint constraint;
        /**
         * Reference to the policyViolation object that records the violation
         */
        @Argument(name = ViolationRule.ARG_VIOLATION)
        private final PolicyViolation violation;
        /**
         * A Map that can be used to store and share data between executions of this rule
         */
        @Argument(name = ViolationRule.ARG_VIOLATION)
        private final Map<String, Object> state;

    }
}
