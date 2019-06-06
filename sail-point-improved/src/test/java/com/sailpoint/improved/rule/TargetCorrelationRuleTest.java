package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.unstructured.TargetCorrelationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Target;
import sailpoint.tools.GeneralException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
 * Test for {@link TargetCorrelationRule} class
 */
public class TargetCorrelationRuleTest {

    /**
     * Test instance of {@link TargetCorrelationRule}
     */
    private TargetCorrelationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link TargetCorrelationRuleTest#testRule} and {@link TargetCorrelationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(TargetCorrelationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - application as in rule context args by name {@link TargetCorrelationRule#ARG_APPLICATION}
     * - native id as in rule context args by name {@link TargetCorrelationRule#ARG_NATIVE_ID}
     * - target as in rule context args by name {@link TargetCorrelationRule#ARG_TARGET}
     * - target source as in rule context args by name {@link TargetCorrelationRule#ARG_TARGET_SOURCE}
     * - isGroup source as in rule context args by name {@link TargetCorrelationRule#ARG_IS_GROUP}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID());

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            TargetCorrelationRule.TargetCorrelationRuleArguments arguments = (TargetCorrelationRule.TargetCorrelationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(TargetCorrelationRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("NativeId is not match",
                    testRuleContext.getArguments().get(TargetCorrelationRule.ARG_NATIVE_ID),
                    arguments.getNativeId());
            assertEquals("Target is not match",
                    testRuleContext.getArguments().get(TargetCorrelationRule.ARG_TARGET),
                    arguments.getTarget());
            assertEquals("Target source is not match",
                    testRuleContext.getArguments().get(TargetCorrelationRule.ARG_TARGET_SOURCE),
                    arguments.getTargetSource());
            assertEquals("IsGroup is not match",
                    testRuleContext.getArguments().get(TargetCorrelationRule.ARG_IS_GROUP),
                    arguments.isGroup());
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
        for (String noneNullArgument : TargetCorrelationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.TargetCorrelation.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(TargetCorrelationRule.ARG_APPLICATION, mock(Application.class));
        ruleParameters.put(TargetCorrelationRule.ARG_NATIVE_ID, UUID.randomUUID().toString());
        ruleParameters.put(TargetCorrelationRule.ARG_TARGET, mock(Target.class));
        ruleParameters.put(TargetCorrelationRule.ARG_TARGET_SOURCE, mock(Target.class));
        ruleParameters.put(TargetCorrelationRule.ARG_IS_GROUP, new Random().nextBoolean());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
