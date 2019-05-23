package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.policy.PolicyNotificationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Attributes;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Policy;
import sailpoint.object.PolicyViolation;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
 * Test for {@link PolicyNotificationRule} class
 */
public class PolicyNotificationRuleTest {

    /**
     * Test instance of {@link PolicyNotificationRule}
     */
    private PolicyNotificationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link PolicyNotificationRuleTest#testRule} and {@link PolicyNotificationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(PolicyNotificationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - environment as in rule context args by name {@link PolicyNotificationRule#ARG_ENVIRONMENT}
     * - policy as in rule context args by name {@link PolicyNotificationRule#ARG_POLICY}
     * - violation as in rule context args by name {@link PolicyNotificationRule#ARG_VIOLATION}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        List<Identity> testResult = Collections.singletonList(mock(Identity.class));

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            PolicyNotificationRule.PolicyNotificationRuleArguments arguments = (PolicyNotificationRule.PolicyNotificationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Environment is not match",
                    testRuleContext.getArguments().get(PolicyNotificationRule.ARG_ENVIRONMENT),
                    arguments.getEnvironment());
            assertEquals("Policy is not match",
                    testRuleContext.getArguments().get(PolicyNotificationRule.ARG_POLICY),
                    arguments.getPolicy());
            assertEquals("Violation is not match",
                    testRuleContext.getArguments().get(PolicyNotificationRule.ARG_VIOLATION),
                    arguments.getViolation());
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
        for (String noneNullArgument : PolicyNotificationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.PolicyNotification.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(PolicyNotificationRule.ARG_ENVIRONMENT, mock(Attributes.class));
        ruleParameters.put(PolicyNotificationRule.ARG_POLICY, mock(Policy.class));
        ruleParameters.put(PolicyNotificationRule.ARG_VIOLATION, mock(PolicyViolation.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
