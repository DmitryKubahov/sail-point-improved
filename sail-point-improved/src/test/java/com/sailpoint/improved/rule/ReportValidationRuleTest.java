package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.report.ReportValidationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Form;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.LiveReport;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Message;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
 * Test for {@link ReportValidationRule} class
 */
public class ReportValidationRuleTest {

    /**
     * Test instance of {@link ReportValidationRule}
     */
    private ReportValidationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link ReportValidationRuleTest#testRule} and {@link ReportValidationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(ReportValidationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - report as in rule context args by name {@link ReportValidationRule#ARG_REPORT}
     * - form as in rule context args by name {@link ReportValidationRule#ARG_FORM}
     * - locale as in rule context args by name {@link ReportValidationRule#ARG_LOCALE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        List<Message> testResult = Collections.singletonList(Message.info(UUID.randomUUID().toString()));

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            ReportValidationRule.ReportValidationRuleArguments arguments = (ReportValidationRule.ReportValidationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Report is not match",
                    testRuleContext.getArguments().get(ReportValidationRule.ARG_REPORT),
                    arguments.getReport());
            assertEquals("Form is not match",
                    testRuleContext.getArguments().get(ReportValidationRule.ARG_FORM),
                    arguments.getForm());
            assertEquals("Local is not match",
                    testRuleContext.getArguments().get(ReportValidationRule.ARG_LOCALE),
                    arguments.getLocale());
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
        for (String noneNullArgument : ReportValidationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.ReportValidator.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(ReportValidationRule.ARG_REPORT, mock(LiveReport.class));
        ruleParameters.put(ReportValidationRule.ARG_FORM, mock(Form.class));
        ruleParameters.put(ReportValidationRule.ARG_LOCALE, Locale.getDefault());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
