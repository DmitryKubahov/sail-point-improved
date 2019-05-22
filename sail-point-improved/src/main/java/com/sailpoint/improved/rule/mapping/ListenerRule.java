package com.sailpoint.improved.rule.mapping;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractNoneOutputJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ObjectAttribute;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Triggered when the value of an attribute changes and performs logic in response to that value
 * change. The rule is called during aggregation or refresh when the attribute value changes.
 * <p>
 * Output:
 * None; the rule performs actions that are outside of the attribute modification process so IdentityIQ
 * does not expect or act upon a return value from this rule.
 */
@Slf4j
public abstract class ListenerRule extends AbstractNoneOutputJavaRuleExecutor<ListenerRule.ListenerRuleArguments> {

    /**
     * Name of environment argument name
     */
    public static final String ARG_ENVIRONMENT = "environment";
    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";
    /**
     * Name of object argument name
     */
    public static final String ARG_OBJECT = "object";
    /**
     * Name of attributeDefinition argument name
     */
    public static final String ARG_ATTRIBUTE_DEFINITION = "attributeDefinition";
    /**
     * Name of attributeName argument name
     */
    public static final String ARG_ATTRIBUTE_NAME = "attributeName";
    /**
     * Name of oldValue argument name
     */
    public static final String ARG_OLD_VALUE = "oldValue";
    /**
     * Name of newValue argument name
     */
    public static final String ARG_NEW_VALUE = "newValue";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ListenerRule.ARG_ENVIRONMENT,
            ListenerRule.ARG_IDENTITY,
            ListenerRule.ARG_OBJECT,
            ListenerRule.ARG_ATTRIBUTE_DEFINITION,
            ListenerRule.ARG_ATTRIBUTE_NAME
    );

    /**
     * Default constructor
     */
    public ListenerRule() {
        super(Rule.Type.Listener.name(), ListenerRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ListenerRule.ListenerRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ListenerRuleArguments.builder()
                .environment((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ListenerRule.ARG_ENVIRONMENT))
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ListenerRule.ARG_IDENTITY))
                .object((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ListenerRule.ARG_OBJECT))
                .attributeDefinition((ObjectAttribute) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ListenerRule.ARG_ATTRIBUTE_DEFINITION))
                .attributeName((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ListenerRule.ARG_ATTRIBUTE_NAME))
                .oldValue(JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext, ListenerRule.ARG_OLD_VALUE))
                .newValue(JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext, ListenerRule.ARG_NEW_VALUE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - environment
     * - identity
     * - object
     * - attributeDefinition
     * - attributeName
     * - oldValue
     * - newValue
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ListenerRuleArguments {
        /**
         * Task arguments for the task that invoked the rule
         */
        @Argument(name = ListenerRule.ARG_ENVIRONMENT)
        private final Map<String, Object> environment;
        /**
         * Reference to the Identity to whom the attribute applies
         */
        @Argument(name = ListenerRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Reference to the Identity to whom the attribute applies; passed in both variables for compatibility with generic rules
         */
        @Argument(name = ListenerRule.ARG_OBJECT)
        private final Identity object;
        /**
         * Definition of the ObjectAttribute
         */
        @Argument(name = ListenerRule.ARG_ATTRIBUTE_DEFINITION)
        private final ObjectAttribute attributeDefinition;
        /**
         * Name of the ObjectAttribute
         */
        @Argument(name = ListenerRule.ARG_ATTRIBUTE_NAME)
        private final String attributeName;
        /**
         * Original (pre-change) value of the attribute
         */
        @Argument(name = ListenerRule.ARG_OLD_VALUE)
        private final Object oldValue;
        /**
         * New (post-change) value of the attribute
         */
        @Argument(name = ListenerRule.ARG_NEW_VALUE)
        private final Object newValue;
    }
}
