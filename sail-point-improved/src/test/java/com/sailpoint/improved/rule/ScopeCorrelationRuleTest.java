package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.scoping.ScopeCorrelationRule;
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
import static org.mockito.Mockito.when;

/**
 * Test for {@link ScopeCorrelationRule} class
 */
public class ScopeCorrelationRuleTest {

    /**
     * Test instance of {@link ScopeCorrelationRule}
     */
    private ScopeCorrelationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link ScopeCorrelationRuleTest#testRule} and {@link ScopeCorrelationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(ScopeCorrelationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - identity as in rule context args by name {@link ScopeCorrelationRule#ARG_IDENTITY}
     * - scopeCorrelationAttribute as in rule context args by name {@link ScopeCorrelationRule#ARG_SCOPE_CORRELATION_ATTRIBUTE}
     * - scopeCorrelationAttributeValue as in rule context args by name {@link ScopeCorrelationRule#ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        List<Scope> testResult = Collections.singletonList(mock(Scope.class));

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            ScopeCorrelationRule.ScopeCorrelationRuleArguments arguments = (ScopeCorrelationRule.ScopeCorrelationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(ScopeCorrelationRule.ARG_IDENTITY),
                    arguments.getIdentity());
            assertEquals("ScopeCorrelationAttribute is not match",
                    testRuleContext.getArguments().get(ScopeCorrelationRule.ARG_SCOPE_CORRELATION_ATTRIBUTE),
                    arguments.getScopeCorrelationAttribute());
            assertEquals("ScopeCorrelationAttributeValue is not match",
                    testRuleContext.getArguments().get(ScopeCorrelationRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE),
                    arguments.getScopeCorrelationAttributeValue());
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
     * - valid rule context, but {@link ScopeCorrelationRule#ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - call internalExecute
     */
    @Test
    public void nullCorrelationAttributeValueArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        List<Scope> testResult = Collections.singletonList(mock(Scope.class));

        testRuleContext.getArguments().remove(ScopeCorrelationRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE);
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
        for (String noneNullArgument : ScopeCorrelationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.ScopeCorrelation.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(ScopeCorrelationRule.ARG_IDENTITY, mock(Identity.class));
        ruleParameters.put(ScopeCorrelationRule.ARG_SCOPE_CORRELATION_ATTRIBUTE, UUID.randomUUID().toString());
        ruleParameters
                .put(ScopeCorrelationRule.ARG_SCOPE_CORRELATION_ATTRIBUTE_VALUE, UUID.randomUUID().toString());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
