package com.sailpoint.improved.rule.miscellaneous;

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
import sailpoint.object.TaskEvent;
import sailpoint.object.TaskResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The TaskEventRule is a rule type created in IdentityIQ 6.0. It is used to inject logic at a particular stage in the
 * Task execution process; currently the only stage supported is task completion. This rule type was created to
 * allow reporting tasks to notify the requesting user when the report has been completed. When the user clicks
 * Email Me When Done on the Task Result, a TaskEvent is created with an attached TaskEventRule that sends an
 * email message to the requester when the task reaches the completion stage.
 * <p>
 * NOTE: Because custom tasks do not modify the Task Result UI and TaskEvents can only be created with a
 * connection to an in-progress TaskResult, this rule type is not currently useful for custom coding.
 * <p>
 * Output:
 * Contains key {@link TaskEvent#RULE_RETURN_TASK_RESULT} and a taskResult object modified by the rule
 * (or null if no update to taskResult is required as a result of the rule’s execution)
 */
@Slf4j
public abstract class TaskEventRule
        extends AbstractJavaRuleExecutor<Map<String, TaskResult>, TaskEventRule.TaskEventRuleArguments> {

    /**
     * Name of taskResult argument name
     */
    public static final String ARG_TASK_RESULT = "taskResult";
    /**
     * Name of event argument name
     */
    public static final String ARG_EVENT = "event";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            TaskEventRule.ARG_TASK_RESULT,
            TaskEventRule.ARG_EVENT
    );

    /**
     * Default constructor
     */
    public TaskEventRule() {
        super(Rule.Type.TaskEventRule.name(), TaskEventRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected TaskEventRule.TaskEventRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return TaskEventRuleArguments.builder()
                .taskResult((TaskResult) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TaskEventRule.ARG_TASK_RESULT))
                .event((TaskEvent) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TaskEventRule.ARG_EVENT))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - taskResult
     * - event
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class TaskEventRuleArguments {
        /**
         * Reference to the current taskResult object from the task execution
         */
        @Argument(name = TaskEventRule.ARG_TASK_RESULT)
        private final TaskResult taskResult;
        /**
         * TaskEvent object to which the rule is connected
         */
        @Argument(name = TaskEventRule.ARG_EVENT)
        private final TaskEvent event;
    }
}
