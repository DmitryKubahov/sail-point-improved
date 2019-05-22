package com.sailpoint.improved.rule.connector;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Application;
import sailpoint.object.Attributes;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Schema;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Used with applications of type RuleBasedFileParser, which is used to parse non-delimited
 * files. This connector can read account and group data from non-standard or free format text. The rule is called
 * to retrieve each complete record from the file; logic in the rule determines what constitutes a complete record –
 * whether that is on one line in the file or whether it spans multiple lines.
 * <p>
 * NOTE: Since the FileParsingRule rule runs to extract every account or group record from the file and build it into
 * a map, any time-intensive operations performed in this rule can have a negative performance impact.
 */
@Slf4j
public abstract class FileParsingRule
        extends AbstractJavaRuleExecutor<Map<String, Object>, FileParsingRule.FileParsingRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of schema argument name
     */
    public static final String ARG_SCHEMA = "schema";
    /**
     * Name of config argument name
     */
    public static final String ARG_CONFIG = "config";
    /**
     * Name of inputStream argument name
     */
    public static final String ARG_INPUT_STREAM = "inputStream";
    /**
     * Name of reader argument name
     */
    public static final String ARG_READER = "reader";
    /**
     * Name of state argument name
     */
    public static final String ARG_STATE = "state";
    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            FileParsingRule.ARG_APPLICATION,
            FileParsingRule.ARG_SCHEMA,
            FileParsingRule.ARG_CONFIG,
            FileParsingRule.ARG_INPUT_STREAM,
            FileParsingRule.ARG_READER,
            FileParsingRule.ARG_STATE
    );

    /**
     * Default constructor
     */
    public FileParsingRule() {
        super(Rule.Type.FileParsingRule.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context for arguments parameters
     * @return argument container instance
     */
    @Override
    protected FileParsingRule.FileParsingRuleArguments buildContainerArguments(JavaRuleContext javaRuleContext) {
        return FileParsingRuleArguments
                .builder()
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, FileParsingRule.ARG_APPLICATION))
                .schema((Schema) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, FileParsingRule.ARG_SCHEMA))
                .config((Attributes) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, FileParsingRule.ARG_CONFIG))
                .inputStream((BufferedInputStream) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, FileParsingRule.ARG_INPUT_STREAM))
                .reader((BufferedReader) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, FileParsingRule.ARG_READER))
                .state((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, FileParsingRule.ARG_STATE))
                .build();
    }

    /**
     * Arguments container for {@link FileParsingRule}. Contains:
     * - application
     * - schema
     * - config
     * - inputStream
     * - reader
     * - state
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class FileParsingRuleArguments {
        /**
         * A reference to the Application object
         */
        @Argument(name = FileParsingRule.ARG_APPLICATION)
        private final Application application;
        /**
         * A reference to the Schema object for the delimited file source being read
         */
        @Argument(name = FileParsingRule.ARG_SCHEMA)
        private final Schema schema;
        /**
         * Attributes Map of Application configuration attributes
         */
        @Argument(name = FileParsingRule.ARG_CONFIG)
        private final Attributes config;
        /**
         * A reference to the file input stream
         */
        @Argument(name = FileParsingRule.ARG_INPUT_STREAM)
        private final BufferedInputStream inputStream;
        /**
         * A reader wrapping the inputStream
         */
        @Argument(name = FileParsingRule.ARG_READER)
        private final BufferedReader reader;
        /**
         * A Map that can be used to store and share data between executions of this rule during a single aggregation run
         */
        @Argument(name = FileParsingRule.ARG_STATE)
        private final Map<String, Object> state;
    }
}
