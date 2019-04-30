package com.sailpoint.improved.rule.connector;

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
import sailpoint.object.Rule;
import sailpoint.object.Schema;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Used to specify a custom basis for merging of rows from a Delimited File or JDBC
 * application. The connectors include a default merge algorithm that merges the rows based on the defined
 * merge parameters. If a MergeMaps rule is specified, it overrides the default merge operation with the rule’s
 * custom behavior.
 * A convenience method is available that performs the default merge algorithm, allowing the remainder of the
 * rule to apply customizations to that default merging. This convenience method is:
 * {@link AbstractConnector#defaultMergeMaps(Map current, Map newObject, List mergeAttrs)}
 * The sailpoint.connector.AbstractConnector class must be imported into the rule to use this method.
 * (Alternatively, since both the DelimitedFileConnector and the JDBCConnector classes extend AbstractConnector,
 * the applicable one of those classes could be imported with the method call naming that class instead.)
 * <p>
 * NOTE: Since the MergeMaps rule runs for every row or ResultSet of data from a delimited file or JDBC data
 * source, performing lengthy operations in this rule can have a negative effect on aggregation performance.
 */
@Slf4j
public abstract class MergeMapsRule
        extends AbstractJavaRuleExecutor<Map<String, Object>, MergeMapsRule.MergeMapsRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION_NAME = "application";
    /**
     * Name of schema argument name
     */
    public static final String ARG_SCHEMA_NAME = "schema";
    /**
     * Name of current argument name
     */
    public static final String ARG_CURRENT_NAME = "current";
    /**
     * Name of newObject argument name
     */
    public static final String ARG_NEW_OBJECT_NAME = "newObject";
    /**
     * Name of mergeAttrs argument name
     */
    public static final String ARG_MERGE_ATTRS_NAME = "mergeAttrs";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            MergeMapsRule.ARG_APPLICATION_NAME,
            MergeMapsRule.ARG_SCHEMA_NAME,
            MergeMapsRule.ARG_CURRENT_NAME,
            MergeMapsRule.ARG_NEW_OBJECT_NAME,
            MergeMapsRule.ARG_MERGE_ATTRS_NAME
    );

    /**
     * Default constructor
     */
    public MergeMapsRule() {
        super(Rule.Type.MergeMaps.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context for arguments parameters
     * @return argument container instance
     */
    @Override
    protected MergeMapsRule.MergeMapsRuleArguments buildContainerArguments(JavaRuleContext javaRuleContext) {
        return MergeMapsRuleArguments
                .builder()
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, MergeMapsRule.ARG_APPLICATION_NAME))
                .schema((Schema) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, MergeMapsRule.ARG_SCHEMA_NAME))
                .current((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, MergeMapsRule.ARG_CURRENT_NAME))
                .newObject((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, MergeMapsRule.ARG_NEW_OBJECT_NAME))
                .mergeAttrs((List<String>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, MergeMapsRule.ARG_MERGE_ATTRS_NAME))
                .build();
    }

    /**
     * Arguments container for {@link MergeMapsRule}. Contains:
     * - application
     * - schema
     * - stats
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class MergeMapsRuleArguments {
        /**
         * A reference to the Application object
         */
        @Argument(name = MergeMapsRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * A reference to the Schema object for the Delimited File or JDBC source being read
         */
        @Argument(name = MergeMapsRule.ARG_SCHEMA_NAME)
        private final Schema schema;
        /**
         * The current Map object
         */
        @Argument(name = MergeMapsRule.ARG_CURRENT_NAME)
        private final Map<String, Object> current;
        /**
         * The map representation of the next row that potentially needs to be merged into the current object based on mergeAttrs
         */
        @Argument(name = MergeMapsRule.ARG_NEW_OBJECT_NAME)
        private final Map<String, Object> newObject;
        /**
         * Names of attributes that need to be merged, specified as part of the application configuration
         */
        @Argument(name = MergeMapsRule.ARG_MERGE_ATTRS_NAME)
        private final List<String> mergeAttrs;

    }
}
