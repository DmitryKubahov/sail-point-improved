package com.sailpoint.improved.rule.mapping;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.AttributeDefinition;
import sailpoint.object.AttributeSource;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * When identity attribute mapping depends on multiple application attributes or other complex evaluations, an
 * IdentityAttribute rule can be specified to control that mapping. IdentityAttribute rules can be specified as
 * application-specific or global rules.
 * <p>
 * Output:
 * Value to record for the attribute
 */
@Slf4j
public abstract class IdentityAttributeRule
        extends AbstractJavaRuleExecutor<Object, IdentityAttributeRule.IdentityAttributeRuleArguments> {

    /**
     * Name of environment argument name
     */
    public static final String ARG_ENVIRONMENT = "environment";
    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";
    /**
     * Name of attributeDefinition argument name
     */
    public static final String ARG_ATTRIBUTE_DEFINITION = "attributeDefinition";
    /**
     * Name of link argument name
     */
    public static final String ARG_LINK = "link";
    /**
     * Name of attributeSource argument name
     */
    public static final String ARG_ATTRIBUTE_SOURCE = "attributeSource";
    /**
     * Name of oldValue argument name
     */
    public static final String ARG_OLD_VALUE = "oldValue";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            IdentityAttributeRule.ARG_ENVIRONMENT,
            IdentityAttributeRule.ARG_IDENTITY,
            IdentityAttributeRule.ARG_ATTRIBUTE_DEFINITION,
            IdentityAttributeRule.ARG_ATTRIBUTE_SOURCE
    );

    /**
     * Default constructor
     */
    public IdentityAttributeRule() {
        super(Rule.Type.IdentityAttribute.name(), IdentityAttributeRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected IdentityAttributeRule.IdentityAttributeRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return IdentityAttributeRuleArguments.builder()
                .environment((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IdentityAttributeRule.ARG_ENVIRONMENT))
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IdentityAttributeRule.ARG_IDENTITY))
                .attributeDefinition((AttributeDefinition) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IdentityAttributeRule.ARG_ATTRIBUTE_DEFINITION))
                .link((Link) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IdentityAttributeRule.ARG_LINK))
                .attributeSource((AttributeSource) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IdentityAttributeRule.ARG_ATTRIBUTE_SOURCE))
                .oldValue(JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IdentityAttributeRule.ARG_OLD_VALUE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - environment
     * - identity
     * - attributeDefinition
     * - link
     * - attributeSource
     * - oldValue
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class IdentityAttributeRuleArguments {
        /**
         * Map of arguments to the aggregation or refresh task that is executing the rule in attribute promotion
         */
        @Argument(name = IdentityAttributeRule.ARG_ENVIRONMENT)
        private final Map<String, Object> environment;
        /**
         * Reference to identity object that represents the user being aggregated/refreshed
         */
        @Argument(name = IdentityAttributeRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Reference to the attributeDefinition object for this attribute
         */
        @Argument(name = IdentityAttributeRule.ARG_ATTRIBUTE_DEFINITION)
        private final AttributeDefinition attributeDefinition;
        /**
         * Only included as an argument for application rules, not global rules
         */
        @Argument(name = IdentityAttributeRule.ARG_LINK)
        private final Link link;
        /**
         * Attribute source definition (see AttributeSource object XML above for an example)
         */
        @Argument(name = IdentityAttributeRule.ARG_ATTRIBUTE_SOURCE)
        private final AttributeSource attributeSource;
        /**
         * Attribute value of target identity attribute before the rule runs
         */
        @Argument(name = IdentityAttributeRule.ARG_OLD_VALUE)
        private final Object oldValue;
    }
}
