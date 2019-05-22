package com.sailpoint.improved.rule.notification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.ApprovalSet;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Workflow;

import java.util.Arrays;
import java.util.List;

/**
 * It is called during the approval generation process in a workflow – specifically the Provisioning Approval
 * Subprocess that ships with IdentityIQ versions 6.2+. It is passed the approval list as it has been built based
 * on the the approval step specification, but it provides one last hook where custom logic can be infused into
 * the approval creation process.
 * It could, for example, change who is responsible for completing the approval process based on some attribute about the
 * request, the workItem, or the target Identity.
 * The main purpose of this rule is to allow approval ownership to be calculated based on extended attribute or
 * some other criteria that falls outside the scope of the default mechanisms for deriving the approval owner. It
 * could also be used to alter the approval scheme according to non-standard criteria, or even to bypass approval
 * entirely based on certain criteria.
 * <p>
 * Output:
 * The final approval list to use for this approval process
 */
@Slf4j
public abstract class ApprovalAssignmentRule
        extends AbstractJavaRuleExecutor<List<Workflow.Approval>, ApprovalAssignmentRule.ApprovalAssignmentRuleArguments> {

    /**
     * Name of approvals argument name
     */
    public static final String ARG_APPROVALS = "approvals";
    /**
     * Name of approvalSet argument name
     */
    public static final String ARG_APPROVAL_SET = "approvalSet";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ApprovalAssignmentRule.ARG_APPROVALS,
            ApprovalAssignmentRule.ARG_APPROVAL_SET
    );

    /**
     * Default constructor
     */
    public ApprovalAssignmentRule() {
        super(Rule.Type.ApprovalAssignment.name(), ApprovalAssignmentRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ApprovalAssignmentRule.ApprovalAssignmentRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ApprovalAssignmentRuleArguments.builder()
                .approvals((List<Workflow.Approval>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ApprovalAssignmentRule.ARG_APPROVALS))
                .approvalSet((ApprovalSet) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ApprovalAssignmentRule.ARG_APPROVAL_SET))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - approvals
     * - approvalSet
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ApprovalAssignmentRuleArguments {
        /**
         * List of approval objects as created based on the approval step specification in the workflow. Contains:
         * - the definition
         * - the current state of the approval
         */
        @Argument(name = ApprovalAssignmentRule.ARG_APPROVALS)
        private final List<Workflow.Approval> approvals;
        /**
         * Contains all the items to be approved in the set
         */
        @Argument(name = ApprovalAssignmentRule.ARG_APPROVAL_SET)
        private final ApprovalSet approvalSet;
    }
}
