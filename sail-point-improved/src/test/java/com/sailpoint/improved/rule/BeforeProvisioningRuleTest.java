package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.provisioning.BeforeProvisioningRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;

import java.util.HashMap;
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
 * Test for {@link BeforeProvisioningRule} class
 */
public class BeforeProvisioningRuleTest {

    /**
     * Test instance of {@link BeforeProvisioningRule}
     */
    private BeforeProvisioningRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link BeforeProvisioningRuleTest#testRule} and {@link BeforeProvisioningRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(BeforeProvisioningRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - null as it is a rule without outputs
     * Expectation:
     * - plan as in rule context args by name {@link BeforeProvisioningRule#ARG_PLAN}
     * - application as in rule context args by name {@link BeforeProvisioningRule#ARG_APPLICATION}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            BeforeProvisioningRule.BeforeProvisioningRuleArguments arguments = (BeforeProvisioningRule.BeforeProvisioningRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Plan is not match", testRuleContext.getArguments().get(BeforeProvisioningRule.ARG_PLAN),
                    arguments.getPlan());
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(BeforeProvisioningRule.ARG_APPLICATION),
                    arguments.getApplication());
            return null;
        }).when(testRule).internalExecuteNoneOutput(eq(sailPointContext), any());

        assertNull(testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(sailPointContext), any());
        verify(testRule).internalExecuteNoneOutput(eq(sailPointContext), any());
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
        for (String noneNullArgument : BeforeProvisioningRule.NONE_NULL_ARGUMENTS_NAME) {

            JavaRuleContext testRuleContext = buildTestJavaRuleContext();
            testRuleContext.getArguments().remove(noneNullArgument);

            assertThrows(GeneralException.class, () -> testRule.execute(testRuleContext));
            verify(testRule).internalValidation(eq(testRuleContext));
            verify(testRule, never()).internalExecute(eq(sailPointContext), any());
            verify(testRule, never()).internalExecuteNoneOutput(eq(sailPointContext), any());
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
        assertEquals("Rule type is not match", Rule.Type.BeforeProvisioning.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(BeforeProvisioningRule.ARG_PLAN, mock(ProvisioningPlan.class));
        ruleParameters.put(BeforeProvisioningRule.ARG_APPLICATION, mock(Application.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
