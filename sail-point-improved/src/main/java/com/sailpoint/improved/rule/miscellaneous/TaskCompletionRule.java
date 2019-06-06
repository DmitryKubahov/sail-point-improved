package com.sailpoint.improved.rule.miscellaneous;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractNoneOutputJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.TaskResult;

import java.util.Arrays;
import java.util.List;

/**
 * The TaskCompletion rule is a rule type created in IdentityIQ 6.3 to support sending an email message to a
 * specified recipient when task execution completes (either in all cases or with an error condition or a warning
 * condition). Task notification is a new feature in version 6.3, and the logic for handling the notification resides in
 * the Task Completion rule specified for the installation. A default rule, called Task Completion Email Rule, ships
 * with the product and contains all the logic necessary to send an email to the recipient designated in the UI, using
 * the email template specified in the UI. Task completion notification can be configured at the task level or at the
 * system level; the task-level configuration takes precedence over the system-level configuration.
 * <p>
 * NOTE: Many customers will never change this rule or create another rule of this type. Only one can be used in
 * each installation, and the provided rule contains the logic most customers will want to use to manage task
 * completion notifications.
 * <p>
 * Output:
 * None; the rule’s logic is intended to send an email notification and return nothing to the system.
 */
@Slf4j
public abstract class TaskCompletionRule
        extends AbstractNoneOutputJavaRuleExecutor<TaskCompletionRule.TaskCompletionRuleArguments> {

    /**
     * Name of taskResult argument name
     */
    public static final String ARG_TASK_RESULT = "taskResult";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            TaskCompletionRule.ARG_TASK_RESULT
    );

    /**
     * Default constructor
     */
    public TaskCompletionRule() {
        super(Rule.Type.TaskCompletion.name(), TaskCompletionRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected TaskCompletionRule.TaskCompletionRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return TaskCompletionRuleArguments.builder()
                .taskResult((TaskResult) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, TaskCompletionRule.ARG_TASK_RESULT))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - taskResult
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class TaskCompletionRuleArguments {
        /**
         * Reference to the current taskResult object from the task execution
         */
        @Argument(name = TaskCompletionRule.ARG_TASK_RESULT)
        private final TaskResult taskResult;
    }
}
