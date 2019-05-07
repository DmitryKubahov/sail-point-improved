package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.connector.FileParsingRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Application;
import sailpoint.object.Attributes;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Schema;
import sailpoint.tools.GeneralException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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
 * Test for {@link FileParsingRule} class
 */
public class FileParsingRuleTest {

    /**
     * Test instance of {@link FileParsingRule}
     */
    private FileParsingRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link FileParsingRuleTest#testRule} and {@link FileParsingRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(FileParsingRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - application as in rule context args by name {@link FileParsingRule#ARG_APPLICATION_NAME}
     * - schema as in rule context args by name {@link FileParsingRule#ARG_SCHEMA_NAME}
     * - config as in rule context args by name {@link FileParsingRule#ARG_CONFIG_NAME}
     * - inputStream as in rule context args by name {@link FileParsingRule#ARG_INPUT_STREAM_NAME}
     * - reader as in rule context args by name {@link FileParsingRule#ARG_READER_NAME}
     * - state as in rule context args by name {@link FileParsingRule#ARG_STATE_NAME}
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID());

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            FileParsingRule.FileParsingRuleArguments arguments = (FileParsingRule.FileParsingRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(FileParsingRule.ARG_APPLICATION_NAME),
                    arguments.getApplication());
            assertEquals("Schema is not match",
                    testRuleContext.getArguments().get(FileParsingRule.ARG_SCHEMA_NAME),
                    arguments.getSchema());
            assertEquals("Config is not match",
                    testRuleContext.getArguments().get(FileParsingRule.ARG_CONFIG_NAME),
                    arguments.getConfig());
            assertEquals("InputStream is not match",
                    testRuleContext.getArguments().get(FileParsingRule.ARG_INPUT_STREAM_NAME),
                    arguments.getInputStream());
            assertEquals("Reader is not match",
                    testRuleContext.getArguments().get(FileParsingRule.ARG_READER_NAME),
                    arguments.getReader());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(FileParsingRule.ARG_STATE_NAME),
                    arguments.getState());
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
        for (String noneNullArgument : FileParsingRule.NONE_NULL_ARGUMENTS_NAME) {
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
        assertEquals("Rule type is not match", Rule.Type.FileParsingRule.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(FileParsingRule.ARG_APPLICATION_NAME, new Application());
        ruleParameters.put(FileParsingRule.ARG_SCHEMA_NAME, new Schema());
        ruleParameters.put(FileParsingRule.ARG_CONFIG_NAME, mock(Attributes.class));
        ruleParameters.put(FileParsingRule.ARG_INPUT_STREAM_NAME, mock(BufferedInputStream.class));
        ruleParameters.put(FileParsingRule.ARG_READER_NAME, mock(BufferedReader.class));
        ruleParameters.put(FileParsingRule.ARG_STATE_NAME,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
