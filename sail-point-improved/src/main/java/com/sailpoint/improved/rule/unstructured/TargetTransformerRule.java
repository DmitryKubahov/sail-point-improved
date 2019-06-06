package com.sailpoint.improved.rule.unstructured;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Target;

import java.util.Arrays;
import java.util.List;

/**
 * Seldom-used rule for manipulating a target object during target collection. It is similar to a Build
 * Map rule for a connector schema, but applies specifically to target collection/manipulation and is only available
 * in the WindowsTargetCollector and the original (pre-7.2) SecurityIQTargetCollector.
 * <p>
 * Output:
 * The Target object as modified by the rule
 */
@Slf4j
public abstract class TargetTransformerRule
        extends AbstractJavaRuleExecutor<Target, TargetTransformerRule.TargetTransformerRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of target argument name
     */
    public static final String ARG_TARGET = "target";
    /**
     * Name of targetSource argument name
     */
    public static final String ARG_TARGET_SOURCE = "targetSource";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            TargetTransformerRule.ARG_APPLICATION,
            TargetTransformerRule.ARG_TARGET,
            TargetTransformerRule.ARG_TARGET_SOURCE
    );

    /**
     * Default constructor
     */
    public TargetTransformerRule() {
        super(Rule.Type.TargetTransformer.name(), TargetTransformerRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected TargetTransformerRule.TargetTransformerRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return TargetTransformerRuleArguments.builder()
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetTransformerRule.ARG_APPLICATION))
                .target((Target) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetTransformerRule.ARG_TARGET))
                .targetSource((Target) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetTransformerRule.ARG_TARGET_SOURCE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - application
     * - target
     * - target source
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class TargetTransformerRuleArguments {
        /**
         * Reference to the target collector from which the targets are being read
         */
        @Argument(name = TargetTransformerRule.ARG_APPLICATION)
        private final Application application;
        /**
         * Reference to the collected target
         */
        @Argument(name = TargetTransformerRule.ARG_TARGET)
        private final Target target;
        /**
         * Reference to the config that drives the collection process
         */
        @Argument(name = TargetTransformerRule.ARG_TARGET_SOURCE)
        private final Target targetSource;
    }
}
