package com.sailpoint.rule.workflow;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.workflow.WorkflowRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

import java.util.UUID;

/**
 * Simple implementation of {@link WorkflowRule} rule
 */
@Slf4j
@Rule(value = "Simple workflow rule", type = sailpoint.object.Rule.Type.Workflow)
public class SimpleWorkflowRule extends WorkflowRule {

    /**
     * Log current workflow and return random object
     */
    @Override
    @Argument(name = "object", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Object internalExecute(JavaRuleContext context, WorkflowRule.WorkflowRuleArguments arguments) {
        log.info("Current workflow:[{}]", arguments.getWorkflow());
        return UUID.randomUUID();
    }
}
