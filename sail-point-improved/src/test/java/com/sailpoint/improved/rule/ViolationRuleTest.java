package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.policy.ViolationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.BaseConstraint;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Policy;
import sailpoint.object.PolicyViolation;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;

import java.util.Collections;
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
 * Test for {@link ViolationRule} class
 */
public class ViolationRuleTest {

    /**
     * Test instance of {@link ViolationRule}
     */
    private ViolationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link ViolationRuleTest#testRule} and {@link ViolationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(ViolationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - identity as in rule context args by name {@link ViolationRule#ARG_IDENTITY}
     * - policy as in rule context args by name {@link ViolationRule#ARG_POLICY}
     * - constraint as in rule context args by name {@link ViolationRule#ARG_CONSTRAINT}
     * - violation as in rule context args by name {@link ViolationRule#ARG_VIOLATION}
     * - state as in rule context args by name {@link ViolationRule#ARG_STATE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        PolicyViolation testResult = mock(PolicyViolation.class);

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            ViolationRule.ViolationRuleArguments arguments = (ViolationRule.ViolationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(ViolationRule.ARG_IDENTITY),
                    arguments.getIdentity());
            assertEquals("Policy is not match",
                    testRuleContext.getArguments().get(ViolationRule.ARG_POLICY),
                    arguments.getPolicy());
            assertEquals("Constraint is not match",
                    testRuleContext.getArguments().get(ViolationRule.ARG_CONSTRAINT),
                    arguments.getConstraint());
            assertEquals("Violation is not match",
                    testRuleContext.getArguments().get(ViolationRule.ARG_VIOLATION),
                    arguments.getViolation());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(ViolationRule.ARG_STATE),
                    arguments.getState());
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
     * - call internalViolation
     * - do not call internalExecute
     */
    @Test
    public void noneNullArgumentIsNullTest() throws GeneralException {
        for (String noneNullArgument : ViolationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.Violation.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(ViolationRule.ARG_IDENTITY, mock(Identity.class));
        ruleParameters.put(ViolationRule.ARG_POLICY, mock(Policy.class));
        ruleParameters.put(ViolationRule.ARG_CONSTRAINT, mock(BaseConstraint.class));
        ruleParameters.put(ViolationRule.ARG_VIOLATION, mock(PolicyViolation.class));
        ruleParameters.put(ViolationRule.ARG_STATE,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
