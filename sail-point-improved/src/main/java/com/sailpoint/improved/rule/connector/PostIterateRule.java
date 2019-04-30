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
 * Can be specified only for an application of Type: DelimitedFile or RuleBasedFileParser. It runs
 * after all other connector rules and can be used to execute any processing that should occur after the connector
 * iteration is complete. Commonly, it is used to remove any global data used by the other rules (e.g. from
 * CustomGlobal), clean up files, or mark statistics in a configuration object that will be used by the {@link PreIterateRule}
 * during a subsequent aggregation. Since it runs only once per aggregation, this rule generally has a minimal
 * impact on aggregation performance.
 */
@Slf4j
public abstract class PostIterateRule
        extends AbstractJavaRuleExecutor<Object, PostIterateRule.PostIterateRuleArguments> {

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
            PostIterateRule.ARG_APPLICATION_NAME,
            PostIterateRule.ARG_SCHEMA_NAME,
            PostIterateRule.ARG_STATS_NAME
    );

    /**
     * Default constructor
     */
    public PostIterateRule() {
        super(Rule.Type.PostIterate.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context for arguments parameters
     * @return argument container instance
     */
    @Override
    protected PostIterateRule.PostIterateRuleArguments buildContainerArguments(JavaRuleContext javaRuleContext) {
        return PostIterateRule.PostIterateRuleArguments
                .builder()
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PostIterateRule.ARG_APPLICATION_NAME))
                .schema((Schema) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PostIterateRule.ARG_SCHEMA_NAME))
                .stats((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, PostIterateRule.ARG_STATS_NAME))
                .build();
    }

    /**
     * Arguments container for {@link PostIterateRule}. Contains:
     * - application
     * - schema
     * - stats
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class PostIterateRuleArguments {
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
         * description: (Long) file length in bytes
         */
        public static final String STATS_KEY_LENGTH = "length";
        /**
         * Stats:       lastModified
         * description: (Long) last time the file was updated (Java GMT)
         */
        public static final String STATS_KEY_LAST_MODIFIED = "lastModified";
        /**
         * Stats:       columnNames
         * description: (List) column names that were used during the iteration
         */
        public static final String STATS_KEY_COLUMN_NAMES = "columnNames";
        /**
         * Stats:       objectsIterated
         * description: (Long) total number of objects iterated during this run
         */
        public static final String STATS_KEY_OBJECTS_ITERATED = "objectsIterated";

        /**
         * A reference to the Application object
         */
        @Argument(name = PostIterateRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * A reference to the Schema object for the delimited file source being read
         */
        @Argument(name = PostIterateRule.ARG_SCHEMA_NAME)
        private final Schema schema;
        /**
         * A map passed by the connector of the stats for the file about to be iterated. Contains keys:
         * • fileName: (String) filename of the file about to be processed
         * • absolutePath: (String) absolute filename
         * • length: (Long) file length in bytes
         * • lastModified: (Long) last time the file was updated (Java GMT)
         */
        @Argument(name = PostIterateRule.ARG_STATS_NAME)
        private final Map<String, Object> stats;
    }
}
