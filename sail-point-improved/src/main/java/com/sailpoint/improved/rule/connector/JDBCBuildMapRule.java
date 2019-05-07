package com.sailpoint.improved.rule.connector;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Applies only to applications of type JDBC. It functions for JDBC applications just like the
 * BuildMap rule does for Delimited File applications: it is used by the JDBC connector to create a map
 * representation of the incoming ResultSet. The rule is called for each row of data as it is read in from the JDBC
 * Rules in IdentityIQ. It is used to manipulate the raw input data (provided via the rows and columns) to build a map out of
 * the incoming data.
 * <p>
 * NOTE: Since this rule is run for every row of data returned from the resource, time-intensive operations
 * performed within this rule can have a noticeable impact on aggregation performance. Try to avoid lengthy or
 * complex operations in this rule.
 */
@Slf4j
public abstract class JDBCBuildMapRule
        extends AbstractJavaRuleExecutor<Map<String, Object>, JDBCBuildMapRule.JDBCBuildMapRuleArguments> {

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
     * Name of result set argument name
     */
    public static final String ARG_RESULT_SET_NAME = "result";
    /**
     * Name of connection argument name
     */
    public static final String ARG_CONNECTION_NAME = "connection";
    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            JDBCBuildMapRule.ARG_APPLICATION_NAME,
            JDBCBuildMapRule.ARG_SCHEMA_NAME,
            JDBCBuildMapRule.ARG_STATE_NAME,
            JDBCBuildMapRule.ARG_RESULT_SET_NAME,
            JDBCBuildMapRule.ARG_CONNECTION_NAME
    );

    /**
     * Default constructor
     */
    public JDBCBuildMapRule() {
        super(Rule.Type.JDBCBuildMap.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected JDBCBuildMapRule.JDBCBuildMapRuleArguments buildContainerArguments(JavaRuleContext javaRuleContext) {
        return JDBCBuildMapRuleArguments
                .builder()
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, JDBCBuildMapRule.ARG_APPLICATION_NAME))
                .schema((Schema) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, JDBCBuildMapRule.ARG_SCHEMA_NAME))
                .state((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, JDBCBuildMapRule.ARG_STATE_NAME))
                .result((ResultSet) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, JDBCBuildMapRule.ARG_RESULT_SET_NAME))
                .connection((Connection) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, JDBCBuildMapRule.ARG_CONNECTION_NAME))
                .build();
    }

    /**
     * Arguments container for {@link JDBCBuildMapRule}. Contains:
     * - application
     * - schema
     * - state
     * - connection
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class JDBCBuildMapRuleArguments {
        /**
         * A reference to the Application object
         */
        @Argument(name = JDBCBuildMapRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * A reference to the Schema object for the JDBC source being read
         */
        @Argument(name = JDBCBuildMapRule.ARG_SCHEMA_NAME)
        private final Schema schema;
        /**
         * A Map that can be used to store and share data between executions of this rule during a single aggregation run
         */
        @Argument(name = JDBCBuildMapRule.ARG_STATE_NAME)
        private final Map<String, Object> state;
        /**
         * The current ResultSet from the JDBC connector
         */
        @Argument(name = JDBCBuildMapRule.ARG_RESULT_SET_NAME)
        private final ResultSet result;
        /**
         * A reference to the current SQL connection
         */
        @Argument(name = JDBCBuildMapRule.ARG_CONNECTION_NAME)
        private final Connection connection;
    }
}
