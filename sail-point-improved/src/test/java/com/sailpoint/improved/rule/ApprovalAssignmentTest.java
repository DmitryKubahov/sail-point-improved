package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.notification.ApprovalAssignmentRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.ApprovalSet;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Workflow;
import sailpoint.tools.GeneralException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sailpoint.improved.JUnit4Helper.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link ApprovalAssignmentRule} class
 */
public class ApprovalAssignmentTest {

    /**
     * Test instance of {@link ApprovalAssignmentRule}
     */
    private ApprovalAssignmentRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link ApprovalAssignmentTest#testRule} and {@link ApprovalAssignmentTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(ApprovalAssignmentRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - approvals as in rule context args by name {@link ApprovalAssignmentRule#ARG_APPROVALS_NAME}
     * - approvalSet as in rule context args by name {@link ApprovalAssignmentRule#ARG_APPROVALS_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        List<Workflow.Approval> testResult = Collections.singletonList(mock(Workflow.Approval.class));

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            ApprovalAssignmentRule.ApprovalAssignmentRuleArguments arguments = (ApprovalAssignmentRule.ApprovalAssignmentRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Approvals is not match",
                    testRuleContext.getArguments().get(ApprovalAssignmentRule.ARG_APPROVALS_NAME),
                    arguments.getApprovals());
            assertEquals("ApprovalSet is not match",
                    testRuleContext.getArguments().get(ApprovalAssignmentRule.ARG_APPROVAL_SET_NAME),
                    arguments.getApprovalSet());
            return testResult;
        }).when(testRule).internalExecute(eq(sailPointContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test execution with invalid arguments.
     * Input:
     * - invalid rule context: one of none-null arguments is null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - do not call internalExecute
     */
    @Test
    public void noneNullArgumentIsNullTest() throws GeneralException {
        for (String noneNullArgument : ApprovalAssignmentRule.NONE_NULL_ARGUMENTS_NAME) {

            JavaRuleContext testRuleContext = buildTestJavaRuleContext();
            testRuleContext.getArguments().remove(noneNullArgument);

            assertThrows(GeneralException.class, () -> testRule.execute(testRuleContext));
            verify(testRule).internalValidation(eq(testRuleContext));
            verify(testRule, never()).internalExecute(eq(sailPointContext), any());
        }
    }

    /**
     * Test rule type
     * Input:
     * - rule type value
     * Expectation:
     * - expected rule type
     */
    @Test
    public void ruleTypeTest() {
        assertEquals("Rule type is not match", Rule.Type.ApprovalAssignment.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(ApprovalAssignmentRule.ARG_APPROVALS_NAME,
                Collections.singletonList(mock(Workflow.Approval.class)));
        ruleParameters.put(ApprovalAssignmentRule.ARG_APPROVAL_SET_NAME, mock(ApprovalSet.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
