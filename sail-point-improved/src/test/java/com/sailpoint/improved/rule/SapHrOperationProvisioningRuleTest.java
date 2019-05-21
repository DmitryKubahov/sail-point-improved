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
     * - application as in rule context args by name {@link SapHrOperationProvisioningRule#ARG_APPLICATION_NAME}
     * - schema as in rule context args by name {@link SapHrOperationProvisioningRule#ARG_SCHEMA_NAME}
     * - destination as in rule context args by name {@link SapHrOperationProvisioningRule#ARG_DESTINATION_NAME}
     * - connection as in rule context args by name {@link SapHrOperationProvisioningRule#ARG_CONNECTION_NAME}
     * - plan as in rule context args by name {@link SapHrOperationProvisioningRule#ARG_PLAN_NAME}
     * - request as in rule context args by name {@link SapHrOperationProvisioningRule#ARG_REQUEST_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        ProvisioningResult testResult = mock(ProvisioningResult.class);

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            SapHrOperationProvisioningRule.SapHrOperationProvisioningRuleArguments arguments = (SapHrOperationProvisioningRule.SapHrOperationProvisioningRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(SapHrOperationProvisioningRule.ARG_APPLICATION_NAME),
                    arguments.getApplication());
            assertEquals("Schema is not match",
                    testRuleContext.getArguments().get(SapHrOperationProvisioningRule.ARG_SCHEMA_NAME),
                    arguments.getSchema());
            assertEquals("Destination is not match",
                    testRuleContext.getArguments().get(SapHrOperationProvisioningRule.ARG_DESTINATION_NAME),
                    arguments.getDestination());
            assertEquals("Connection is not match",
                    testRuleContext.getArguments().get(SapHrOperationProvisioningRule.ARG_CONNECTION_NAME),
                    arguments.getConnection());
            assertEquals("Plan is not match",
                    testRuleContext.getArguments().get(SapHrOperationProvisioningRule.ARG_PLAN_NAME),
                    arguments.getPlan());
            assertEquals("Request is not match",
                    testRuleContext.getArguments().get(SapHrOperationProvisioningRule.ARG_REQUEST_NAME),
                    arguments.getRequest());
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
        for (String noneNullArgument : SapHrOperationProvisioningRule.NONE_NULL_ARGUMENTS_NAME) {

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
        ruleParameters.put(SapHrOperationProvisioningRule.ARG_APPLICATION_NAME, mock(Application.class));
        ruleParameters.put(SapHrOperationProvisioningRule.ARG_SCHEMA_NAME, mock(Schema.class));
        ruleParameters.put(SapHrOperationProvisioningRule.ARG_DESTINATION_NAME, mock(JCoDestination.class));
        ruleParameters.put(SapHrOperationProvisioningRule.ARG_CONNECTION_NAME, mock(Connection.class));
        ruleParameters.put(SapHrOperationProvisioningRule.ARG_PLAN_NAME, mock(ProvisioningPlan.class));
        ruleParameters
                .put(SapHrOperationProvisioningRule.ARG_REQUEST_NAME, mock(ProvisioningPlan.AbstractRequest.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}