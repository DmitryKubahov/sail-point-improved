package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.connector.JDBCBuildMapRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Schema;
import sailpoint.tools.GeneralException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.sailpoint.improved.JUnit4Helper.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test for {@link JDBCBuildMapRule} class
 */
public class JDBCBuildMapRuleTest {

    /**
     * Test instance of {@link JDBCBuildMapRule}
     */
    private JDBCBuildMapRule jdbcBuildMapRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link JDBCBuildMapRuleTest#jdbcBuildMapRule} and {@link JDBCBuildMapRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.jdbcBuildMapRule = mock(JDBCBuildMapRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - application as in rule context args by name {@link JDBCBuildMapRule#ARG_APPLICATION_NAME}
     * - schema as in rule context args by name {@link JDBCBuildMapRule#ARG_SCHEMA_NAME}
     * - state as in rule context args by name {@link JDBCBuildMapRule#ARG_STATE_NAME}
     * - connection as in rule context args by name {@link JDBCBuildMapRule#ARG_CONNECTION_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = new HashMap<>();
        testResult.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            JDBCBuildMapRule.JDBCBuildMapRuleArguments arguments = (JDBCBuildMapRule.JDBCBuildMapRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(JDBCBuildMapRule.ARG_APPLICATION_NAME),
                    arguments.getApplication());
            assertEquals("Schema is not match",
                    testRuleContext.getArguments().get(JDBCBuildMapRule.ARG_SCHEMA_NAME),
                    arguments.getSchema());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(JDBCBuildMapRule.ARG_STATE_NAME),
                    arguments.getState());
            assertEquals("Connection is not match",
                    testRuleContext.getArguments().get(JDBCBuildMapRule.ARG_CONNECTION_NAME),
                    arguments.getConnection());
            return testResult;
        }).when(jdbcBuildMapRule).internalExecute(eq(sailPointContext), any());

        assertEquals(testResult, jdbcBuildMapRule.execute(testRuleContext));
        verify(jdbcBuildMapRule).internalValidation(eq(testRuleContext));
        verify(jdbcBuildMapRule).execute(eq(testRuleContext));
        verify(jdbcBuildMapRule).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test execution with invalid arguments.
     * Input:
     * - invalid rule context: application argument is null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - do not call internalExecute
     */
    @Test
    public void applicationNullTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(JDBCBuildMapRule.ARG_APPLICATION_NAME);

        assertThrows(GeneralException.class, () -> jdbcBuildMapRule.execute(testRuleContext));
        verify(jdbcBuildMapRule).internalValidation(eq(testRuleContext));
        verify(jdbcBuildMapRule, never()).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test execution with invalid arguments.
     * Input:
     * - invalid rule context: schema argument is null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - do not call internalExecute
     */
    @Test
    public void schemaNullTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(JDBCBuildMapRule.ARG_SCHEMA_NAME);

        assertThrows(GeneralException.class, () -> jdbcBuildMapRule.execute(testRuleContext));
        verify(jdbcBuildMapRule).internalValidation(eq(testRuleContext));
        verify(jdbcBuildMapRule, never()).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test execution with invalid arguments.
     * Input:
     * - invalid rule context: state argument is null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - do not call internalExecute
     */
    @Test
    public void stateNullTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(JDBCBuildMapRule.ARG_STATE_NAME);

        assertThrows(GeneralException.class, () -> jdbcBuildMapRule.execute(testRuleContext));
        verify(jdbcBuildMapRule).internalValidation(eq(testRuleContext));
        verify(jdbcBuildMapRule, never()).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test execution with invalid arguments.
     * Input:
     * - invalid rule context: resutl set argument is null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - do not call internalExecute
     */
    @Test
    public void resultSetNullTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(JDBCBuildMapRule.ARG_RESULT_SET_NAME);

        assertThrows(GeneralException.class, () -> jdbcBuildMapRule.execute(testRuleContext));
        verify(jdbcBuildMapRule).internalValidation(eq(testRuleContext));
        verify(jdbcBuildMapRule, never()).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test execution with invalid arguments.
     * Input:
     * - invalid rule context: connection argument is null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - do not call internalExecute
     */
    @Test
    public void connectionNullTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(JDBCBuildMapRule.ARG_CONNECTION_NAME);

        assertThrows(GeneralException.class, () -> jdbcBuildMapRule.execute(testRuleContext));
        verify(jdbcBuildMapRule).internalValidation(eq(testRuleContext));
        verify(jdbcBuildMapRule, never()).internalExecute(eq(sailPointContext), any());
    }


    /**
     * Create valid java rule context for jdbc build map rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(JDBCBuildMapRule.ARG_APPLICATION_NAME, new Application());
        ruleParameters.put(JDBCBuildMapRule.ARG_SCHEMA_NAME, new Schema());
        ruleParameters.put(JDBCBuildMapRule.ARG_STATE_NAME, Collections.emptyMap());
        ruleParameters.put(JDBCBuildMapRule.ARG_RESULT_SET_NAME, mock(ResultSet.class));
        ruleParameters.put(JDBCBuildMapRule.ARG_CONNECTION_NAME, mock(Connection.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
