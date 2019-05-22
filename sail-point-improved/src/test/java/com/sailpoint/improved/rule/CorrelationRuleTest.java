package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.aggregation.CorrelationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;
import sailpoint.object.ResourceObject;
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
 * Test for {@link CorrelationRule} class
 */
public class CorrelationRuleTest {

    /**
     * Test instance of {@link CorrelationRule}
     */
    private CorrelationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link CorrelationRuleTest#testRule} and {@link CorrelationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(CorrelationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - environment as in rule context args by name {@link CorrelationRule#ARG_ENVIRONMENT}
     * - application as in rule context args by name {@link CorrelationRule#ARG_APPLICATION}
     * - account as in rule context args by name {@link CorrelationRule#ARG_ACCOUNT}
     * - link as in rule context args by name {@link CorrelationRule#ARG_LINK}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID());

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            CorrelationRule.CorrelationRuleArguments arguments = (CorrelationRule.CorrelationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Environment is not match",
                    testRuleContext.getArguments().get(CorrelationRule.ARG_ENVIRONMENT),
                    arguments.getEnvironment());
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(CorrelationRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("Account is not match",
                    testRuleContext.getArguments().get(CorrelationRule.ARG_ACCOUNT),
                    arguments.getAccount());
            assertEquals("Link is not match",
                    testRuleContext.getArguments().get(CorrelationRule.ARG_LINK),
                    arguments.getLink());
            return testResult;
        }).when(testRule).internalExecute(eq(sailPointContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(sailPointContext), any());
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
        for (String noneNullArgument : CorrelationRule.NONE_NULL_ARGUMENTS_NAME) {

            JavaRuleContext testRuleContext = buildTestJavaRuleContext();
            testRuleContext.getArguments().remove(noneNullArgument);

            assertThrows(GeneralException.class, () -> testRule.execute(testRuleContext));
            verify(testRule).internalValidation(eq(testRuleContext));
            verify(testRule, never()).internalExecute(eq(sailPointContext), any());
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
        assertEquals("Rule type is not match", Rule.Type.Correlation.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(CorrelationRule.ARG_ENVIRONMENT,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        ruleParameters.put(CorrelationRule.ARG_APPLICATION, new Application());
        ruleParameters.put(CorrelationRule.ARG_ACCOUNT, mock(ResourceObject.class));
        ruleParameters.put(CorrelationRule.ARG_LINK, mock(Link.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
