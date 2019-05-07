package com.sailpoint.improved.rule.connector;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import com.sap.conn.jco.JCoDestination;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.connector.SAPInternalConnector;
import sailpoint.object.Application;
import sailpoint.object.Attributes;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Schema;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Applies only to applications of type SAP. This rule differs from the Delimited File BuildMap
 * rule and the SAPBuildMap rule in that the SAP connector builds the attribute map for each object read from
 * the connector before it calls this rule, so it passes the rule a prebuilt Map object instead of requiring the rule to
 * build the map from a record or resourceObject. This rule can then modify the map as needed. The rule also
 * receives a “destination” object through which it can make SAP calls to retrieve extra data.
 * <p>
 * NOTE: Since an SAPBuildMap rule is run once for every object read from an SAP data source, performing time-
 * intensive operations in this rule can have a negative performance impact.
 */
@Slf4j
public abstract class SAPBuildMapRule
        extends AbstractJavaRuleExecutor<Attributes, SAPBuildMapRule.SAPBuildMapRuleArguments> {

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
     * Name of destination argument name
     */
    public static final String ARG_DESTINATION_NAME = "destination";
    /**
     * Name of connection argument name
     */
    public static final String ARG_OBJECT_NAME = "object";
    /**
     * Name of connector argument name
     */
    public static final String ARG_CONNECTOR_NAME = "connector";
    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            SAPBuildMapRule.ARG_APPLICATION_NAME,
            SAPBuildMapRule.ARG_SCHEMA_NAME,
            SAPBuildMapRule.ARG_STATE_NAME,
            SAPBuildMapRule.ARG_DESTINATION_NAME,
            SAPBuildMapRule.ARG_OBJECT_NAME,
            SAPBuildMapRule.ARG_CONNECTOR_NAME
    );

    /**
     * Default constructor
     */
    public SAPBuildMapRule() {
        super(Rule.Type.SAPBuildMap.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected SAPBuildMapRule.SAPBuildMapRuleArguments buildContainerArguments(JavaRuleContext javaRuleContext) {
        return SAPBuildMapRuleArguments
                .builder()
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, SAPBuildMapRule.ARG_APPLICATION_NAME))
                .schema((Schema) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, SAPBuildMapRule.ARG_SCHEMA_NAME))
                .state((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, SAPBuildMapRule.ARG_STATE_NAME))
                .destination((JCoDestination) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, SAPBuildMapRule.ARG_DESTINATION_NAME))
                .object((Attributes) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, SAPBuildMapRule.ARG_OBJECT_NAME))
                .connector((SAPInternalConnector) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, SAPBuildMapRule.ARG_CONNECTOR_NAME))
                .build();
    }

    /**
     * Arguments container for {@link SAPBuildMapRule}. Contains:
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
    public static class SAPBuildMapRuleArguments {
        /**
         * A reference to the Application object
         */
        @Argument(name = SAPBuildMapRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * A reference to the Schema object that represents the object we are building
         */
        @Argument(name = SAPBuildMapRule.ARG_SCHEMA_NAME)
        private final Schema schema;
        /**
         * A Map that can be used to store and share data between executions of this rule during a single aggregation run
         */
        @Argument(name = SAPBuildMapRule.ARG_STATE_NAME)
        private final Map<String, Object> state;
        /**
         * A connected and ready to use SAP destination object that can be used to call BAPI function modules and call to SAP tables
         */
        @Argument(name = SAPBuildMapRule.ARG_DESTINATION_NAME)
        private final JCoDestination destination;
        /**
         * A reference to a SailPoint attributes object (basically a Map object with some added convenience methods) that holds the
         * attributes that have been built up by the default connector implementation. The rule should modify this object to change, add or
         * remove attributes from the map.
         */
        @Argument(name = SAPBuildMapRule.ARG_OBJECT_NAME)
        private final Attributes object;
        /**
         * A reference to the current SAP Connector
         */
        @Argument(name = SAPBuildMapRule.ARG_CONNECTOR_NAME)
        private final SAPInternalConnector connector;
    }
}
