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
 * Called when a Target is created, allowing manipulation of the target before it is saved. It is most
 * commonly used to filter out unwanted targets before they are created (e.g. objects that have only “system
 * access” and therefore will not correlate to anything or targets that do not need to be tracked because they are
 * unrestricted).
 * <p>
 * Output:
 * The Target object as modified by the rule
 * NOTE: The rule can modify the target that is passed in as a parameter. If the rule returns a Target object, that
 * object will replace the target passed to the rule. If the rule returns anything else (including no return value), the
 * target passed to the rule is used in subsequent processing. As a result, if the target was modified directly by the
 * rule, those modifications are applied even if the rule does not explicitly return it.
 */
@Slf4j
public abstract class TargetCreationRule
        extends AbstractJavaRuleExecutor<Target, TargetCreationRule.TargetCreationRuleArguments> {

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
            TargetCreationRule.ARG_APPLICATION,
            TargetCreationRule.ARG_TARGET,
            TargetCreationRule.ARG_TARGET_SOURCE
    );

    /**
     * Default constructor
     */
    public TargetCreationRule() {
        super(Rule.Type.TargetCreation.name(), TargetCreationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected TargetCreationRule.TargetCreationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return TargetCreationRuleArguments.builder()
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetCreationRule.ARG_APPLICATION))
                .target((Target) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetCreationRule.ARG_TARGET))
                .targetSource((Target) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetCreationRule.ARG_TARGET_SOURCE))
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
    public static class TargetCreationRuleArguments {
        /**
         * Reference to the Application object that owns the Target
         */
        @Argument(name = TargetCreationRule.ARG_APPLICATION)
        private final Application application;
        /**
         * Reference to the Target object being created
         */
        @Argument(name = TargetCreationRule.ARG_TARGET)
        private final Target target;
        /**
         * Source of the configuration for the collector
         */
        @Argument(name = TargetCreationRule.ARG_TARGET_SOURCE)
        private final Target targetSource;
    }
}
