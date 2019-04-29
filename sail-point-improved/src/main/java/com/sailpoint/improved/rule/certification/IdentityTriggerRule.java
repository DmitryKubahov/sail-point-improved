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
 * Common class for all identity trigger java rules
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
     * Build argument container for current rule
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