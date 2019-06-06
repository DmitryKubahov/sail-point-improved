package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.unstructured.TargetCreationRule;
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
 * Test for {@link TargetCreationRule} class
 */
public class TargetCreationRuleTest {

    /**
     * Test instance of {@link TargetCreationRule}
     */
    private TargetCreationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link TargetCreationRuleTest#testRule} and {@link TargetCreationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(TargetCreationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - application as in rule context args by name {@link TargetCreationRule#ARG_APPLICATION}
     * - target as in rule context args by name {@link TargetCreationRule#ARG_TARGET}
     * - target source as in rule context args by name {@link TargetCreationRule#ARG_TARGET_SOURCE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Target testResult = mock(Target.class);

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            TargetCreationRule.TargetCreationRuleArguments arguments = (TargetCreationRule.TargetCreationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(TargetCreationRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("Target is not match",
                    testRuleContext.getArguments().get(TargetCreationRule.ARG_TARGET),
                    arguments.getTarget());
            assertEquals("Target source is not match",
                    testRuleContext.getArguments().get(TargetCreationRule.ARG_TARGET_SOURCE),
                    arguments.getTargetSource());
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
        for (String noneNullArgument : TargetCreationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.TargetCreation.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(TargetCreationRule.ARG_APPLICATION, mock(Application.class));
        ruleParameters.put(TargetCreationRule.ARG_TARGET, mock(Target.class));
        ruleParameters.put(TargetCreationRule.ARG_TARGET_SOURCE, mock(Target.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
