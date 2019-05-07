package com.sailpoint.improved.rule.aggregation;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;
import sailpoint.object.ResourceObject;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Used to select the existing Identity to which the aggregated account information should be
 * connected. Correlation can be specified on the application definition through a simple attribute match process
 * or it can be managed with a rule. If both are specified, the correlation rule supersedes the correlation attribute
 * specification and the simple attribute match will only be attempted if the rule does not return an Identity.
 * Every time an aggregation task runs, except when the optimize aggregation option has been selected or when
 * an account has been manually correlated to an Identity, the Identity to which the account should be connected
 * is reassessed; if the existing correlation is found to be incorrect or no longer applicable, that connection is
 * broken and a new one is established to the correct Identity.
 * If the correlation rule returns null or if the information returned from the correlation rule does not match to an
 * Identity (and the attribute-matching process also fails to select an Identity), a new Identity will be created (see
 * the IdentityCreation rule) and the account will be correlated to that Identity.
 * <p>
 * NOTE: In IdentityIQ 6.0, optimized aggregation is the default behavior, which means that no changes will be
 * made to a Link object if the corresponding managed system account has not changed. Consequently, accounts
 * will not be recorrelated to Identities in subsequent aggregations if nothing has changed on the application
 * account. (Other actions will be bypassed too, including attribute promotion, manager correlation, etc., but the
 * skipped recorrelation is usually the most noticeable effect of this setting.) Optimized aggregation can be turned
 * off by selecting the Disable optimization of unchanged accounts option in the aggregation task options or
 * specifying <entry key="noOptimizeReaggregation" value="true"/> in the TaskDefinition XML attributes map.
 * <p>
 * NOTE: Except as noted above with respect to optimized aggregation, the Correlation rule runs for every Link
 * created in the aggregation. Therefore, time-intensive operations performed in it can have a negative impact on
 * aggregation performance.
 */
@Slf4j
public abstract class CorrelationRule
        extends AbstractJavaRuleExecutor<Map<String, Object>, CorrelationRule.CorrelationRuleArguments> {

    /**
     * Name of environment argument name
     */
    public static final String ARG_ENVIRONMENT_NAME = "environment";
    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION_NAME = "application";
    /**
     * Name of account argument name
     */
    public static final String ARG_ACCOUNT_NAME = "account";
    /**
     * Name of link argument name
     */
    public static final String ARG_LINK_NAME = "link";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CorrelationRule.ARG_ENVIRONMENT_NAME,
            CorrelationRule.ARG_APPLICATION_NAME,
            CorrelationRule.ARG_ACCOUNT_NAME,
            CorrelationRule.ARG_LINK_NAME
    );

    /**
     * Default constructor
     */
    public CorrelationRule() {
        super(Rule.Type.Correlation.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CorrelationRule.CorrelationRuleArguments buildContainerArguments(
            JavaRuleContext javaRuleContext) {
        return CorrelationRuleArguments
                .builder()
                .environment((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, CorrelationRule.ARG_ENVIRONMENT_NAME))
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, CorrelationRule.ARG_APPLICATION_NAME))
                .account((ResourceObject) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, CorrelationRule.ARG_ACCOUNT_NAME))
                .link((Link) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, CorrelationRule.ARG_LINK_NAME))
                .build();
    }

    /**
     * Arguments container for {@link CorrelationRule}. Contains:
     * - environment
     * - application
     * - account
     * - link
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CorrelationRuleArguments {
        /**
         * Map of arguments passed to the aggregation task
         */
        @Argument(name = CorrelationRule.ARG_ENVIRONMENT_NAME)
        private final Map<String, Object> environment;
        /**
         * A reference to the Application object
         */
        @Argument(name = CorrelationRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * A reference to the ResourceObject passed from the connector
         */
        @Argument(name = CorrelationRule.ARG_ACCOUNT_NAME)
        private final ResourceObject account;
        /**
         * A reference to the existing link identified based on the resourceObject, if any
         */
        @Argument(name = CorrelationRule.ARG_LINK_NAME)
        private final Link link;
    }
}
