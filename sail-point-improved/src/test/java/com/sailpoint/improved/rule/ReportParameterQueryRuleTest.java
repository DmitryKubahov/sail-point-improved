package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.report.ReportParameterQueryRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Attributes;
import sailpoint.object.Filter;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.QueryOptions;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;

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
 * Test for {@link ReportParameterQueryRule} class
 */
public class ReportParameterQueryRuleTest {

    /**
     * Test instance of {@link ReportParameterQueryRule}
     */
    private ReportParameterQueryRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link ReportParameterQueryRuleTest#testRule} and {@link ReportParameterQueryRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(ReportParameterQueryRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - value as in rule context args by name {@link ReportParameterQueryRule#ARG_VALUE}
     * - arguments as in rule context args by name {@link ReportParameterQueryRule#ARG_ARGUMENTS}
     * - queryOptions as in rule context args by name {@link ReportParameterQueryRule#ARG_QUERY_OPTIONS}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        QueryOptions testResult = new QueryOptions(Filter.ne("testUUID", UUID.randomUUID().toString()));

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            ReportParameterQueryRule.ReportParameterQueryRuleArguments arguments = (ReportParameterQueryRule.ReportParameterQueryRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Value is not match",
                    testRuleContext.getArguments().get(ReportParameterQueryRule.ARG_VALUE),
                    arguments.getValue());
            assertEquals("Arguments is not match",
                    testRuleContext.getArguments().get(ReportParameterQueryRule.ARG_ARGUMENTS),
                    arguments.getArguments());
            assertEquals("QueryOptions is not match",
                    testRuleContext.getArguments().get(ReportParameterQueryRule.ARG_QUERY_OPTIONS),
                    arguments.getQueryOptions());
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
        for (String noneNullArgument : ReportParameterQueryRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.ReportParameterQuery.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(ReportParameterQueryRule.ARG_VALUE, new Object());
        ruleParameters.put(ReportParameterQueryRule.ARG_ARGUMENTS, mock(Attributes.class));
        ruleParameters.put(ReportParameterQueryRule.ARG_QUERY_OPTIONS, mock(QueryOptions.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
