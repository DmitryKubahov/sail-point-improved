package com.sailpoint.improved.rule.workflow;

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
import sailpoint.object.WorkItem;
import sailpoint.object.Workflow;
import sailpoint.workflow.WorkflowContext;
import sailpoint.workflow.WorkflowHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Returns an “object” which can be any value required for the functionality that invokes the rule. Workflow rules are used for
 * initializing variables, controlling transitions between steps, and even performing the action within steps.
 * <p>
 * NOTE: Though they are not explicitly named in the rule signature, all workflow arguments and process variables
 * are automatically available to all workflow rules.
 * <p>
 * Output:
 * Value to be returned from the rule (depends on the rule’s usage)
 */
@Slf4j
public abstract class WorkflowRule
        extends AbstractJavaRuleExecutor<Object, WorkflowRule.WorkflowRuleArguments> {

    /**
     * Name of wfcontext argument name
     */
    public static final String ARG_WORKFLOW_CONTEXT = "wfcontext";
    /**
     * Name of handler argument name
     */
    public static final String ARG_HANDLER = "handler";
    /**
     * Name of workflow argument name
     */
    public static final String ARG_WORKFLOW = "workflow";
    /**
     * Name of step argument name
     */
    public static final String ARG_STEP = "step";
    /**
     * Name of approval argument name
     */
    public static final String ARG_APPROVAL = "approval";
    /**
     * Name of item argument name
     */
    public static final String ARG_ITEM = "item";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            WorkflowRule.ARG_WORKFLOW_CONTEXT,
            WorkflowRule.ARG_HANDLER,
            WorkflowRule.ARG_WORKFLOW
    );

    /**
     * Default constructor
     */
    public WorkflowRule() {
        super(Rule.Type.Workflow.name(), WorkflowRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected WorkflowRule.WorkflowRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return WorkflowRuleArguments.builder()
                .workflowContext((WorkflowContext) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, WorkflowRule.ARG_WORKFLOW_CONTEXT))
                .handler((WorkflowHandler) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, WorkflowRule.ARG_HANDLER))
                .workflow((Workflow) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, WorkflowRule.ARG_WORKFLOW))
                .step((Workflow.Step) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, WorkflowRule.ARG_STEP))
                .approval((Workflow.Approval) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, WorkflowRule.ARG_APPROVAL))
                .item((WorkItem) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, WorkflowRule.ARG_ITEM))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - wfcontext
     * - handler
     * - workflow
     * - step
     * - approval
     * - item
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class WorkflowRuleArguments {
        /**
         * Reference to the current workflowContext
         */
        @Argument(name = WorkflowRule.ARG_WORKFLOW_CONTEXT)
        private final WorkflowContext workflowContext;
        /**
         * Workflow handler connected to the current workflowContext
         */
        @Argument(name = WorkflowRule.ARG_HANDLER)
        private final WorkflowHandler handler;
        /**
         * Current workflow definition
         */
        @Argument(name = WorkflowRule.ARG_WORKFLOW)
        private final Workflow workflow;
        /**
         * Current step in the workflow
         */
        @Argument(name = WorkflowRule.ARG_STEP)
        private final Workflow.Step step;
        /**
         * Current approval being processed
         */
        @Argument(name = WorkflowRule.ARG_APPROVAL)
        private final Workflow.Approval approval;
        /**
         * Work item being processed
         */
        @Argument(name = WorkflowRule.ARG_ITEM)
        private final WorkItem item;
    }
}
