package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.miscellaneous.TaskEventRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.TaskEvent;
import sailpoint.object.TaskResult;
import sailpoint.tools.GeneralException;

import java.util.Collections;
import java.util.HashMap;
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
 * Test for {@link TaskEventRule} class
 */
public class TaskEventRuleTest {

    /**
     * Test instance of {@link TaskEventRule}
     */
    private TaskEventRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link TaskEventRuleTest#testRule} and {@link TaskEventRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(TaskEventRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test resource object
     * Expectation:
     * - taskResult as in rule context args by name {@link TaskEventRule#ARG_TASK_RESULT}
     * - event as in rule context args by name {@link TaskEventRule#ARG_EVENT}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, TaskResult> testResult = Collections
                .singletonMap(TaskEvent.RULE_RETURN_TASK_RESULT, mock(TaskResult.class));

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            TaskEventRule.TaskEventRuleArguments arguments = (TaskEventRule.TaskEventRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("TaskResult is not match",
                    testRuleContext.getArguments().get(TaskEventRule.ARG_TASK_RESULT),
                    arguments.getTaskResult());
            assertEquals("Event is not match",
                    testRuleContext.getArguments().get(TaskEventRule.ARG_EVENT),
                    arguments.getEvent());
            return testResult;
        }).when(testRule).internalExecute(eq(testRuleContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
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
        for (String noneNullArgument : TaskEventRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.TaskEventRule.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(TaskEventRule.ARG_TASK_RESULT, mock(TaskResult.class));
        ruleParameters.put(TaskEventRule.ARG_EVENT, mock(TaskEvent.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
