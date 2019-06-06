package com.sailpoint.improved.rule.unstructured;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.Correlator;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Target;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Determines how the permission data gathered through unstructured configs is correlated to a link or
 * group in IdentityIQ. The correlation is done by IdentityIQ based on the attribute name and value returned from
 * this rule.
 * <p>
 * Output:
 * Map containing the appropriate group or link attribute name and value to correlate the discovered permission to an entity:
 * If permission belongs to a group:
 * - {@link Correlator#RULE_RETURN_GROUP_ATTRIBUTE},”[attribute name]”
 * - {@link Correlator#RULE_RETURN_GROUP_ATTRIBUTE_VALUE},”[value that identifies the group]”
 * or
 * - {@link Correlator#RULE_RETURN_GROUP},”[ManagedAttribute object for the group]”
 * <p>
 * If permission belongs an account:
 * - {@link Correlator#RULE_RETURN_LINK_IDENTITY},”[GUID for the correlated Link]”
 * or
 * - {@link Correlator#RULE_RETURN_LINK_DISPLAYNAME},”[display name attribute value for the Link]”
 * or
 * - {@link Correlator#RULE_RETURN_LINK_ATTRIBUTE},”[attribute name]”
 * - {@link Correlator#RULE_RETURN_LINK_ATTRIBUTE_VALUE},”[value that identifies the account]”
 * NOTE: If Link is on an application that includes Instances, a “linkInstance” attribute should be included in the map to
 * specify the application instance to match to as well.
 */
@Slf4j
public abstract class TargetCorrelationRule
        extends AbstractJavaRuleExecutor<Map<String, Object>, TargetCorrelationRule.TargetCorrelationRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of nativeId argument name
     */
    public static final String ARG_NATIVE_ID = "nativeId";
    /**
     * Name of target argument name
     */
    public static final String ARG_TARGET = "target";
    /**
     * Name of targetSource argument name
     */
    public static final String ARG_TARGET_SOURCE = "targetSource";
    /**
     * Name of isGroup argument name
     */
    public static final String ARG_IS_GROUP = "isGroup";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            TargetCorrelationRule.ARG_APPLICATION,
            TargetCorrelationRule.ARG_NATIVE_ID,
            TargetCorrelationRule.ARG_TARGET,
            TargetCorrelationRule.ARG_TARGET_SOURCE,
            TargetCorrelationRule.ARG_IS_GROUP
    );

    /**
     * Default constructor
     */
    public TargetCorrelationRule() {
        super(Rule.Type.TargetCorrelation.name(), TargetCorrelationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected TargetCorrelationRule.TargetCorrelationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return TargetCorrelationRuleArguments.builder()
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetCorrelationRule.ARG_APPLICATION))
                .nativeId((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetCorrelationRule.ARG_NATIVE_ID))
                .target((Target) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetCorrelationRule.ARG_TARGET))
                .targetSource((Target) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetCorrelationRule.ARG_TARGET_SOURCE))
                .isGroup((Boolean) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetCorrelationRule.ARG_IS_GROUP))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - application
     * - native id
     * - target
     * - target source
     * - is group
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class TargetCorrelationRuleArguments {
        /**
         * Reference to the application object on which the targets exist
         */
        @Argument(name = TargetCorrelationRule.ARG_APPLICATION)
        private final Application application;
        /**
         * The group or user’s native identity on the target
         */
        @Argument(name = TargetCorrelationRule.ARG_NATIVE_ID)
        private final String nativeId;
        /**
         * Reference to the collected target
         */
        @Argument(name = TargetCorrelationRule.ARG_TARGET)
        private final Target target;
        /**
         * Reference to the config that drives the collection process
         */
        @Argument(name = TargetCorrelationRule.ARG_TARGET_SOURCE)
        private final Target targetSource;
        /**
         * Flag indicating whether the target represent a group (as opposed to a user account)
         */
        @Argument(name = TargetCorrelationRule.ARG_IS_GROUP)
        private final boolean isGroup;
    }
}
