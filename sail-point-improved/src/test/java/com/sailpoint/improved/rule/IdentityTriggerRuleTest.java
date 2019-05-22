package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.certification.IdentityTriggerRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
 * Test for {@link IdentityTriggerRule} class
 */
public class IdentityTriggerRuleTest {

    /**
     * Test instance of {@link IdentityTriggerRule}
     */
    private IdentityTriggerRule identityTriggerRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link IdentityTriggerRuleTest#identityTriggerRule} and {@link IdentityTriggerRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.identityTriggerRule = mock(IdentityTriggerRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test value of execution
     * Expectation:
     * - newIdentity as in rule context args by name {@link IdentityTriggerRule#ARG_NEW_IDENTITY}
     * - previousIdentity as in rule context args by name {@link IdentityTriggerRule#ARG_PREVIOUS_IDENTITY}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Boolean testResult = new Random().nextBoolean();
        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            IdentityTriggerRule.IdentityTriggerRuleArguments arguments = (IdentityTriggerRule.IdentityTriggerRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("New identity is not match",
                    testRuleContext.getArguments().get(IdentityTriggerRule.ARG_NEW_IDENTITY),
                    arguments.getNewIdentity());
            assertEquals("Previous identity is not match",
                    testRuleContext.getArguments().get(IdentityTriggerRule.ARG_PREVIOUS_IDENTITY),
                    arguments.getPreviousIdentity());
            return testResult;
        }).when(identityTriggerRule).internalExecute(any(), any());

        assertEquals(testResult, identityTriggerRule.execute(testRuleContext));
        verify(identityTriggerRule).internalValidation(eq(testRuleContext));
        verify(identityTriggerRule).execute(eq(testRuleContext));
        verify(identityTriggerRule).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test of execution with newIdentity is null (delete operation)
     * Input:
     * - valid rule context where newIdentity is null
     * Output:
     * - test value of execution
     * Expectation:
     * - newIdentity as null
     * - previousIdentity as in rule context args by name {@link IdentityTriggerRule#ARG_PREVIOUS_IDENTITY}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void newIdentityIsNullTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(IdentityTriggerRule.ARG_NEW_IDENTITY);
        Boolean testResult = new Random().nextBoolean();
        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            IdentityTriggerRule.IdentityTriggerRuleArguments arguments = (IdentityTriggerRule.IdentityTriggerRuleArguments) invocation
                    .getArguments()[1];
            assertNull("New identity is not null",
                    testRuleContext.getArguments().get(IdentityTriggerRule.ARG_NEW_IDENTITY));
            assertEquals("Previous identity is not match",
                    testRuleContext.getArguments().get(IdentityTriggerRule.ARG_PREVIOUS_IDENTITY),
                    arguments.getPreviousIdentity());
            return testResult;
        }).when(identityTriggerRule).internalExecute(any(), any());

        assertEquals(testResult, identityTriggerRule.execute(testRuleContext));
        verify(identityTriggerRule).internalValidation(eq(testRuleContext));
        verify(identityTriggerRule).execute(eq(testRuleContext));
        verify(identityTriggerRule).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test of execution with previousIdentity is null (creation operation)
     * Input:
     * - valid rule context where previousIdentity is null
     * Output:
     * - test value of execution
     * Expectation:
     * - newIdentity as in rule context args by name {@link IdentityTriggerRule#ARG_NEW_IDENTITY}
     * - previousIdentity as null
     * - context as in sailpoint context in rule context
     */
    @Test
    public void previousIdentityIsNullTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(IdentityTriggerRule.ARG_PREVIOUS_IDENTITY);
        Boolean testResult = new Random().nextBoolean();
        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            IdentityTriggerRule.IdentityTriggerRuleArguments arguments = (IdentityTriggerRule.IdentityTriggerRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("New identity is not match",
                    testRuleContext.getArguments().get(IdentityTriggerRule.ARG_NEW_IDENTITY),
                    arguments.getNewIdentity());
            assertNull("Previous identity is not null",
                    testRuleContext.getArguments().get(IdentityTriggerRule.ARG_PREVIOUS_IDENTITY));
            return testResult;
        }).when(identityTriggerRule).internalExecute(any(), any());

        assertEquals(testResult, identityTriggerRule.execute(testRuleContext));
        verify(identityTriggerRule).internalValidation(eq(testRuleContext));
        verify(identityTriggerRule).execute(eq(testRuleContext));
        verify(identityTriggerRule).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test of execution with newIdentity and previousIdentity are null (invalid context)
     * Input:
     * - invalid rule context where previousIdentity and newIdentity are null
     * Output:
     * - general exception
     * Expectation:
     * - call internal validation
     */
    @Test
    public void previousIdentityAndNewIdentityAreNullsTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(IdentityTriggerRule.ARG_NEW_IDENTITY);
        testRuleContext.getArguments().remove(IdentityTriggerRule.ARG_PREVIOUS_IDENTITY);

        assertThrows(GeneralException.class, () -> identityTriggerRule.execute(testRuleContext));
        verify(identityTriggerRule).internalValidation(eq(testRuleContext));
        verify(identityTriggerRule, never()).internalExecute(eq(sailPointContext), any());
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
        assertEquals("Rule type is not match", Rule.Type.IdentityTrigger.name(), identityTriggerRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(IdentityTriggerRule.ARG_NEW_IDENTITY, new Identity());
        ruleParameters.put(IdentityTriggerRule.ARG_PREVIOUS_IDENTITY, new Identity());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
