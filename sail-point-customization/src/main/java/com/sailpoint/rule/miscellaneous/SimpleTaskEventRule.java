package com.sailpoint.rule.miscellaneous;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.miscellaneous.TaskEventRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.TaskResult;

import java.util.Map;

/**
 * Simple implementation of {@link TaskEventRule} rule
 */
@Slf4j
@Rule(value = "Simple task event rule", type = sailpoint.object.Rule.Type.TaskEventRule)
public class SimpleTaskEventRule extends TaskEventRule {

    /**
     * Log current task result and event. Return null.
     */
    @Override
    @Argument(name = "newTaskResult", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, TaskResult> internalExecute(JavaRuleContext context,
                                                      TaskEventRuleArguments arguments) {
        log.info("Current task result:[{}]", arguments.getTaskResult());
        log.info("Current task event:[{}]", arguments.getEvent());
        return null;
    }
}
