package com.sailpoint.improved.rule.mapping;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Can be used as the source for an Account Mapping activity, promoting account attributes from Links
 * during aggregation. LinkAttribute rules can be specified as application-specific rules or as a global rule.
 * <p>
 * Output:
 * Contains the value for the account attribute
 */
@Slf4j
public abstract class LinkAttributeRule
        extends AbstractJavaRuleExecutor<Object, LinkAttributeRule.LinkAttributeRuleArguments> {

    /**
     * Name of environment argument name
     */
    public static final String ARG_ENVIRONMENT = "environment";
    /**
     * Name of link argument name
     */
    public static final String ARG_LINK = "link";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            LinkAttributeRule.ARG_ENVIRONMENT,
            LinkAttributeRule.ARG_LINK
    );

    /**
     * Default constructor
     */
    public LinkAttributeRule() {
        super(Rule.Type.LinkAttribute.name(), LinkAttributeRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected LinkAttributeRule.LinkAttributeRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return LinkAttributeRuleArguments.builder()
                .environment((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, LinkAttributeRule.ARG_ENVIRONMENT))
                .link((Link) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, LinkAttributeRule.ARG_LINK))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - environment
     * - link
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class LinkAttributeRuleArguments {
        /**
         * Map of the task arguments from the aggregation task
         */
        @Argument(name = LinkAttributeRule.ARG_ENVIRONMENT)
        private final Map<String, Object> environment;
        /**
         * Reference to the link object from which the account attribute value is being extracted/manipulated
         */
        @Argument(name = LinkAttributeRule.ARG_LINK)
        private final Link link;
    }
}
