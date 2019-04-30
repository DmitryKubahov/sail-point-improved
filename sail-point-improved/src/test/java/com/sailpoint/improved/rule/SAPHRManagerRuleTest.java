package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.connector.SAPHRManagerRule;
import com.sap.conn.jco.JCoDestination;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.connector.SAPInternalConnector;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Schema;
import sailpoint.tools.GeneralException;

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
 * Test for {@link SAPHRManagerRule} class
 */
public class SAPHRManagerRuleTest {

    /**
     * Test instance of {@link SAPHRManagerRule}
     */
    private SAPHRManagerRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link SAPHRManagerRuleTest#testRule} and {@link SAPHRManagerRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(SAPHRManagerRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - application as in rule context args by name {@link SAPHRManagerRule#ARG_APPLICATION_NAME}
     * - schema as in rule context args by name {@link SAPHRManagerRule#ARG_SCHEMA_NAME}
     * - destination as in rule context args by name {@link SAPHRManagerRule#ARG_DESTINATION_NAME}
     * - connector as in rule context args by name {@link SAPHRManagerRule#ARG_CONNECTOR_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        String testResult = UUID.randomUUID().toString();

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            SAPHRManagerRule.SAPHRManagerRuleArguments arguments = (SAPHRManagerRule.SAPHRManagerRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(SAPHRManagerRule.ARG_APPLICATION_NAME),
                    arguments.getApplication());
            assertEquals("Schema is not match",
                    testRuleContext.getArguments().get(SAPHRManagerRule.ARG_SCHEMA_NAME),
                    arguments.getSchema());
            assertEquals("Destination is not match",
                    testRuleContext.getArguments().get(SAPHRManagerRule.ARG_DESTINATION_NAME),
                    arguments.getDestination());
            assertEquals("Connector is not match",
                    testRuleContext.getArguments().get(SAPHRManagerRule.ARG_CONNECTOR_NAME),
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
        for (String noneNullArgument : SAPHRManagerRule.NONE_NULL_ARGUMENTS_NAME) {

            JavaRuleContext testRuleContext = buildTestJavaRuleContext();
            testRuleContext.getArguments().remove(noneNullArgument);

            assertThrows(GeneralException.class, () -> testRule.execute(testRuleContext));
            verify(testRule).internalValidation(eq(testRuleContext));
            verify(testRule, never()).internalExecute(eq(sailPointContext), any());
        }
    }


    /**
     * Create valid java rule context for SAP build map rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(SAPHRManagerRule.ARG_APPLICATION_NAME, new Application());
        ruleParameters.put(SAPHRManagerRule.ARG_SCHEMA_NAME, new Schema());
        ruleParameters.put(SAPHRManagerRule.ARG_DESTINATION_NAME, mock(JCoDestination.class));
        ruleParameters.put(SAPHRManagerRule.ARG_CONNECTOR_NAME, mock(SAPInternalConnector.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
