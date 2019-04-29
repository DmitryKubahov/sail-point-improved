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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Applies only to applications of type DelimitedFile. It is run for each row of data as it is read in
 * from a connector. A BuildMap rule is used to manipulate the raw input data (provided via the rows and columns
 * in the file) and build a map out of the incoming data
 * <p>
 * NOTE: Because this rule is run for each record in the input file, it can have a noticeable effect on performance if
 * it contains time-intensive operations. Where possible, complicated lookups should be done in the PreIterate
 * rule, with the results stored in CustomGlobal for use by the BuildMap rule; the global data should be removed
 * by the PostIterate rule
 */
@Slf4j
public abstract class BuildMapRule
        extends AbstractJavaRuleExecutor<Map<String, Object>, BuildMapRule.BuildMapRuleArguments> {

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
     * Name of record argument name
     */
    public static final String ARG_RECORD_NAME = "record";
    /**
     * Name of cols argument name
     */
    public static final String ARG_COLUMNS_NAME = "cols";
    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            BuildMapRule.ARG_APPLICATION_NAME,
            BuildMapRule.ARG_SCHEMA_NAME,
            BuildMapRule.ARG_STATE_NAME,
            BuildMapRule.ARG_RECORD_NAME,
            BuildMapRule.ARG_COLUMNS_NAME
    );

    /**
     * Default constructor
     */
    public BuildMapRule() {
        super(Rule.Type.BuildMap.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build argument container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected BuildMapRule.BuildMapRuleArguments buildContainerArguments(JavaRuleContext javaRuleContext) {
        return BuildMapRuleArguments
                .builder()
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, BuildMapRule.ARG_APPLICATION_NAME))
                .schema((Schema) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, BuildMapRule.ARG_SCHEMA_NAME))
                .state((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, BuildMapRule.ARG_STATE_NAME))
                .record((List<String>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, BuildMapRule.ARG_RECORD_NAME))
                .columns((List<String>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, BuildMapRule.ARG_COLUMNS_NAME))
                .build();
    }

    /**
     * Arguments container for build map rule. Contains:
     * - application
     * - schema
     * - state
     * - connection
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class BuildMapRuleArguments {
        /**
         * A reference to the Application object
         */
        @Argument(name = BuildMapRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * A reference to the Schema object for the delimited File source being read
         */
        @Argument(name = BuildMapRule.ARG_SCHEMA_NAME)
        private final Schema schema;
        /**
         * A Map that can be used to store and share data between executions of this rule during a single aggregation run
         */
        @Argument(name = BuildMapRule.ARG_STATE_NAME)
        private final Map<String, Object> state;
        /**
         * An ordered list of the values for the current record (parsed based on the specified delimiter)
         */
        @Argument(name = BuildMapRule.ARG_RECORD_NAME)
        private final List<String> record;
        /**
         * An ordered list of the column names from the file’s header record or specified Columns list
         */
        @Argument(name = BuildMapRule.ARG_COLUMNS_NAME)
        private final List<String> columns;
    }
}
