package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.policy.PolicyOwnerRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.BaseConstraint;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Policy;
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
 * Test for {@link PolicyOwnerRule} class
 */
public class PolicyOwnerRuleTest {

    /**
     * Test instance of {@link PolicyOwnerRule}
     */
    private PolicyOwnerRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link PolicyOwnerRuleTest#testRule} and {@link PolicyOwnerRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(PolicyOwnerRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - identity as in rule context args by name {@link PolicyOwnerRule#ARG_IDENTITY}
     * - policy as in rule context args by name {@link PolicyOwnerRule#ARG_POLICY}
     * - constraint as in rule context args by name {@link PolicyOwnerRule#ARG_CONSTRAINT}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Identity testResult = mock(Identity.class);

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            PolicyOwnerRule.PolicyOwnerRuleArguments arguments = (PolicyOwnerRule.PolicyOwnerRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(PolicyOwnerRule.ARG_IDENTITY),
                    arguments.getIdentity());
            assertEquals("Policy is not match",
                    testRuleContext.getArguments().get(PolicyOwnerRule.ARG_POLICY),
                    arguments.getPolicy());
            assertEquals("Constraint is not match",
                    testRuleContext.getArguments().get(PolicyOwnerRule.ARG_CONSTRAINT),
                    arguments.getConstraint());
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
        for (String noneNullArgument : PolicyOwnerRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.PolicyOwner.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(PolicyOwnerRule.ARG_IDENTITY, mock(Identity.class));
        ruleParameters.put(PolicyOwnerRule.ARG_POLICY, mock(Policy.class));
        ruleParameters.put(PolicyOwnerRule.ARG_CONSTRAINT, mock(BaseConstraint.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
