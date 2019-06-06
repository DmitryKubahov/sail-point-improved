package com.sailpoint.rule.miscellaneous;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.miscellaneous.TaskCompletionRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

/**
 * Simple implementation of {@link TaskCompletionRule} rule
 */
@Slf4j
@Rule(value = "Simple task completion rule", type = sailpoint.object.Rule.Type.TaskCompletion)
public class SimpleTaskCompletionRule extends TaskCompletionRule {

    /**
     * Log current task result
     */
    @Override
    protected void internalExecuteNoneOutput(JavaRuleContext context,
                                             TaskCompletionRuleArguments arguments) {
        log.info("Current task result:[{}]", arguments.getTaskResult());
    }
}
