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

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Applies only to JDBC Activity Collectors. It runs at the end of the activity-data-
 * gathering process and uses the current position in the activity collector resultSet to build a Map<String,String>
 * that can be saved to the SailPoint database. This map will be retrieved and passed to the
 * ActivityConditionBuilder rule to build the where clause in the next incremental call to this collector. These two
 * rules, together, function as a placeholder for activity collection to identify which records in the datasource have
 * already been collected by IdentityIQ and which still need to be read.
 * <p>
 * Output:
 * Map that can be used to build the where clause for the next call to the activity collector
 */
@Slf4j
public abstract class ActivityPositionBuilderRule
        extends AbstractJavaRuleExecutor<Map<String, Object>, ActivityPositionBuilderRule.ActivityPositionBuilderRuleArguments> {

    /**
     * Name of row argument name
     */
    public static final String ARG_ROW = "row";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ActivityPositionBuilderRule.ARG_ROW
    );

    /**
     * Default constructor
     */
    public ActivityPositionBuilderRule() {
        super(Rule.Type.ActivityPositionBuilder.name(), ActivityPositionBuilderRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ActivityPositionBuilderRule.ActivityPositionBuilderRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ActivityPositionBuilderRuleArguments.builder()
                .row((ResultSet) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ActivityPositionBuilderRule.ARG_ROW))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - row
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ActivityPositionBuilderRuleArguments {
        /**
         * Current position in the result set
         */
        @Argument(name = ActivityPositionBuilderRule.ARG_ROW)
        private final ResultSet row;
    }
}
