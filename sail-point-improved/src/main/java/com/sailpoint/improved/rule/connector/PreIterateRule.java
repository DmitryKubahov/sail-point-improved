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

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Applies only to DelimitedFile and RuleBasedFileParser connectors. It is run immediately after
 * the file input stream is opened, before all other connector rules. It can be used to execute any processing that
 * should occur prior to iterating through the records in the file. Commonly it is used to configure global data that
 * will be used by the other rules, unzip/move/copy files, or validate files
 * <p>
 * NOTE: A preIterate rule can optionally return an inputStream. If it does, this new stream will replace the
 * opened file inputStream in the remainder of the delimited file processing.
 */
@Slf4j
public abstract class PreIterateRule
        extends AbstractJavaRuleExecutor<InputStream, PreIterateRule.PreIterateRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION_NAME = "application";
    /**
     * Name of schema argument name
     */
    public static final String ARG_SCHEMA_NAME = "schema";
    /**
     * Name of stats argument name
     */
    public static final String ARG_STATS_NAME = "stats";
    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            PreIterateRule.ARG_APPLICATION_NAME,
            PreIterateRule.ARG_SCHEMA_NAME,
            PreIterateRule.ARG_STATS_NAME
    );

    /**
     * Default constructor
     */
    public PreIterateRule() {
        super(Rule.Type.PreIterate.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context for arguments parameters
     * @return argument container instance
     */
    @Override
    protected PreIterateRule.PreIterateRuleArguments buildContainerArguments(JavaRuleContext javaRuleContext) {
        return PreIterateRule.PreIterateRuleArguments
                .builder()
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PreIterateRule.ARG_APPLICATION_NAME))
                .schema((Schema) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PreIterateRule.ARG_SCHEMA_NAME))
                .stats((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PreIterateRule.ARG_STATS_NAME))
                .build();
    }

    /**
     * Arguments container for {@link PreIterateRule}. Contains:
     * - application
     * - schema
     * - stats
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class PreIterateRuleArguments {
        /**
         * Stats:       fileName
         * description: filename of the file about to be processed
         */
        public static final String STATS_KEY_FILE_NAME = "fileName";
        /**
         * Stats:       absolutePath
         * description: absolute filename
         */
        public static final String STATS_KEY_ABSOLUTE_PATH = "absolutePath";
        /**
         * Stats:       length
         * description: file length in bytes
         */
        public static final String STATS_KEY_LENGTH = "length";
        /**
         * Stats:       lastModified
         * description: last time the file was updated (Java GMT)
         */
        public static final String STATS_KEY_LAST_MODIFIED = "lastModified";

        /**
         * A reference to the Application object
         */
        @Argument(name = PreIterateRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * A reference to the Schema object for the delimited file source being read
         */
        @Argument(name = PreIterateRule.ARG_SCHEMA_NAME)
        private final Schema schema;
        /**
         * A map passed by the connector of the stats for the file about to be iterated. Contains keys:
         * • fileName: (String) filename of the file about to be processed
         * • absolutePath: (String) absolute filename
         * • length: (Long) file length in bytes
         * • lastModified: (Long) last time the file was updated (Java GMT)
         */
        @Argument(name = PreIterateRule.ARG_STATS_NAME)
        private final Map<String, Object> stats;
    }
}
