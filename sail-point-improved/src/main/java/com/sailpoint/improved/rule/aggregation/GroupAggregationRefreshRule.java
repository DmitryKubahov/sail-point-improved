package com.sailpoint.improved.rule.aggregation;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ManagedAttribute;
import sailpoint.object.ResourceObject;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Runs during an Account Group Aggregation task. It allows custom manipulation of group attributes
 * while the group is being refreshed (on both create and update).
 * <p>
 * NOTE: This rule runs for every group object involved in the aggregation task, so time-intensive operations
 * performed in it can have a negative impact on task performance.
 * <p>
 * Outputs:
 * The refreshed (modified) account group object
 */
@Slf4j
public abstract class GroupAggregationRefreshRule
        extends AbstractJavaRuleExecutor<Map<String, Object>, GroupAggregationRefreshRule.GroupAggregationRefreshRuleArguments> {

    /**
     * Name of environment argument name
     */
    public static final String ARG_ENVIRONMENT_NAME = "environment";
    /**
     * Name of obj argument name
     */
    public static final String ARG_OBJECT_NAME = "obj";
    /**
     * Name of accountGroup argument name
     */
    public static final String ARG_ACCOUNT_GROUP_NAME = "accountGroup";
    /**
     * Name of groupApplication argument name
     */
    public static final String ARG_GROUP_APPLICATION_NAME = "groupApplication";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            GroupAggregationRefreshRule.ARG_ENVIRONMENT_NAME,
            GroupAggregationRefreshRule.ARG_OBJECT_NAME,
            GroupAggregationRefreshRule.ARG_ACCOUNT_GROUP_NAME,
            GroupAggregationRefreshRule.ARG_GROUP_APPLICATION_NAME
    );

    /**
     * Default constructor
     */
    public GroupAggregationRefreshRule() {
        super(Rule.Type.GroupAggregationRefresh.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected GroupAggregationRefreshRule.GroupAggregationRefreshRuleArguments buildContainerArguments(
            JavaRuleContext javaRuleContext) {
        return GroupAggregationRefreshRuleArguments
                .builder()
                .environment((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, GroupAggregationRefreshRule.ARG_ENVIRONMENT_NAME))
                .object((ResourceObject) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, GroupAggregationRefreshRule.ARG_OBJECT_NAME))
                .accountGroup((ManagedAttribute) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, GroupAggregationRefreshRule.ARG_ACCOUNT_GROUP_NAME))
                .groupApplication((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, GroupAggregationRefreshRule.ARG_GROUP_APPLICATION_NAME))
                .build();
    }

    /**
     * Arguments container for {@link GroupAggregationRefreshRule}. Contains:
     * - environment
     * - obj
     * - accountGroup
     * - groupApplication
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class GroupAggregationRefreshRuleArguments {
        /**
         * Map of arguments passed to the aggregation task
         */
        @Argument(name = GroupAggregationRefreshRule.ARG_ENVIRONMENT_NAME)
        private final Map<String, Object> environment;
        /**
         * Reference to the resourceObject from the application
         */
        @Argument(name = GroupAggregationRefreshRule.ARG_OBJECT_NAME)
        private final ResourceObject object;
        /**
         * Reference to the account group being refreshed
         */
        @Argument(name = GroupAggregationRefreshRule.ARG_ACCOUNT_GROUP_NAME)
        private final ManagedAttribute accountGroup;
        /**
         * Reference to the application being aggregated
         */
        @Argument(name = GroupAggregationRefreshRule.ARG_GROUP_APPLICATION_NAME)
        private final Application groupApplication;
    }
}
