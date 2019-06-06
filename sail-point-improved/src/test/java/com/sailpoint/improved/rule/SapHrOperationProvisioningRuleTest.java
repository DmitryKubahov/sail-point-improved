package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.provisioning.SapHrOperationProvisioningRule;
import com.sap.conn.jco.JCoDestination;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningResult;
import sailpoint.object.Rule;
import sailpoint.object.Schema;
import sailpoint.tools.GeneralException;

import java.sql.Connection;
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
 * Test for {@link SapHrOperationProvisioningRule} class
 */
public class SapHrOperationProvisioningRuleTest {

    /**
     * Test instance of {@link SapHrOperationProvisioningRule}
     */
    private SapHrOperationProvisioningRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link SapHrOperationProvisioningRuleTest#testRule} and {@link SapHrOperationProvisioningRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(SapHrOperationProvisioningRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test provisioning result value
     * Expectation:
     * - application as in rule context args by name {@link SapHrOperationProvisioningRule#ARG_APPLICATION}
     * - schema as in rule context args by name {@link SapHrOperationProvisioningRule#ARG_SCHEMA}
     * - destination as in rule context args by name {@link SapHrOperationProvisioningRule#ARG_DESTINATION}
     * - connection as in rule context args by name {@link SapHrOperationProvisioningRule#ARG_CONNECTION}
     * - plan as in rule context args by name {@link SapHrOperationProvisioningRule#ARG_PLAN}
     * - request as in rule context args by name {@link SapHrOperationProvisioningRule#ARG_REQUEST}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        ProvisioningResult testResult = mock(ProvisioningResult.class);

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            SapHrOperationProvisioningRule.SapHrOperationProvisioningRuleArguments arguments = (SapHrOperationProvisioningRule.SapHrOperationProvisioningRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(SapHrOperationProvisioningRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("Schema is not match",
                    testRuleContext.getArguments().get(SapHrOperationProvisioningRule.ARG_SCHEMA),
                    arguments.getSchema());
            assertEquals("Destination is not match",
                    testRuleContext.getArguments().get(SapHrOperationProvisioningRule.ARG_DESTINATION),
                    arguments.getDestination());
            assertEquals("Connection is not match",
                    testRuleContext.getArguments().get(SapHrOperationProvisioningRule.ARG_CONNECTION),
                    arguments.getConnection());
            assertEquals("Plan is not match",
                    testRuleContext.getArguments().get(SapHrOperationProvisioningRule.ARG_PLAN),
                    arguments.getPlan());
            assertEquals("Request is not match",
                    testRuleContext.getArguments().get(SapHrOperationProvisioningRule.ARG_REQUEST),
                    arguments.getRequest());
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
        for (String noneNullArgument : SapHrOperationProvisioningRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.SapHrOperationProvisioning.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(SapHrOperationProvisioningRule.ARG_APPLICATION, mock(Application.class));
        ruleParameters.put(SapHrOperationProvisioningRule.ARG_SCHEMA, mock(Schema.class));
        ruleParameters.put(SapHrOperationProvisioningRule.ARG_DESTINATION, mock(JCoDestination.class));
        ruleParameters.put(SapHrOperationProvisioningRule.ARG_CONNECTION, mock(Connection.class));
        ruleParameters.put(SapHrOperationProvisioningRule.ARG_PLAN, mock(ProvisioningPlan.class));
        ruleParameters
                .put(SapHrOperationProvisioningRule.ARG_REQUEST, mock(ProvisioningPlan.AbstractRequest.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
