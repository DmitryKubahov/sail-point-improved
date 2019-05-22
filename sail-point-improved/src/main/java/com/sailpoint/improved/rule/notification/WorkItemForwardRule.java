package com.sailpoint.improved.rule.notification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.WorkItem;

import java.util.Arrays;
import java.util.List;

/**
 * Examines a WorkItem and determines whether or not it needs to be forwarded to a
 * new owner for further analysis or action. Only one WorkItemForward rule can be in use at any time for an
 * installation; it is selected in the system configuration and is called every time a WorkItem is opened and any
 * time it is forwarded through the user interface.
 * <p>
 * Output:
 * Identity object or name of Identity object who should receive the workItem
 */
@Slf4j
public abstract class WorkItemForwardRule
        extends AbstractJavaRuleExecutor<Identity, WorkItemForwardRule.WorkItemForwardRuleArguments> {

    /**
     * Name of item argument name
     */
    public static final String ARG_ITEM = "item";
    /**
     * Name of owner argument name
     */
    public static final String ARG_OWNER = "owner";
    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            WorkItemForwardRule.ARG_ITEM,
            WorkItemForwardRule.ARG_OWNER,
            WorkItemForwardRule.ARG_IDENTITY
    );

    /**
     * Default constructor
     */
    public WorkItemForwardRule() {
        super(Rule.Type.WorkItemForward.name(), WorkItemForwardRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected WorkItemForwardRule.WorkItemForwardRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return WorkItemForwardRuleArguments.builder()
                .item((WorkItem) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, WorkItemForwardRule.ARG_ITEM))
                .owner((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, WorkItemForwardRule.ARG_OWNER))
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, WorkItemForwardRule.ARG_IDENTITY))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - item
     * - owner
     * - identity
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class WorkItemForwardRuleArguments {
        /**
         * Reference to the workItem being opened (some workItem arguments may not yet be set)
         */
        @Argument(name = WorkItemForwardRule.ARG_ITEM)
        private final WorkItem item;
        /**
         * Reference to the Identity who currently owns the work item
         */
        @Argument(name = WorkItemForwardRule.ARG_OWNER)
        private final Identity owner;
        /**
         * Reference to the same Identity object as owner (provided for backward compatibility to older versions of this rule)
         */
        @Argument(name = WorkItemForwardRule.ARG_IDENTITY)
        private final Identity identity;
    }
}
