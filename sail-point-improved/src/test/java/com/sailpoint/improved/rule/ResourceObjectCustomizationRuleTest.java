package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.aggregation.ResourceObjectCustomizationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.connector.AbstractConnector;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ResourceObject;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;

import java.util.Collections;
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
 * Test for {@link ResourceObjectCustomizationRule} class
 */
public class ResourceObjectCustomizationRuleTest {

    /**
     * Test instance of {@link ResourceObjectCustomizationRule}
     */
    private ResourceObjectCustomizationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link ResourceObjectCustomizationRuleTest#testRule} and {@link ResourceObjectCustomizationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(ResourceObjectCustomizationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test resource object
     * Expectation:
     * - object as in rule context args by name {@link ResourceObjectCustomizationRule#ARG_OBJECT}
     * - application as in rule context args by name {@link ResourceObjectCustomizationRule#ARG_APPLICATION}
     * - connector as in rule context args by name {@link ResourceObjectCustomizationRule#ARG_CONNECTOR}
     * - state as in rule context args by name {@link ResourceObjectCustomizationRule#ARG_STATE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        ResourceObject testResult = mock(ResourceObject.class);

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            ResourceObjectCustomizationRule.ResourceObjectCustomizationRuleArguments arguments = (ResourceObjectCustomizationRule.ResourceObjectCustomizationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Object is not match",
                    testRuleContext.getArguments().get(ResourceObjectCustomizationRule.ARG_OBJECT),
                    arguments.getObject());
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(ResourceObjectCustomizationRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("Connector is not match",
                    testRuleContext.getArguments().get(ResourceObjectCustomizationRule.ARG_CONNECTOR),
                    arguments.getConnector());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(ResourceObjectCustomizationRule.ARG_STATE),
                    arguments.getState());
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
        for (String noneNullArgument : ResourceObjectCustomizationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.ResourceObjectCustomization.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(ResourceObjectCustomizationRule.ARG_OBJECT, mock(ResourceObject.class));
        ruleParameters.put(ResourceObjectCustomizationRule.ARG_APPLICATION, new Application());
        ruleParameters.put(ResourceObjectCustomizationRule.ARG_CONNECTOR, mock(AbstractConnector.class));
        ruleParameters.put(ResourceObjectCustomizationRule.ARG_STATE, Collections.emptyMap());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
