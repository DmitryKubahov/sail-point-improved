package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.connector.PeopleSoftHRMSBuildMapRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.connector.PeopleSoftHRMSConnector;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Schema;
import sailpoint.tools.GeneralException;

import java.sql.Connection;
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
 * Test for {@link PeopleSoftHRMSBuildMapRule} class
 */
public class PeopleSoftHRMSBuildMapRuleTest {

    /**
     * Test instance of {@link PeopleSoftHRMSBuildMapRule}
     */
    private PeopleSoftHRMSBuildMapRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link PeopleSoftHRMSBuildMapRuleTest#testRule} and {@link PeopleSoftHRMSBuildMapRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(PeopleSoftHRMSBuildMapRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - application as in rule context args by name {@link PeopleSoftHRMSBuildMapRule#ARG_APPLICATION}
     * - schema as in rule context args by name {@link PeopleSoftHRMSBuildMapRule#ARG_SCHEMA}
     * - state as in rule context args by name {@link PeopleSoftHRMSBuildMapRule#ARG_STATE}
     * - identity as in rule context args by name {@link PeopleSoftHRMSBuildMapRule#ARG_IDENTITY}
     * - connection as in rule context args by name {@link PeopleSoftHRMSBuildMapRule#ARG_CONNECTION}
     * - connector as in rule context args by name {@link PeopleSoftHRMSBuildMapRule#ARG_CONNECTOR}
     * - map as in rule context args by name {@link PeopleSoftHRMSBuildMapRule#ARG_MAP}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID());

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            PeopleSoftHRMSBuildMapRule.PeopleSoftHRMSBuildMapRuleArguments arguments = (PeopleSoftHRMSBuildMapRule.PeopleSoftHRMSBuildMapRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(PeopleSoftHRMSBuildMapRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("Schema is not match",
                    testRuleContext.getArguments().get(PeopleSoftHRMSBuildMapRule.ARG_SCHEMA),
                    arguments.getSchema());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(PeopleSoftHRMSBuildMapRule.ARG_STATE),
                    arguments.getState());
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(PeopleSoftHRMSBuildMapRule.ARG_IDENTITY),
                    arguments.getIdentity());
            assertEquals("Connection is not match",
                    testRuleContext.getArguments().get(PeopleSoftHRMSBuildMapRule.ARG_CONNECTION),
                    arguments.getConnection());
            assertEquals("Connector is not match",
                    testRuleContext.getArguments().get(PeopleSoftHRMSBuildMapRule.ARG_CONNECTOR),
                    arguments.getConnector());
            assertEquals("Map is not match",
                    testRuleContext.getArguments().get(PeopleSoftHRMSBuildMapRule.ARG_MAP),
                    arguments.getMap());
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
        for (String noneNullArgument : PeopleSoftHRMSBuildMapRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.PeopleSoftHRMSBuildMap.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(PeopleSoftHRMSBuildMapRule.ARG_APPLICATION, new Application());
        ruleParameters.put(PeopleSoftHRMSBuildMapRule.ARG_SCHEMA, new Schema());
        ruleParameters.put(PeopleSoftHRMSBuildMapRule.ARG_STATE,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        ruleParameters.put(PeopleSoftHRMSBuildMapRule.ARG_IDENTITY, UUID.randomUUID().toString());
        ruleParameters.put(PeopleSoftHRMSBuildMapRule.ARG_CONNECTION, mock(Connection.class));
        ruleParameters.put(PeopleSoftHRMSBuildMapRule.ARG_CONNECTOR, mock(PeopleSoftHRMSConnector.class));
        ruleParameters.put(PeopleSoftHRMSBuildMapRule.ARG_MAP,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
