package com.sailpoint.improved.rule.activity;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.ActivityDataSource;
import sailpoint.object.Application;
import sailpoint.object.ApplicationActivity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Used to correlate the activity record to a user in IdentityIQ; in other words, this rule associates the
 * activity record to the Identity who performed the activity.
 * <p>
 * Output:
 * Link object
 */
@Slf4j
public abstract class ActivityCorrelationRule
        extends AbstractJavaRuleExecutor<Link, ActivityCorrelationRule.ActivityCorrelationRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of datasource argument name
     */
    public static final String ARG_DATA_SOURCE = "datasource";
    /**
     * Name of activity argument name
     */
    public static final String ARG_ACTIVITY = "activity";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ActivityCorrelationRule.ARG_APPLICATION,
            ActivityCorrelationRule.ARG_DATA_SOURCE,
            ActivityCorrelationRule.ARG_ACTIVITY

    );

    /**
     * Default constructor
     */
    public ActivityCorrelationRule() {
        super(Rule.Type.ActivityCorrelation.name(), ActivityCorrelationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ActivityCorrelationRule.ActivityCorrelationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ActivityCorrelationRuleArguments.builder()
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ActivityCorrelationRule.ARG_APPLICATION))
                .dataSource((ActivityDataSource) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ActivityCorrelationRule.ARG_DATA_SOURCE))
                .activity((ApplicationActivity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ActivityCorrelationRule.ARG_ACTIVITY))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - application
     * - datasource
     * - activity
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ActivityCorrelationRuleArguments {
        /**
         * Reference to the application on which the activity occurred
         */
        @Argument(name = ActivityCorrelationRule.ARG_APPLICATION)
        private final Application application;
        /**
         * Reference to the datasource from which the activity was collected
         */
        @Argument(name = ActivityCorrelationRule.ARG_DATA_SOURCE)
        private final ActivityDataSource dataSource;
        /**
         * Reference to the ApplicationActivity object representing the collected activity record
         */
        @Argument(name = ActivityCorrelationRule.ARG_ACTIVITY)
        private final ApplicationActivity activity;
    }
}
