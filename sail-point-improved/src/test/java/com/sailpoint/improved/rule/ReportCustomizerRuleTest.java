package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.report.ReportCustomizerRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.LiveReport;
import sailpoint.object.Rule;
import sailpoint.object.TaskDefinition;
import sailpoint.tools.GeneralException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.sailpoint.improved.JUnit4Helper.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link ReportCustomizerRule} class
 */
public class ReportCustomizerRuleTest {

    /**
     * Test instance of {@link ReportCustomizerRule}
     */
    private ReportCustomizerRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link ReportCustomizerRuleTest#testRule} and {@link ReportCustomizerRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(ReportCustomizerRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - null, as rule does not return anything
     * Expectation:
     * - taskDefinition as in rule context args by name {@link ReportCustomizerRule#ARG_TASK_DEFINITION}
     * - report as in rule context args by name {@link ReportCustomizerRule#ARG_REPORT}
     * - locale as in rule context args by name {@link ReportCustomizerRule#ARG_LOCALE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            ReportCustomizerRule.ReportCustomizerRuleArguments arguments = (ReportCustomizerRule.ReportCustomizerRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("TaskDefinition is not match",
                    testRuleContext.getArguments().get(ReportCustomizerRule.ARG_TASK_DEFINITION),
                    arguments.getTaskDefinition());
            assertEquals("Report is not match",
                    testRuleContext.getArguments().get(ReportCustomizerRule.ARG_REPORT),
                    arguments.getReport());
            assertEquals("Local is not match",
                    testRuleContext.getArguments().get(ReportCustomizerRule.ARG_LOCALE),
                    arguments.getLocale());
            return null;
        }).when(testRule).internalExecuteNoneOutput(eq(testRuleContext), any());

        assertNull(testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
        verify(testRule).internalExecuteNoneOutput(eq(testRuleContext), any());
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
        for (String noneNullArgument : ReportCustomizerRule.NONE_NULL_ARGUMENTS_NAME) {

            JavaRuleContext testRuleContext = buildTestJavaRuleContext();
            testRuleContext.getArguments().remove(noneNullArgument);

            assertThrows(GeneralException.class, () -> testRule.execute(testRuleContext));
            verify(testRule).internalValidation(eq(testRuleContext));
            verify(testRule, never()).internalExecute(eq(testRuleContext), any());
            verify(testRule, never()).internalExecuteNoneOutput(eq(testRuleContext), any());
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
        assertEquals("Rule type is not match", Rule.Type.ReportCustomizer.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(ReportCustomizerRule.ARG_TASK_DEFINITION, mock(TaskDefinition.class));
        ruleParameters.put(ReportCustomizerRule.ARG_REPORT, mock(LiveReport.class));
        ruleParameters.put(ReportCustomizerRule.ARG_LOCALE, Locale.getDefault());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
