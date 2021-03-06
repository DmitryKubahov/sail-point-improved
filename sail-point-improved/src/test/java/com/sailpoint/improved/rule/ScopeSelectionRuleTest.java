package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.scoping.ScopeSelectionRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Scope;
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
import static org.mockito.Mockito.when;

/**
 * Test for {@link ScopeSelectionRule} class
 */
public class ScopeSelectionRuleTest {

    /**
     * Test instance of {@link ScopeSelectionRule}
     */
    private ScopeSelectionRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link ScopeSelectionRuleTest#testRule} and {@link ScopeSelectionRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(ScopeSelectionRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - identity as in rule context args by name {@link ScopeSelectionRule#ARG_IDENTITY}
     * - scopeSelectionAttribute as in rule context args by name {@link ScopeSelectionRule#ARG_SCOPE_CORRELATION_ATTRIBUTE}
     * - scopeCorrelationAttribute as in rule context args by name {@link ScopeSelectionRule#ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE}
     * - candidateScopes as in rule context args by name {@link ScopeSelectionRule#ARG_CANDIDATE_SCOPES}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Scope testResult = mock(Scope.class);

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            ScopeSelectionRule.ScopeSelectionRuleArguments arguments = (ScopeSelectionRule.ScopeSelectionRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(ScopeSelectionRule.ARG_IDENTITY),
                    arguments.getIdentity());
            assertEquals("ScopeSelectionAttribute is not match",
                    testRuleContext.getArguments().get(ScopeSelectionRule.ARG_SCOPE_CORRELATION_ATTRIBUTE),
                    arguments.getScopeCorrelationAttribute());
            assertEquals("ScopeSelectionAttributeValue is not match",
                    testRuleContext.getArguments().get(ScopeSelectionRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE),
                    arguments.getScopeCorrelationAttributeValue());
            assertEquals("candidateScopes is not match",
                    testRuleContext.getArguments().get(ScopeSelectionRule.ARG_CANDIDATE_SCOPES),
                    arguments.getCandidateScopes());
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
     * - valid rule context, but {@link ScopeSelectionRule#ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - call internalExecute
     */
    @Test
    public void nullCorrelationAttributeValueArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Scope testResult = mock(Scope.class);

        testRuleContext.getArguments().remove(ScopeSelectionRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE);
        when(testRule.internalExecute(eq(testRuleContext), any())).thenReturn(testResult);

        assertEquals(testResult, testRule.execute(testRuleContext));
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
        for (String noneNullArgument : ScopeSelectionRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.ScopeSelection.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(ScopeSelectionRule.ARG_IDENTITY, mock(Identity.class));
        ruleParameters.put(ScopeSelectionRule.ARG_SCOPE_CORRELATION_ATTRIBUTE, UUID.randomUUID().toString());
        ruleParameters
                .put(ScopeSelectionRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE, UUID.randomUUID().toString());
        ruleParameters.put(ScopeSelectionRule.ARG_CANDIDATE_SCOPES,
                Collections.singletonList(new Scope(UUID.randomUUID().toString())));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
