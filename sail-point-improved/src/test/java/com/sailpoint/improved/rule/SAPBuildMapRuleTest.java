package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.connector.SAPBuildMapRule;
import com.sap.conn.jco.JCoDestination;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.connector.SAPInternalConnector;
import sailpoint.object.Application;
import sailpoint.object.Attributes;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Schema;
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
 * Test for {@link SAPBuildMapRule} class
 */
public class SAPBuildMapRuleTest {

    /**
     * Test instance of {@link SAPBuildMapRule}
     */
    private SAPBuildMapRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link SAPBuildMapRuleTest#testRule} and {@link SAPBuildMapRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(SAPBuildMapRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - application as in rule context args by name {@link SAPBuildMapRule#ARG_APPLICATION_NAME}
     * - schema as in rule context args by name {@link SAPBuildMapRule#ARG_SCHEMA_NAME}
     * - state as in rule context args by name {@link SAPBuildMapRule#ARG_STATE_NAME}
     * - destination as in rule context args by name {@link SAPBuildMapRule#ARG_DESTINATION_NAME}
     * - object as in rule context args by name {@link SAPBuildMapRule#ARG_OBJECT_NAME}
     * - connector as in rule context args by name {@link SAPBuildMapRule#ARG_CONNECTOR_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Attributes testResult = mock(Attributes.class);

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            SAPBuildMapRule.SAPBuildMapRuleArguments arguments = (SAPBuildMapRule.SAPBuildMapRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(SAPBuildMapRule.ARG_APPLICATION_NAME),
                    arguments.getApplication());
            assertEquals("Schema is not match",
                    testRuleContext.getArguments().get(SAPBuildMapRule.ARG_SCHEMA_NAME),
                    arguments.getSchema());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(SAPBuildMapRule.ARG_STATE_NAME),
                    arguments.getState());
            assertEquals("Destination is not match",
                    testRuleContext.getArguments().get(SAPBuildMapRule.ARG_DESTINATION_NAME),
                    arguments.getDestination());
            assertEquals("Object is not match",
                    testRuleContext.getArguments().get(SAPBuildMapRule.ARG_OBJECT_NAME),
                    arguments.getObject());
            assertEquals("Connector is not match",
                    testRuleContext.getArguments().get(SAPBuildMapRule.ARG_CONNECTOR_NAME),
                    arguments.getConnector());
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
        for (String noneNullArgument : SAPBuildMapRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.SAPBuildMap.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(SAPBuildMapRule.ARG_APPLICATION_NAME, new Application());
        ruleParameters.put(SAPBuildMapRule.ARG_SCHEMA_NAME, new Schema());
        ruleParameters.put(SAPBuildMapRule.ARG_STATE_NAME, Collections.emptyMap());
        ruleParameters.put(SAPBuildMapRule.ARG_DESTINATION_NAME, mock(JCoDestination.class));
        ruleParameters.put(SAPBuildMapRule.ARG_OBJECT_NAME, mock(Attributes.class));
        ruleParameters.put(SAPBuildMapRule.ARG_CONNECTOR_NAME, mock(SAPInternalConnector.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
