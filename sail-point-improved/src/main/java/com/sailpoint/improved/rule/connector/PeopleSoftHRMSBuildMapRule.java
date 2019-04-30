package com.sailpoint.improved.rule.connector;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.connector.PeopleSoftHRMSConnector;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Schema;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Applies only to applications of type PeopleSoft HCM Database. This rule was
 * introduced in version 7.0 with the PeopleSoft HCM Database Connector.
 * As with the SAPBuildMap rule above, the PeopleSoft HRMS connector builds the attribute map for each object
 * read from the connector before it calls this rule, so it passes the rule a prebuilt Map object instead of requiring
 * the rule to build the map from a record. This rule can then modify the map as needed.
 * <p>
 * NOTE: Since a PeopleSoftHRMSBuildMap rule is run once for every object read from a PeopleSoft HRMS data
 * source, performing time-intensive operations in this rule can have a negative performance impact.
 */
@Slf4j
public abstract class PeopleSoftHRMSBuildMapRule
        extends
        AbstractJavaRuleExecutor<Map<String, Object>, PeopleSoftHRMSBuildMapRule.PeopleSoftHRMSBuildMapRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION_NAME = "application";
    /**
     * Name of schema argument name
     */
    public static final String ARG_SCHEMA_NAME = "schema";
    /**
     * Name of state argument name
     */
    public static final String ARG_STATE_NAME = "state";
    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY_NAME = "identity";
    /**
     * Name of connection argument name
     */
    public static final String ARG_CONNECTION_NAME = "connection";
    /**
     * Name of connector argument name
     */
    public static final String ARG_CONNECTOR_NAME = "connector";
    /**
     * Name of map argument name
     */
    public static final String ARG_MAP_NAME = "map";
    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            PeopleSoftHRMSBuildMapRule.ARG_APPLICATION_NAME,
            PeopleSoftHRMSBuildMapRule.ARG_SCHEMA_NAME,
            PeopleSoftHRMSBuildMapRule.ARG_STATE_NAME,
            PeopleSoftHRMSBuildMapRule.ARG_IDENTITY_NAME,
            PeopleSoftHRMSBuildMapRule.ARG_CONNECTION_NAME,
            PeopleSoftHRMSBuildMapRule.ARG_CONNECTOR_NAME,
            PeopleSoftHRMSBuildMapRule.ARG_MAP_NAME
    );

    /**
     * Default constructor
     */
    public PeopleSoftHRMSBuildMapRule() {
        super(Rule.Type.PeopleSoftHRMSBuildMap.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected PeopleSoftHRMSBuildMapRule.PeopleSoftHRMSBuildMapRuleArguments buildContainerArguments(
            JavaRuleContext javaRuleContext) {
        return PeopleSoftHRMSBuildMapRuleArguments
                .builder()
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PeopleSoftHRMSBuildMapRule.ARG_APPLICATION_NAME))
                .schema((Schema) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PeopleSoftHRMSBuildMapRule.ARG_SCHEMA_NAME))
                .state((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PeopleSoftHRMSBuildMapRule.ARG_STATE_NAME))
                .identity((String) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PeopleSoftHRMSBuildMapRule.ARG_IDENTITY_NAME))
                .connection((Connection) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PeopleSoftHRMSBuildMapRule.ARG_CONNECTION_NAME))
                .connector((PeopleSoftHRMSConnector) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PeopleSoftHRMSBuildMapRule.ARG_CONNECTOR_NAME))
                .map((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PeopleSoftHRMSBuildMapRule.ARG_MAP_NAME))
                .build();
    }

    /**
     * Arguments container for {@link PeopleSoftHRMSBuildMapRule}. Contains:
     * - application
     * - schema
     * - state
     * - destination
     * - object
     * - connector
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class PeopleSoftHRMSBuildMapRuleArguments {
        /**
         * A reference to the Application object
         */
        @Argument(name = PeopleSoftHRMSBuildMapRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * A reference to the Schema object that represents the object we are building
         */
        @Argument(name = PeopleSoftHRMSBuildMapRule.ARG_SCHEMA_NAME)
        private final Schema schema;
        /**
         * A Map that can be used to store and share data between executions of this rule during a single aggregation run
         */
        @Argument(name = PeopleSoftHRMSBuildMapRule.ARG_STATE_NAME)
        private final Map<String, Object> state;
        /**
         * Name of the target identity
         */
        @Argument(name = PeopleSoftHRMSBuildMapRule.ARG_IDENTITY_NAME)
        private final String identity;
        /**
         * Connection to the application database
         */
        @Argument(name = PeopleSoftHRMSBuildMapRule.ARG_CONNECTION_NAME)
        private final Connection connection;
        /**
         * A reference to the current PeopleSoft HRMS Connector
         */
        @Argument(name = PeopleSoftHRMSBuildMapRule.ARG_CONNECTOR_NAME)
        private final PeopleSoftHRMSConnector connector;
        /**
         * Map of attributes pre-built by the connector, to which the rule can add more attributes, as needed
         */
        @Argument(name = PeopleSoftHRMSBuildMapRule.ARG_MAP_NAME)
        private final Map<String, Object> map;
    }
}
