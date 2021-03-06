package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.aggregation.IdentityCreationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ResourceObject;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
 * Test for {@link IdentityCreationRule} class
 */
public class IdentityCreationRuleTest {

    /**
     * Test instance of {@link IdentityCreationRule}
     */
    private IdentityCreationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link IdentityCreationRuleTest#testRule} and {@link IdentityCreationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(IdentityCreationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - null as it is a rule without outputs
     * Expectation:
     * - environment as in rule context args by name {@link IdentityCreationRule#ARG_ENVIRONMENT}
     * - application as in rule context args by name {@link IdentityCreationRule#ARG_APPLICATION}
     * - account as in rule context args by name {@link IdentityCreationRule#ARG_ACCOUNT}
     * - identity as in rule context args by name {@link IdentityCreationRule#ARG_IDENTITY}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            IdentityCreationRule.IdentityCreationRuleArguments arguments = (IdentityCreationRule.IdentityCreationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Environment is not match",
                    testRuleContext.getArguments().get(IdentityCreationRule.ARG_ENVIRONMENT),
                    arguments.getEnvironment());
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(IdentityCreationRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("Account is not match",
                    testRuleContext.getArguments().get(IdentityCreationRule.ARG_ACCOUNT),
                    arguments.getAccount());
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(IdentityCreationRule.ARG_IDENTITY),
                    arguments.getIdentity());
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
        for (String noneNullArgument : IdentityCreationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.IdentityCreation.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(IdentityCreationRule.ARG_ENVIRONMENT,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        ruleParameters.put(IdentityCreationRule.ARG_APPLICATION, new Application());
        ruleParameters.put(IdentityCreationRule.ARG_ACCOUNT, mock(ResourceObject.class));
        ruleParameters.put(IdentityCreationRule.ARG_IDENTITY, mock(Identity.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
