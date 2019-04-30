package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.connector.BuildMapRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Application;
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
 * Test for {@link BuildMapRule} class
 */
public class BuildMapRuleTest {

    /**
     * Test instance of {@link BuildMapRule}
     */
    private BuildMapRule buildMapRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link BuildMapRuleTest#buildMapRule} and {@link BuildMapRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.buildMapRule = mock(BuildMapRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - application as in rule context args by name {@link BuildMapRule#ARG_APPLICATION_NAME}
     * - schema as in rule context args by name {@link BuildMapRule#ARG_SCHEMA_NAME}
     * - state as in rule context args by name {@link BuildMapRule#ARG_STATE_NAME}
     * - record as in rule context args by name {@link BuildMapRule#ARG_RECORD_NAME}
     * - cols as in rule context args by name {@link BuildMapRule#ARG_COLUMNS_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = new HashMap<>();
        testResult.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            BuildMapRule.BuildMapRuleArguments arguments = (BuildMapRule.BuildMapRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(BuildMapRule.ARG_APPLICATION_NAME),
                    arguments.getApplication());
            assertEquals("Schema is not match",
                    testRuleContext.getArguments().get(BuildMapRule.ARG_SCHEMA_NAME),
                    arguments.getSchema());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(BuildMapRule.ARG_STATE_NAME),
                    arguments.getState());
            assertEquals("Record is not match",
                    testRuleContext.getArguments().get(BuildMapRule.ARG_RECORD_NAME),
                    arguments.getRecord());
            assertEquals("Columns are not match",
                    testRuleContext.getArguments().get(BuildMapRule.ARG_COLUMNS_NAME),
                    arguments.getColumns());
            return testResult;
        }).when(buildMapRule).internalExecute(eq(sailPointContext), any());

        assertEquals(testResult, buildMapRule.execute(testRuleContext));
        verify(buildMapRule).internalValidation(eq(testRuleContext));
        verify(buildMapRule).execute(eq(testRuleContext));
        verify(buildMapRule).internalExecute(eq(sailPointContext), any());
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
        for (String noneNullArgument : BuildMapRule.NONE_NULL_ARGUMENTS_NAME) {

            JavaRuleContext testRuleContext = buildTestJavaRuleContext();
            testRuleContext.getArguments().remove(noneNullArgument);

            assertThrows(GeneralException.class, () -> buildMapRule.execute(testRuleContext));
            verify(buildMapRule).internalValidation(eq(testRuleContext));
            verify(buildMapRule, never()).internalExecute(eq(sailPointContext), any());
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
        assertEquals("Rule type is not match", Rule.Type.BuildMap.name(), buildMapRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(BuildMapRule.ARG_APPLICATION_NAME, new Application());
        ruleParameters.put(BuildMapRule.ARG_SCHEMA_NAME, new Schema());
        ruleParameters.put(BuildMapRule.ARG_STATE_NAME, Collections.emptyMap());
        ruleParameters.put(BuildMapRule.ARG_RECORD_NAME, Collections.singletonList(UUID.randomUUID().toString()));
        ruleParameters.put(BuildMapRule.ARG_COLUMNS_NAME, Collections.singletonList(UUID.randomUUID().toString()));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
