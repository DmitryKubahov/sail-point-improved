package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.notification.WorkItemForwardRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.WorkItem;
import sailpoint.tools.GeneralException;

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
 * Test for {@link WorkItemForwardRule} class
 */
public class WorkItemForwardRuleTest {

    /**
     * Test instance of {@link WorkItemForwardRule}
     */
    private WorkItemForwardRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link WorkItemForwardRuleTest#testRule} and {@link WorkItemForwardRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(WorkItemForwardRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - item as in rule context args by name {@link WorkItemForwardRule#ARG_ITEM_NAME}
     * - owner as in rule context args by name {@link WorkItemForwardRule#ARG_OWNER_NAME}
     * - identity as in rule context args by name {@link WorkItemForwardRule#ARG_IDENTITY_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Identity testResult = mock(Identity.class);

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            WorkItemForwardRule.WorkItemForwardRuleArguments arguments = (WorkItemForwardRule.WorkItemForwardRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Item is not match",
                    testRuleContext.getArguments().get(WorkItemForwardRule.ARG_ITEM_NAME),
                    arguments.getItem());
            assertEquals("Owner is not match",
                    testRuleContext.getArguments().get(WorkItemForwardRule.ARG_OWNER_NAME),
                    arguments.getOwner());
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(WorkItemForwardRule.ARG_IDENTITY_NAME),
                    arguments.getIdentity());
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
        for (String noneNullArgument : WorkItemForwardRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.WorkItemForward.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(WorkItemForwardRule.ARG_ITEM_NAME, mock(WorkItem.class));
        ruleParameters.put(WorkItemForwardRule.ARG_OWNER_NAME, mock(Identity.class));
        ruleParameters.put(WorkItemForwardRule.ARG_IDENTITY_NAME, mock(Identity.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
