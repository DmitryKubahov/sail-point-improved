package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.connector.PreIterateRule;
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

import java.io.InputStream;
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
 * Test for {@link PreIterateRule} class
 */
public class PreIterateRuleTest {

    /**
     * Test instance of {@link PreIterateRule}
     */
    private PreIterateRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link PreIterateRuleTest#testRule} and {@link PreIterateRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(PreIterateRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - application as in rule context args by name {@link PreIterateRule#ARG_APPLICATION_NAME}
     * - schema as in rule context args by name {@link PreIterateRule#ARG_SCHEMA_NAME}
     * - stats as in rule context args by name {@link PreIterateRule#PreIterateRule}
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        InputStream testResult = mock(InputStream.class);

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            PreIterateRule.PreIterateRuleArguments arguments = (PreIterateRule.PreIterateRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(PreIterateRule.ARG_APPLICATION_NAME),
                    arguments.getApplication());
            assertEquals("Schema is not match",
                    testRuleContext.getArguments().get(PreIterateRule.ARG_SCHEMA_NAME),
                    arguments.getSchema());
            assertEquals("Stats is not match",
                    testRuleContext.getArguments().get(PreIterateRule.ARG_STATS_NAME),
                    arguments.getStats());
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
    public void noneNullValidationFailedTest() throws GeneralException {
        for (String noneNullArgument : PreIterateRule.NONE_NULL_ARGUMENTS_NAME) {
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
        assertEquals("Rule type is not match", Rule.Type.PreIterate.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for pre-iterate rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(PreIterateRule.ARG_APPLICATION_NAME, new Application());
        ruleParameters.put(PreIterateRule.ARG_SCHEMA_NAME, new Schema());
        ruleParameters.put(PreIterateRule.ARG_STATS_NAME, Collections.emptyMap());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
