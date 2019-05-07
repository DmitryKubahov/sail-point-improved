package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.aggregation.ManagerCorrelationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.connector.AbstractConnector;
import sailpoint.object.Application;
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
import static org.mockito.Mockito.when;

/**
 * Test for {@link ManagerCorrelationRule} class
 */
public class ManagerCorrelationRuleTest {

    /**
     * Test instance of {@link ManagerCorrelationRule}
     */
    private ManagerCorrelationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link ManagerCorrelationRuleTest#testRule} and {@link ManagerCorrelationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(ManagerCorrelationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - environment as in rule context args by name {@link ManagerCorrelationRule#ARG_ENVIRONMENT_NAME}
     * - application as in rule context args by name {@link ManagerCorrelationRule#ARG_APPLICATION_NAME}
     * - instance as in rule context args by name {@link ManagerCorrelationRule#ARG_INSTANCE_NAME}
     * - connector as in rule context args by name {@link ManagerCorrelationRule#ARG_CONNECTOR_NAME}
     * - link as in rule context args by name {@link ManagerCorrelationRule#ARG_LINK_NAME}
     * - managerAttributeValue as in rule context args by name {@link ManagerCorrelationRule#ARG_MANAGER_ATTRIBUTE_VALUE_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = new HashMap<>();
        testResult.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            ManagerCorrelationRule.ManagerCorrelationRuleArguments arguments = (ManagerCorrelationRule.ManagerCorrelationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Environment is not match",
                    testRuleContext.getArguments().get(ManagerCorrelationRule.ARG_ENVIRONMENT_NAME),
                    arguments.getEnvironment());
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(ManagerCorrelationRule.ARG_APPLICATION_NAME),
                    arguments.getApplication());
            assertEquals("Instance is not match",
                    testRuleContext.getArguments().get(ManagerCorrelationRule.ARG_INSTANCE_NAME),
                    arguments.getInstance());
            assertEquals("Connector is not match",
                    testRuleContext.getArguments().get(ManagerCorrelationRule.ARG_CONNECTOR_NAME),
                    arguments.getConnector());
            assertEquals("Link is not match",
                    testRuleContext.getArguments().get(ManagerCorrelationRule.ARG_LINK_NAME),
                    arguments.getLink());
            assertEquals("ManagerAttributeValue is not match",
                    testRuleContext.getArguments().get(ManagerCorrelationRule.ARG_MANAGER_ATTRIBUTE_VALUE_NAME),
                    arguments.getManagerAttributeValue());
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
     * - test map of execution
     * Expectation:
     * - call internalValidation
     * - call internalExecute
     */
    @Test
    public void noneNullArgumentIsNullTest() throws GeneralException {
        for (String noneNullArgument : ManagerCorrelationRule.NONE_NULL_ARGUMENTS_NAME) {

            JavaRuleContext testRuleContext = buildTestJavaRuleContext();
            testRuleContext.getArguments().remove(noneNullArgument);

            assertThrows(GeneralException.class, () -> testRule.execute(testRuleContext));
            verify(testRule).internalValidation(eq(testRuleContext));
            verify(testRule, never()).internalExecute(eq(sailPointContext), any());
        }
    }

    /**
     * Test execution with valid NULL arguments.
     * Input:
     * - valid rule context, but {@link ManagerCorrelationRule#ARG_INSTANCE_NAME} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - do not call internalExecute
     */
    @Test
    public void nullInstanceArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = new HashMap<>();
        testResult.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        testRuleContext.getArguments().remove(ManagerCorrelationRule.ARG_INSTANCE_NAME);
        when(testRule.internalExecute(eq(sailPointContext), any())).thenReturn(testResult);

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test execution with valid NULL arguments.
     * Input:
     * - valid rule context, but {@link ManagerCorrelationRule#ARG_MANAGER_ATTRIBUTE_VALUE_NAME} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - do not call internalExecute
     */
    @Test
    public void nullManagerAttributeValueArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = new HashMap<>();
        testResult.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        testRuleContext.getArguments().remove(ManagerCorrelationRule.ARG_MANAGER_ATTRIBUTE_VALUE_NAME);
        when(testRule.internalExecute(eq(sailPointContext), any())).thenReturn(testResult);

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).internalExecute(eq(sailPointContext), any());
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
        assertEquals("Rule type is not match", Rule.Type.ManagerCorrelation.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(ManagerCorrelationRule.ARG_ENVIRONMENT_NAME,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        ruleParameters.put(ManagerCorrelationRule.ARG_APPLICATION_NAME, new Application());
        ruleParameters.put(ManagerCorrelationRule.ARG_INSTANCE_NAME, UUID.randomUUID().toString());
        ruleParameters.put(ManagerCorrelationRule.ARG_CONNECTOR_NAME, mock(AbstractConnector.class));
        ruleParameters.put(ManagerCorrelationRule.ARG_LINK_NAME, mock(Link.class));
        ruleParameters.put(ManagerCorrelationRule.ARG_MANAGER_ATTRIBUTE_VALUE_NAME, UUID.randomUUID().toString());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
