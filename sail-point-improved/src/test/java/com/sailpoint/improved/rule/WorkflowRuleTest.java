package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.workflow.WorkflowRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.WorkItem;
import sailpoint.object.Workflow;
import sailpoint.tools.GeneralException;
import sailpoint.workflow.WorkflowContext;
import sailpoint.workflow.WorkflowHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
 * Test for {@link WorkflowRule} class
 */
public class WorkflowRuleTest {

    /**
     * Test instance of {@link WorkflowRule}
     */
    private WorkflowRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link WorkflowRuleTest#testRule} and {@link WorkflowRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(WorkflowRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - wfcontext as in rule context args by name {@link WorkflowRule#ARG_WORKFLOW_CONTEXT}
     * - handler as in rule context args by name {@link WorkflowRule#ARG_HANDLER}
     * - workflow as in rule context args by name {@link WorkflowRule#ARG_WORKFLOW}
     * - step as in rule context args by name {@link WorkflowRule#ARG_STEP}
     * - approval as in rule context args by name {@link WorkflowRule#ARG_APPROVAL}
     * - item as in rule context args by name {@link WorkflowRule#ARG_ITEM}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Object testResult = UUID.randomUUID();

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            WorkflowRule.WorkflowRuleArguments arguments = (WorkflowRule.WorkflowRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("WorkflowContext is not match",
                    testRuleContext.getArguments().get(WorkflowRule.ARG_WORKFLOW_CONTEXT),
                    arguments.getWorkflowContext());
            assertEquals("Handler is not match",
                    testRuleContext.getArguments().get(WorkflowRule.ARG_HANDLER),
                    arguments.getHandler());
            assertEquals("Workflow is not match",
                    testRuleContext.getArguments().get(WorkflowRule.ARG_WORKFLOW),
                    arguments.getWorkflow());
            assertEquals("Step is not match",
                    testRuleContext.getArguments().get(WorkflowRule.ARG_STEP),
                    arguments.getStep());
            assertEquals("Approval is not match",
                    testRuleContext.getArguments().get(WorkflowRule.ARG_APPROVAL),
                    arguments.getApproval());
            assertEquals("Item is not match",
                    testRuleContext.getArguments().get(WorkflowRule.ARG_ITEM),
                    arguments.getItem());
            return testResult;
        }).when(testRule).internalExecute(eq(testRuleContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
    }

    /**
     * Test execution with valid NULL arguments.
     * Input:
     * - valid rule context, but {@link WorkflowRule#ARG_STEP} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalWorkflow
     * - call internalExecute
     */
    @Test
    public void nullStepArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(WorkflowRule.ARG_STEP);

        testRule.execute(testRuleContext);
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
    }

    /**
     * Test execution with valid NULL arguments.
     * Input:
     * - valid rule context, but {@link WorkflowRule#ARG_APPROVAL} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalWorkflow
     * - call internalExecute
     */
    @Test
    public void nullApprovalArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(WorkflowRule.ARG_APPROVAL);

        testRule.execute(testRuleContext);
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
    }

    /**
     * Test execution with valid NULL arguments.
     * Input:
     * - valid rule context, but {@link WorkflowRule#ARG_ITEM} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalWorkflow
     * - call internalExecute
     */
    @Test
    public void nullItemArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(WorkflowRule.ARG_ITEM);

        testRule.execute(testRuleContext);
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
    }

    /**
     * Test execution with invalid arguments.
     * Input:
     * - invalid rule context: one of none-null arguments is null
     * Output:
     * - General exception
     * Expectation:
     * - call internalWorkflow
     * - do not call internalExecute
     */
    @Test
    public void noneNullArgumentIsNullTest() throws GeneralException {
        for (String noneNullArgument : WorkflowRule.NONE_NULL_ARGUMENTS_NAME) {

            JavaRuleContext testRuleContext = buildTestJavaRuleContext();
            testRuleContext.getArguments().remove(noneNullArgument);

            assertThrows(GeneralException.class, () -> testRule.execute(testRuleContext));
            verify(testRule).internalValidation(eq(testRuleContext));
            verify(testRule, never()).internalExecute(eq(testRuleContext), any());
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
        assertEquals("Rule type is not match", Rule.Type.Workflow.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(WorkflowRule.ARG_WORKFLOW_CONTEXT, mock(WorkflowContext.class));
        ruleParameters.put(WorkflowRule.ARG_HANDLER, mock(WorkflowHandler.class));
        ruleParameters.put(WorkflowRule.ARG_WORKFLOW, mock(Workflow.class));
        ruleParameters.put(WorkflowRule.ARG_STEP, mock(Workflow.Step.class));
        ruleParameters.put(WorkflowRule.ARG_APPROVAL, mock(Workflow.Approval.class));
        ruleParameters.put(WorkflowRule.ARG_ITEM, mock(WorkItem.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
