package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.login.SAMLCorrelationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;
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
 * Test for {@link SAMLCorrelationRule} class
 */
public class SAMLCorrelationRuleTest {

    /**
     * Test instance of {@link SAMLCorrelationRule}
     */
    private SAMLCorrelationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link SAMLCorrelationRuleTest#testRule} and {@link SAMLCorrelationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(SAMLCorrelationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test identity value
     * Expectation:
     * - assertionAttributes in rule context args by name {@link SAMLCorrelationRule#ARG_ASSERTION_ATTRIBUTES}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionIdentityTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Identity testResult = mock(Identity.class);

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            SAMLCorrelationRule.SAMLCorrelationRuleArguments arguments = (SAMLCorrelationRule.SAMLCorrelationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("AssertionAttributes is not match",
                    testRuleContext.getArguments().get(SAMLCorrelationRule.ARG_ASSERTION_ATTRIBUTES),
                    arguments.getAssertionAttributes());
            return testResult;
        }).when(testRule).internalExecute(eq(testRuleContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test link value
     * Expectation:
     * - assertionAttributes in rule context args by name {@link SAMLCorrelationRule#ARG_ASSERTION_ATTRIBUTES}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionLinkTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Link testResult = mock(Link.class);

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            SAMLCorrelationRule.SAMLCorrelationRuleArguments arguments = (SAMLCorrelationRule.SAMLCorrelationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("AssertionAttributes is not match",
                    testRuleContext.getArguments().get(SAMLCorrelationRule.ARG_ASSERTION_ATTRIBUTES),
                    arguments.getAssertionAttributes());
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
        for (String noneNullArgument : SAMLCorrelationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.SAMLCorrelation.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(SAMLCorrelationRule.ARG_ASSERTION_ATTRIBUTES,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
