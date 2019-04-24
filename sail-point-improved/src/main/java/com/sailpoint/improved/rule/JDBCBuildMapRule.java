package com.sailpoint.improved.rule;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Schema;
import sailpoint.tools.GeneralException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;

/**
 * Common rule for all JDBCBuildMap rules
 */
@Slf4j
public abstract class JDBCBuildMapRule extends AbstractJavaRuleExecutor<Map<String, Object>> {

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
     * Main execution method for JDBCBuildMap rule. During execution:
     * 1 - checks all necessary attributes in rule context via {@link JDBCBuildMapRule#internalValidation(JavaRuleContext)}
     * 2 - run {@link JDBCBuildMapRule#executeJDBCBuildMapRule(SailPointContext, JDBCBuildMapRule.JDBCBuildMapRuleArguments)}
     *
     * @param javaRuleContext - rule context
     * @return rule execution result
     * @throws GeneralException error of execution jdbc build map rule
     */
    @Override
    protected Map<String, Object> internalExecute(JavaRuleContext javaRuleContext) throws GeneralException {
        log.debug("Start jdbc build map rule execution");
        log.trace("Parameters:[{}]", javaRuleContext.getArguments());

        log.debug("Build jdbc build map rule container arguments");
        JDBCBuildMapRule.JDBCBuildMapRuleArguments containerArguments = JDBCBuildMapRuleArguments
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
        log.debug("Got container arguments for jdbc build map rule");
        log.trace("Container arguments:[{}]", containerArguments);

        log.debug("Execute jdbc build map rule  with context and container arguments");
        return executeJDBCBuildMapRule(javaRuleContext.getContext(), containerArguments);
    }

    /**
     * JDBC build map rule validation.
     * Checks all arguments by name
     * - {@link JDBCBuildMapRule#ARG_APPLICATION_NAME}
     * - {@link JDBCBuildMapRule#ARG_SCHEMA_NAME}
     * - {@link JDBCBuildMapRule#ARG_STATE_NAME}
     * - {@link JDBCBuildMapRule#ARG_RESULT_SET_NAME}
     * - {@link JDBCBuildMapRule#ARG_CONNECTION_NAME}
     * All must be not null
     *
     * @param javaRuleContext - rule context to validate
     * @throws GeneralException - one of the arguments is null
     */
    @Override
    protected void internalValidation(JavaRuleContext javaRuleContext) throws GeneralException {
        log.debug("Validate jdbc build map rule arguments");
        JavaRuleExecutorUtil.notNullArgumentValidation(javaRuleContext, JDBCBuildMapRule.ARG_APPLICATION_NAME);
        JavaRuleExecutorUtil.notNullArgumentValidation(javaRuleContext, JDBCBuildMapRule.ARG_SCHEMA_NAME);
        JavaRuleExecutorUtil.notNullArgumentValidation(javaRuleContext, JDBCBuildMapRule.ARG_STATE_NAME);
        JavaRuleExecutorUtil.notNullArgumentValidation(javaRuleContext, JDBCBuildMapRule.ARG_RESULT_SET_NAME);
        JavaRuleExecutorUtil.notNullArgumentValidation(javaRuleContext, JDBCBuildMapRule.ARG_CONNECTION_NAME);
    }


    /**
     * Real executor method for jdbc build map rules with parameters
     *
     * @param context   - sail point context instance
     * @param arguments - arguments for jdbc build map rule
     * @return new map of properties for mapping
     * @throws GeneralException error of execution jdbc build map rule
     */
    protected abstract Map<String, Object> executeJDBCBuildMapRule(SailPointContext context,
                                                                   JDBCBuildMapRuleArguments arguments)
            throws GeneralException;

    /**
     * Arguments container for jdbc build map rule. Contains:
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
