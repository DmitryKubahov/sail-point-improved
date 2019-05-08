package com.sailpoint.improved.rule.certification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Like an IdentityTrigger, an IdentitySelector rule can apply to a Certification Event or a Lifecycle Event and
 * determines whether the associated certification or business process should be run for the Identity on which an
 * action occurs. The difference is that an IdentityTrigger rule defines the event itself whereas an IdentitySelector
 * rule determines the set of Identities to which the event applies. Additionally, the IdentitySelector rule (or any
 * identity selector filter) is evaluated before the action is examined, so if the Identity on which the action occurred
 * is not part of the Identity selector filter, the action is ignored and the certification or business process is not
 * fired.
 * <p>
 * Like IdentityTrigger rules, these rules only run during refresh or aggregation if the “process events” option is
 * selected for the identity refresh or aggregation task.
 * <p>
 * IdentitySelector rules can also be used for specifying criteria for role assignment or for Advanced Policy
 * detection. In the case of role assignment rules, if the rule returns “true”, the role is assigned to the Identity. See
 * the description of the Policy rule type for more information on the Policy usage of IdentitySelector rules. Role
 * assignment and policy rules are also run by Identity Refresh tasks, though their execution is controlled by the
 * “Refresh assigned, detected roles and promote additional entitlements” and “Check active policies” options,
 * respectively.
 * <p>
 * Output:
 * True if the Identity meets the criteria for running the certification/business process or false if it does not
 */
@Slf4j
public abstract class IdentitySelectorRule
        extends AbstractJavaRuleExecutor<Boolean, IdentitySelectorRule.IdentitySelectorRuleArguments> {

    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY_NAME = "identity";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Collections.singletonList(
            IdentitySelectorRule.ARG_IDENTITY_NAME
    );

    /**
     * Default constructor
     */
    public IdentitySelectorRule() {
        super(Rule.Type.IdentitySelector.name(),
                IdentitySelectorRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected IdentitySelectorRule.IdentitySelectorRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return IdentitySelectorRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        IdentitySelectorRule.ARG_IDENTITY_NAME))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class IdentitySelectorRuleArguments {
        /**
         * Identity object on which the triggering action has occurred (post-change version unless change is a delete action,
         * in which case pre-change version is passed to rule)
         */
        @Argument(name = IdentitySelectorRule.ARG_IDENTITY_NAME)
        private final Identity identity;
    }
}
