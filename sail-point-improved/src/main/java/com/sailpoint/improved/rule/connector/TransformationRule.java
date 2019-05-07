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
import sailpoint.object.ResourceObject;
import sailpoint.object.Rule;
import sailpoint.object.Schema;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This rule is run for every account or group read from a delimited file or JDBC application. It runs after the
 * BuildMap rule (and MergeMaps, if applicable) and is used to control the transformation of each map into a
 * ResourceObject. Connectors must get data into this ResourceObject format before it can be processed by the
 * aggregator and the aggregation rules.
 * If no transformation rule is specified, the transformation is performed through the defaultTransformObject
 * method in the AbstractConnector class. This method is available to the rule as a convenience method and can
 * be used to do the basic conversion, allowing the rule to do further customization on the ResourceObject in the
 * remainder of its logic. The convenience method is:
 * {@link AbstractConnector#defaultTransformObject(Schema schema, Map object)}
 * The {@link AbstractConnector} class must be imported into the rule to use this method.
 * <p>
 * NOTE: Since the Transformation rule runs for every map created from the source data, time-intensive
 * operations performed in it can have a negative impact on aggregation performance.
 */
@Slf4j
public abstract class TransformationRule
        extends AbstractJavaRuleExecutor<ResourceObject, TransformationRule.TransformationRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION_NAME = "application";
    /**
     * Name of schema argument name
     */
    public static final String ARG_SCHEMA_NAME = "schema";
    /**
     * Name of object argument name
     */
    public static final String ARG_OBJECT_NAME = "object";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            TransformationRule.ARG_APPLICATION_NAME,
            TransformationRule.ARG_SCHEMA_NAME,
            TransformationRule.ARG_OBJECT_NAME
    );

    /**
     * Default constructor
     */
    public TransformationRule() {
        super(Rule.Type.Transformation.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context for arguments parameters
     * @return argument container instance
     */
    @Override
    protected TransformationRule.TransformationRuleArguments buildContainerArguments(JavaRuleContext javaRuleContext) {
        return TransformationRuleArguments
                .builder()
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, TransformationRule.ARG_APPLICATION_NAME))
                .schema((Schema) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, TransformationRule.ARG_SCHEMA_NAME))
                .object((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, TransformationRule.ARG_OBJECT_NAME))
                .build();
    }

    /**
     * Arguments container for {@link TransformationRule}. Contains:
     * - application
     * - schema
     * - object
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class TransformationRuleArguments {
        /**
         * A reference to the Application object
         */
        @Argument(name = TransformationRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * A reference to the Schema object for the Delimited File source being read
         */
        @Argument(name = TransformationRule.ARG_SCHEMA_NAME)
        private final Schema schema;
        /**
         * The incoming Map object
         */
        @Argument(name = TransformationRule.ARG_OBJECT_NAME)
        private final Map<String, Object> object;
    }
}
