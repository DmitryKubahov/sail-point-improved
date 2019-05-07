package com.sailpoint.improved.rule.aggregation;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.connector.AbstractConnector;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * As with Identity correlation, manager correlation can be specified on the application definition through a simple
 * attribute match process or it can be managed with a rule. If a rule is defined, it is used to determine the correct
 * manager Identity; if no rule is defined, the Identity attribute match process is used to find the manager Identity.
 * A ManagerCorrelation Rule identifies an Identity’s manager based on an application account attribute (or
 * attributes) on the account being aggregated or refreshed.
 * <p>
 * The ManagerCorrelation rule runs as part of the identity refresh process that occurs either in an identity refresh
 * task or at the end of an account aggregation task, though the manager correlation option is hidden from the UI
 * on the aggregation task and is more often performed only as part of a refresh task. It runs before both the
 * managedAttributeCustomization rule(s) and the Refresh rule (if any).
 * <p>
 * NOTE: In general, manager correlation is bypassed if no change has been detected in the source attribute. This
 * is because manager correlation can be time-intensive activity that negatively impacts aggregation/refresh
 * performance. To force manager correlation to occur for every Identity, even if it has not changed, set the
 * alwaysRefreshManager attribute to “true” in the task attributes map. When this option is set, time-intensive
 * operations performed in the ManagerCorrelation rule can have a negative impact on task performance.
 * <p>
 * Outputs:
 * Map that identifies the manager’s Identity; may contain
 * any of the following:
 * “identityName”, “[identity.name value]”
 * or
 * “identity”, [Identity object]
 * or
 * “identityAttributeName”, “[attribute name]”
 * “identityAttributeValue”, “[attribute value]”
 * where the attribute value uniquely identifies one Identity
 */
@Slf4j
public abstract class ManagerCorrelationRule
        extends AbstractJavaRuleExecutor<Map<String, Object>, ManagerCorrelationRule.ManagerCorrelationRuleArguments> {

    /**
     * Name of environment argument name
     */
    public static final String ARG_ENVIRONMENT_NAME = "environment";
    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION_NAME = "application";
    /**
     * Name of instance argument name
     */
    public static final String ARG_INSTANCE_NAME = "instance";
    /**
     * Name of connector argument name
     */
    public static final String ARG_CONNECTOR_NAME = "connector";
    /**
     * Name of link argument name
     */
    public static final String ARG_LINK_NAME = "link";
    /**
     * Name of managerAttributeValue argument name
     */
    public static final String ARG_MANAGER_ATTRIBUTE_VALUE_NAME = "managerAttributeValue";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ManagerCorrelationRule.ARG_ENVIRONMENT_NAME,
            ManagerCorrelationRule.ARG_APPLICATION_NAME,
            ManagerCorrelationRule.ARG_CONNECTOR_NAME,
            ManagerCorrelationRule.ARG_LINK_NAME
    );

    /**
     * Default constructor
     */
    public ManagerCorrelationRule() {
        super(Rule.Type.ManagerCorrelation.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ManagerCorrelationRule.ManagerCorrelationRuleArguments buildContainerArguments(
            JavaRuleContext javaRuleContext) {
        return ManagerCorrelationRuleArguments
                .builder()
                .environment((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, ManagerCorrelationRule.ARG_ENVIRONMENT_NAME))
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, ManagerCorrelationRule.ARG_APPLICATION_NAME))
                .instance((String) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, ManagerCorrelationRule.ARG_INSTANCE_NAME))
                .connector((AbstractConnector) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, ManagerCorrelationRule.ARG_CONNECTOR_NAME))
                .link((Link) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, ManagerCorrelationRule.ARG_LINK_NAME))
                .managerAttributeValue((String) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext,
                                ManagerCorrelationRule.ARG_MANAGER_ATTRIBUTE_VALUE_NAME))
                .build();
    }

    /**
     * Arguments container for {@link ManagerCorrelationRule}. Contains:
     * - environment
     * - application
     * - instance
     * - connector
     * - link
     * - managerAttributeValue
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ManagerCorrelationRuleArguments {
        /**
         * Map of arguments passed to the aggregation task
         */
        @Argument(name = ManagerCorrelationRule.ARG_ENVIRONMENT_NAME)
        private final Map<String, Object> environment;
        /**
         * A reference to the Application object
         */
        @Argument(name = ManagerCorrelationRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * Application instance name (if not null)
         */
        @Argument(name = ManagerCorrelationRule.ARG_INSTANCE_NAME)
        private final String instance;
        /**
         * A reference to the Connector object used by this application
         */
        @Argument(name = ManagerCorrelationRule.ARG_CONNECTOR_NAME)
        private final AbstractConnector connector;
        /**
         * A reference to the account link
         */
        @Argument(name = ManagerCorrelationRule.ARG_LINK_NAME)
        private final Link link;
        /**
         * Manager attribute value being used to correlated to an Identity
         */
        @Argument(name = ManagerCorrelationRule.ARG_MANAGER_ATTRIBUTE_VALUE_NAME)
        private final String managerAttributeValue;
    }
}
