package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.form.ValidationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Application;
import sailpoint.object.Field;
import sailpoint.object.Form;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Message;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
 * Test for {@link ValidationRule} class
 */
public class ValidationRuleTest {

    /**
     * Test instance of {@link ValidationRule}
     */
    private ValidationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link ValidationRuleTest#testRule} and {@link ValidationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(ValidationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - identity as in rule context args by name {@link ValidationRule#ARG_IDENTITY}
     * - application as in rule context args by name {@link ValidationRule#ARG_APPLICATION}
     * - form as in rule context args by name {@link ValidationRule#ARG_FORM}
     * - field as in rule context args by name {@link ValidationRule#ARG_FIELD}
     * - value as in rule context args by name {@link ValidationRule#ARG_VALUE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        List<Message> testResult = Collections.singletonList(Message.info(UUID.randomUUID().toString()));

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            ValidationRule.ValidationRuleArguments arguments = (ValidationRule.ValidationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(ValidationRule.ARG_IDENTITY),
                    arguments.getIdentity());
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(ValidationRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("Form is not match",
                    testRuleContext.getArguments().get(ValidationRule.ARG_FORM),
                    arguments.getForm());
            assertEquals("Field is not match",
                    testRuleContext.getArguments().get(ValidationRule.ARG_FIELD),
                    arguments.getField());
            assertEquals("Value is not match",
                    testRuleContext.getArguments().get(ValidationRule.ARG_VALUE),
                    arguments.getValue());
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
     * - valid rule context, but {@link ValidationRule#ARG_VALUE} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - call internalExecute
     */
    @Test
    public void nullValueNameArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(ValidationRule.ARG_VALUE);

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
     * - call internalValidation
     * - do not call internalExecute
     */
    @Test
    public void noneNullArgumentIsNullTest() throws GeneralException {
        for (String noneNullArgument : ValidationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.Validation.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(ValidationRule.ARG_IDENTITY, mock(Identity.class));
        ruleParameters.put(ValidationRule.ARG_APPLICATION, mock(Application.class));
        ruleParameters.put(ValidationRule.ARG_FORM, mock(Form.class));
        ruleParameters.put(ValidationRule.ARG_FIELD, mock(Field.class));
        ruleParameters.put(ValidationRule.ARG_VALUE, new Object());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
