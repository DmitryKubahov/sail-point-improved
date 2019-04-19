package com.sailpoint.improved.rule;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.runner.JavaRuleRunner;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;

/**
 * Common class for all identity trigger java rules
 */
@Slf4j
public abstract class IdentityTriggerRule extends AbstractJavaRuleExecutor {


    /**
     * {@link JavaRuleRunner} calls this method. Only after this it is suitable to to different stuff
     *
     * @param javaRuleContext - rule context with sailpoint context and passed parameters
     * @return rule execution result
     * @throws Exception - error while preparing, validating or executing rule
     */
    @Override
    public Object execute(@NonNull JavaRuleContext javaRuleContext) {
        log.debug("Start identity trigger execution");
        log.trace("Parameters:[{}]", javaRuleContext.getArguments());

        log.debug("Build rule container arguments");
        IdentityTriggerRuleArguments containerArguments = IdentityTriggerRuleArguments.builder()
                .newIdentity((Identity) getAttributeByName(javaRuleContext, Dictionary.ARG_NEW_IDENTITY_NAME))
                .previousIdentity((Identity) getAttributeByName(javaRuleContext, Dictionary.ARG_PREVIOUS_IDENTITY_NAME))
                .build();
        log.debug("Get container arguments for identity trigger rule");
        log.trace("Container arguments:[{}]", containerArguments);

        log.debug("Execute identity trigger rule with context and container arguments");
        return executeIdentityTriggerRule(javaRuleContext.getContext(), containerArguments);
    }

    /**
     * Real executor method for identity trigger rules with parameters
     *
     * @param context   - sail point context instance
     * @param arguments - arguments for identity trigger rule
     * @return execution result of identity trigger task
     */
    protected abstract Object executeIdentityTriggerRule(SailPointContext context,
                                                         IdentityTriggerRuleArguments arguments);

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
        @Argument(name = Dictionary.ARG_PREVIOUS_IDENTITY_NAME)
        private Identity previousIdentity;

        /**
         * Identity as it existed after it was updated
         */
        @Argument(name = Dictionary.ARG_NEW_IDENTITY_NAME)
        private Identity newIdentity;
    }

    /**
     * IdentityTrigger rules dictionary
     */
    public static class Dictionary {

        /**
         * Name of previous identity argument name
         */
        public static final String ARG_PREVIOUS_IDENTITY_NAME = "previousIdentity";
        /**
         * Name of new identity argument name
         */
        public static final String ARG_NEW_IDENTITY_NAME = "newIdentity";

        /**
         * Only dictionary functions
         */
        private Dictionary() {
        }
    }
}
