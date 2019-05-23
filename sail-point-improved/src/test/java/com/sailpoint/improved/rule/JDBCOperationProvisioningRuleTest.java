package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.provisioning.JDBCOperationProvisioningRule;
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
 * Test for {@link JDBCOperationProvisioningRule} class
 */
public class JDBCOperationProvisioningRuleTest {

    /**
     * Test instance of {@link JDBCOperationProvisioningRule}
     */
    private JDBCOperationProvisioningRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link JDBCOperationProvisioningRuleTest#testRule} and {@link JDBCOperationProvisioningRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(JDBCOperationProvisioningRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test provisioning result value
     * Expectation:
     * - application as in rule context args by name {@link JDBCOperationProvisioningRule#ARG_APPLICATION}
     * - schema as in rule context args by name {@link JDBCOperationProvisioningRule#ARG_SCHEMA}
     * - connection as in rule context args by name {@link JDBCOperationProvisioningRule#ARG_CONNECTION}
     * - plan as in rule context args by name {@link JDBCOperationProvisioningRule#ARG_PLAN}
     * - request as in rule context args by name {@link JDBCOperationProvisioningRule#ARG_REQUEST}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        ProvisioningResult testResult = mock(ProvisioningResult.class);

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            JDBCOperationProvisioningRule.JDBCOperationProvisioningRuleArguments arguments = (JDBCOperationProvisioningRule.JDBCOperationProvisioningRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(JDBCOperationProvisioningRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("Schema is not match",
                    testRuleContext.getArguments().get(JDBCOperationProvisioningRule.ARG_SCHEMA),
                    arguments.getSchema());
            assertEquals("Connection is not match",
                    testRuleContext.getArguments().get(JDBCOperationProvisioningRule.ARG_CONNECTION),
                    arguments.getConnection());
            assertEquals("Plan is not match",
                    testRuleContext.getArguments().get(JDBCOperationProvisioningRule.ARG_PLAN),
                    arguments.getPlan());
            assertEquals("Request is not match",
                    testRuleContext.getArguments().get(JDBCOperationProvisioningRule.ARG_REQUEST),
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
        for (String noneNullArgument : JDBCOperationProvisioningRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.JDBCOperationProvisioning.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(JDBCOperationProvisioningRule.ARG_APPLICATION, mock(Application.class));
        ruleParameters.put(JDBCOperationProvisioningRule.ARG_SCHEMA, mock(Schema.class));
        ruleParameters.put(JDBCOperationProvisioningRule.ARG_CONNECTION, mock(Connection.class));
        ruleParameters.put(JDBCOperationProvisioningRule.ARG_PLAN, mock(ProvisioningPlan.class));
        ruleParameters
                .put(JDBCOperationProvisioningRule.ARG_REQUEST, mock(ProvisioningPlan.AbstractRequest.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
