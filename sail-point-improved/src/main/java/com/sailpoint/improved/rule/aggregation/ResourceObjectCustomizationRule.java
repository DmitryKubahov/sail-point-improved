package com.sailpoint.improved.rule.aggregation;

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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Runs prior to any other aggregation rule to customize the resource object
 * provided by the connector before aggregation begins. Connectors that provide a transformation rule may not
 * need to use a ResourceObjectCustomization rule, since the transformation rule can modify the ResourceObject
 * as needed. However, many connectors directly provide a resource object, without the hooks for processing the
 * data through rules like a transformation rule, so this rule allows customization of the resource object before
 * IdentityIQ attempts to correlate the object to an Identity.
 * <p>
 * NOTE: Since the ResourceObjectCustomization rule runs for every ResourceObject provided by the connector,
 * time-intensive operations performed in it can have a negative impact on task performance. It runs even when
 * optimized aggregation has been specified, so customizations made by this rule can impact the optimization
 * decisions for the ResourceObject.
 * <p>
 * Outputs:
 * Even though the ResourceObject is passed to the rule where it can be modified directly, it must be
 * returned from the rule for its changes to be used by the rest of the aggregation process. Otherwise, changes
 * made to it inside the rule are not transferred back to the rest of the process.
 */
@Slf4j
public abstract class ResourceObjectCustomizationRule
        extends AbstractJavaRuleExecutor<ResourceObject, ResourceObjectCustomizationRule.ResourceObjectCustomizationRuleArguments> {

    /**
     * Name of object argument name
     */
    public static final String ARG_OBJECT = "object";
    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of connector argument name
     */
    public static final String ARG_CONNECTOR = "connector";
    /**
     * Name of state argument name
     */
    public static final String ARG_STATE = "state";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ResourceObjectCustomizationRule.ARG_OBJECT,
            ResourceObjectCustomizationRule.ARG_APPLICATION,
            ResourceObjectCustomizationRule.ARG_CONNECTOR,
            ResourceObjectCustomizationRule.ARG_STATE
    );

    /**
     * Default constructor
     */
    public ResourceObjectCustomizationRule() {
        super(Rule.Type.ResourceObjectCustomization.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ResourceObjectCustomizationRule.ResourceObjectCustomizationRuleArguments buildContainerArguments(
            JavaRuleContext javaRuleContext) {
        return ResourceObjectCustomizationRuleArguments
                .builder()
                .object((ResourceObject) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, ResourceObjectCustomizationRule.ARG_OBJECT))
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, ResourceObjectCustomizationRule.ARG_APPLICATION))
                .connector((AbstractConnector) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, ResourceObjectCustomizationRule.ARG_CONNECTOR))
                .state((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, ResourceObjectCustomizationRule.ARG_STATE))
                .build();
    }

    /**
     * Arguments container for {@link ResourceObjectCustomizationRule}. Contains:
     * - object
     * - application
     * - connector
     * - state
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ResourceObjectCustomizationRuleArguments {
        /**
         * A reference to the resource object built by the connector
         */
        @Argument(name = ResourceObjectCustomizationRule.ARG_OBJECT)
        private final ResourceObject object;
        /**
         * A reference to the Application object
         */
        @Argument(name = ResourceObjectCustomizationRule.ARG_APPLICATION)
        private final Application application;
        /**
         * A reference to the Connector object used by this application
         */
        @Argument(name = ResourceObjectCustomizationRule.ARG_CONNECTOR)
        private final AbstractConnector connector;
        /**
         * A Map that can be used to store and share data between executions of this rule during a single aggregation run
         */
        @Argument(name = ResourceObjectCustomizationRule.ARG_STATE)
        private final Map<String, Object> state;
    }
}
