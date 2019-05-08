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
import sailpoint.tools.GeneralException;

import java.text.MessageFormat;
import java.util.Collections;

/**
 * Applies to both Certification Events and Lifecycle Events; they determine whether the
 * associated certification or business process (respectively) should be triggered for the Identity on which an action
 * occurs. IdentityTrigger rules run anytime an Identity is changed in an Identity Refresh or Aggregation if the
 * “Process Events” option is selected on the task, and they are passed the Identity as it existed before and after
 * the change. They also run when an Identity is edited through the Define -> Identities administrator page. The
 * rule’s logic determines what attributes are evaluated, and the rule can return a True or False value; True fires
 * the certification/business process associated with the rule and False does not.
 * <p>
 * When more than one trigger exists, they are retrieved from the database without regard to order, so their
 * evaluation order depends on the database engine and possibly the order in which they were created in the
 * database. Regardless, all are passed the same new and previous identity values (i.e. the effects of the one
 * trigger’s event do not feed into the next trigger’s evaluation). Additionally, if multiple triggers’ conditions are
 * met in one Identity update, the events launched by the triggers are processed in the background and may occur
 * concurrently.
 * <p>
 * Output:
 * True if the event should be triggered or false if it should not
 */
@Slf4j
public abstract class IdentityTriggerRule
        extends AbstractJavaRuleExecutor<Boolean, IdentityTriggerRule.IdentityTriggerRuleArguments> {

    /**
     * Name of previous identity argument name
     */
    public static final String ARG_PREVIOUS_IDENTITY_NAME = "previousIdentity";
    /**
     * Name of new identity argument name
     */
    public static final String ARG_NEW_IDENTITY_NAME = "newIdentity";

    /**
     * Validation context error message. Parameters:
     * 0 - newIdentity argument name
     * 1 - previousIdentity argument name
     */
    public static final String VALIDATION_ERROR_MESSAGE = "[{0}] and [{1}] are null";

    /**
     * Default constructor
     */
    public IdentityTriggerRule() {
        super(Rule.Type.IdentityTrigger.name(), Collections.emptyList());
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected IdentityTriggerRule.IdentityTriggerRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return IdentityTriggerRuleArguments.builder()
                .newIdentity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IdentityTriggerRule.ARG_NEW_IDENTITY_NAME))
                .previousIdentity(
                        (Identity) JavaRuleExecutorUtil
                                .getArgumentValueByName(javaRuleContext,
                                        IdentityTriggerRule.ARG_PREVIOUS_IDENTITY_NAME))
                .build();
    }

    /**
     * Identity trigger rule validation.
     * Arguments {@link IdentityTriggerRule#ARG_NEW_IDENTITY_NAME} and {@link IdentityTriggerRule#ARG_PREVIOUS_IDENTITY_NAME} can not be both null simultaneously.
     *
     * @param javaRuleContext - rule context to validate
     * @throws GeneralException - newIdentity and previousIdentity are null
     */
    @Override
    protected void internalValidateArguments(JavaRuleContext javaRuleContext) throws GeneralException {
        log.debug("Validate arguments attributes: {} and {}", IdentityTriggerRule.ARG_NEW_IDENTITY_NAME,
                IdentityTriggerRule.ARG_PREVIOUS_IDENTITY_NAME);
        if (JavaRuleExecutorUtil
                .getArgumentValueByName(javaRuleContext, IdentityTriggerRule.ARG_NEW_IDENTITY_NAME) == null
                && JavaRuleExecutorUtil
                .getArgumentValueByName(javaRuleContext, IdentityTriggerRule.ARG_PREVIOUS_IDENTITY_NAME) == null) {
            String errorMessage = MessageFormat.format(IdentityTriggerRule.VALIDATION_ERROR_MESSAGE,
                    IdentityTriggerRule.ARG_NEW_IDENTITY_NAME, IdentityTriggerRule.ARG_PREVIOUS_IDENTITY_NAME);
            log.error("Identity trigger rule execution validation error:[{}]", errorMessage);
            throw new GeneralException(errorMessage);
        }
    }

    /**
     * Arguments container for identity trigger rule. Contains prev and new identity snapshot.
     * <p>
     * NOTE: Either Identity can be void (previous is void on Identity creation and new is void on Identity deletion) and
     * the rule must test for this to prevent a possible exception condition
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class IdentityTriggerRuleArguments {
        /**
         * Identity as it existed before it was updated
         */
        @Argument(name = ARG_PREVIOUS_IDENTITY_NAME)
        private final Identity previousIdentity;

        /**
         * Identity as it existed after it was updated
         */
        @Argument(name = ARG_NEW_IDENTITY_NAME)
        private final Identity newIdentity;
    }
}
