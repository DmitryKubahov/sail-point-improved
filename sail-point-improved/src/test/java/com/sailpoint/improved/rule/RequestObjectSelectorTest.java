package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.miscellaneous.RequestObjectSelectorRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.QueryInfo;
import sailpoint.object.Rule;
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
 * Test for {@link RequestObjectSelectorRule} class
 */
public class RequestObjectSelectorTest {

    /**
     * Test instance of {@link RequestObjectSelectorRule}
     */
    private RequestObjectSelectorRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link RequestObjectSelectorTest#testRule} and {@link RequestObjectSelectorTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(RequestObjectSelectorRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - requestor as in rule context args by name {@link RequestObjectSelectorRule#ARG_REQUESTOR}
     * - requestee as in rule context args by name {@link RequestObjectSelectorRule#ARG_REQUESTEE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        QueryInfo testResult = mock(QueryInfo.class);

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            RequestObjectSelectorRule.RequestObjectSelectorRuleArguments arguments = (RequestObjectSelectorRule.RequestObjectSelectorRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Requestor is not match",
                    testRuleContext.getArguments().get(RequestObjectSelectorRule.ARG_REQUESTOR),
                    arguments.getRequestor());
            assertEquals("Requestee is not match",
                    testRuleContext.getArguments().get(RequestObjectSelectorRule.ARG_REQUESTEE),
                    arguments.getRequestee());
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
        for (String noneNullArgument : RequestObjectSelectorRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.RequestObjectSelector.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(RequestObjectSelectorRule.ARG_REQUESTOR, mock(Identity.class));
        ruleParameters.put(RequestObjectSelectorRule.ARG_REQUESTEE, mock(Identity.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
