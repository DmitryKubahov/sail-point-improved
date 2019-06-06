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
 * Allows the installation to manipulate attributes of an Unstructured Target during a Target Aggregation
 * task if that target has previously been created. The TargetCreation rule is run when new targets are being
 * created while this one runs for existing targets. This rule type was introduced in version 7.1.
 * <p>
 * Output:
 * The Target object as modified by the rule
 */
@Slf4j
public abstract class TargetRefreshRule
        extends AbstractJavaRuleExecutor<Target, TargetRefreshRule.TargetRefreshRuleArguments> {

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
            TargetRefreshRule.ARG_APPLICATION,
            TargetRefreshRule.ARG_TARGET,
            TargetRefreshRule.ARG_TARGET_SOURCE
    );

    /**
     * Default constructor
     */
    public TargetRefreshRule() {
        super(Rule.Type.TargetRefresh.name(), TargetRefreshRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected TargetRefreshRule.TargetRefreshRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return TargetRefreshRuleArguments.builder()
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetRefreshRule.ARG_APPLICATION))
                .target((Target) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetRefreshRule.ARG_TARGET))
                .targetSource((Target) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TargetRefreshRule.ARG_TARGET_SOURCE))
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
    public static class TargetRefreshRuleArguments {
        /**
         * Reference to the application object on which the targets exist
         */
        @Argument(name = TargetRefreshRule.ARG_APPLICATION)
        private final Application application;
        /**
         * Reference to the collected target
         */
        @Argument(name = TargetRefreshRule.ARG_TARGET)
        private final Target target;
        /**
         * Reference to the config that drives the collection process
         */
        @Argument(name = TargetRefreshRule.ARG_TARGET_SOURCE)
        private final Target targetSource;
    }
}
