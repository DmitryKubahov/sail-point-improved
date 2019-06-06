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
import sailpoint.object.Rule;
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
     * - test random object value
     * Expectation:
     * - application as in rule context args by name {@link SAPHRManagerRule#ARG_APPLICATION}
     * - schema as in rule context args by name {@link SAPHRManagerRule#ARG_SCHEMA}
     * - destination as in rule context args by name {@link SAPHRManagerRule#ARG_DESTINATION}
     * - connector as in rule context args by name {@link SAPHRManagerRule#ARG_CONNECTOR}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        String testResult = UUID.randomUUID().toString();

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            SAPHRManagerRule.SAPHRManagerRuleArguments arguments = (SAPHRManagerRule.SAPHRManagerRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(SAPHRManagerRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("Schema is not match",
                    testRuleContext.getArguments().get(SAPHRManagerRule.ARG_SCHEMA),
                    arguments.getSchema());
            assertEquals("Destination is not match",
                    testRuleContext.getArguments().get(SAPHRManagerRule.ARG_DESTINATION),
                    arguments.getDestination());
            assertEquals("Connector is not match",
                    testRuleContext.getArguments().get(SAPHRManagerRule.ARG_CONNECTOR),
                    arguments.getConnector());
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
        for (String noneNullArgument : SAPHRManagerRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.SAPHRManagerRule.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(SAPHRManagerRule.ARG_APPLICATION, new Application());
        ruleParameters.put(SAPHRManagerRule.ARG_SCHEMA, new Schema());
        ruleParameters.put(SAPHRManagerRule.ARG_DESTINATION, mock(JCoDestination.class));
        ruleParameters.put(SAPHRManagerRule.ARG_CONNECTOR, mock(SAPInternalConnector.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
