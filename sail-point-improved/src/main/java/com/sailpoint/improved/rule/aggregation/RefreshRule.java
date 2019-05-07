package com.sailpoint.improved.rule.aggregation;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Runs at the end of the Identity Refresh process, both during an Identity Refresh task and at the
 * end of an Aggregation task. It allows custom manipulation of Identity attributes while the Identity is being
 * refreshed. Refresh rules are most commonly used in manually-executed refresh tasks configured for data
 * cleanup when erroneous aggregation configurations have resulted in unintended data consequences on
 * Identities. However, they can also be used in normal refresh or aggregation tasks to set attributes (usually
 * custom attributes).
 * <p>
 * NOTE: IdentityIQ 6.0 introduced a second option for running a Refresh rule at the beginning of the Identity
 * Refresh process in addition to the end. To have the rule run at the beginning, specify it as a “preRefreshRule”,
 * as shown in the Definition and Storage Location section below. This option will rarely be used but is available if
 * attribute values need to be manipulated before the Identity and its associated links and roles are refreshed.
 * <p>
 * NOTE: Since the Refresh rules run for every Identity involved in the aggregation or refresh task, time-intensive
 * operations performed in it can have a negative impact on task performance.
 * <p>
 * Outputs:
 * None. The identity object passed as parameter to the rule should be edited directly by the rule.
 */
@Slf4j
public abstract class RefreshRule
        extends AbstractJavaRuleExecutor<Object, RefreshRule.RefreshRuleArguments> {

    /**
     * Name of environment argument name
     */
    public static final String ARG_ENVIRONMENT_NAME = "environment";
    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY_NAME = "identity";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            RefreshRule.ARG_ENVIRONMENT_NAME,
            RefreshRule.ARG_IDENTITY_NAME
    );

    /**
     * Default constructor
     */
    public RefreshRule() {
        super(Rule.Type.Refresh.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected RefreshRule.RefreshRuleArguments buildContainerArguments(
            JavaRuleContext javaRuleContext) {
        return RefreshRuleArguments
                .builder()
                .environment((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, RefreshRule.ARG_ENVIRONMENT_NAME))
                .identity((Identity) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, RefreshRule.ARG_IDENTITY_NAME))
                .build();
    }

    /**
     * Arguments container for {@link RefreshRule}. Contains:
     * - environment
     * - identity
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class RefreshRuleArguments {
        /**
         * Arguments passed to the aggregation or refresh task
         */
        @Argument(name = RefreshRule.ARG_ENVIRONMENT_NAME)
        private final Map<String, Object> environment;
        /**
         * Reference to the Identity object being refreshed
         */
        @Argument(name = RefreshRule.ARG_IDENTITY_NAME)
        private final Identity identity;
    }
}
