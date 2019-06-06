package com.sailpoint.improved.rule.activity;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Works in conjunction with the ActivityPositionBuilder rule, as described above.
 * It, too, only applies to JDBC Activity Collectors. It uses the map created by the ActivityPositionBuilder rule to
 * build the where clause for retrieving the next set of activity data from the data source.
 * This rule is only applied if the sql statement that defines where and how activity data is read includes a
 * reference variable $(positionCondition). The ActivityConditionBuilder rule specifies the condition that is
 * substituted for that variable.
 * <p>
 * Output:
 * String value for where clause in activity lookup
 */
@Slf4j
public abstract class ActivityConditionBuilderRule
        extends AbstractJavaRuleExecutor<String, ActivityConditionBuilderRule.ActivityConditionBuilderRuleArguments> {

    /**
     * Name of config argument name
     */
    public static final String ARG_CONFIG = "config";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ActivityConditionBuilderRule.ARG_CONFIG
    );

    /**
     * Default constructor
     */
    public ActivityConditionBuilderRule() {
        super(Rule.Type.ActivityConditionBuilder.name(), ActivityConditionBuilderRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ActivityConditionBuilderRule.ActivityConditionBuilderRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ActivityConditionBuilderRuleArguments.builder()
                .config((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ActivityConditionBuilderRule.ARG_CONFIG))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - row
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ActivityConditionBuilderRuleArguments {
        /**
         * Map of values that can be used to identify where in the datasource the collector should resume activity collection
         */
        @Argument(name = ActivityConditionBuilderRule.ARG_CONFIG)
        private final Map<String, Object> config;
    }
}
