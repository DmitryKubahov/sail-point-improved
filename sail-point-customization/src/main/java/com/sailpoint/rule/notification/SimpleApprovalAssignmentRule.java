package com.sailpoint.rule.notification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.notification.ApprovalAssignmentRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.Workflow;

import java.util.List;

/**
 * Simple implementation of {@link ApprovalAssignmentRule} rule
 */
@Slf4j
@Rule(value = "Simple approval assignment rule", type = sailpoint.object.Rule.Type.ApprovalAssignment)
public class SimpleApprovalAssignmentRule extends ApprovalAssignmentRule {

    /**
     * Log current approvals and approvalSet and return approvals
     */
    @Override
    @Argument(name = "newOwner", type = ArgumentType.RETURNS, isReturnsType = true)
    protected List<Workflow.Approval> internalExecute(SailPointContext sailPointContext,
                                                      ApprovalAssignmentRuleArguments arguments) {
        log.info("Current approvals:{}", arguments.getApprovals());
        log.info("Current approvalSet:{}", arguments.getApprovalSet());
        return arguments.getApprovals();
    }
}
