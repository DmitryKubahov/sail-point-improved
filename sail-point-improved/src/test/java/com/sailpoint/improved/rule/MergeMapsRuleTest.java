package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.connector.MergeMapsRule;
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
 * Test for {@link MergeMapsRule} class
 */
public class MergeMapsRuleTest {

    /**
     * Test instance of {@link MergeMapsRule}
     */
    private MergeMapsRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link MergeMapsRuleTest#testRule} and {@link MergeMapsRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(MergeMapsRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - application as in rule context args by name {@link MergeMapsRule#ARG_APPLICATION}
     * - schema as in rule context args by name {@link MergeMapsRule#ARG_SCHEMA}
     * - current as in rule context args by name {@link MergeMapsRule#ARG_CURRENT}
     * - newObject as in rule context args by name {@link MergeMapsRule#ARG_NEW_OBJECT}
     * - mergeAttrs as in rule context args by name {@link MergeMapsRule#ARG_MERGE_ATTRS}
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID());

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            MergeMapsRule.MergeMapsRuleArguments arguments = (MergeMapsRule.MergeMapsRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(MergeMapsRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("Schema is not match",
                    testRuleContext.getArguments().get(MergeMapsRule.ARG_SCHEMA),
                    arguments.getSchema());
            assertEquals("Current is not match",
                    testRuleContext.getArguments().get(MergeMapsRule.ARG_CURRENT),
                    arguments.getCurrent());
            assertEquals("NewObject is not match",
                    testRuleContext.getArguments().get(MergeMapsRule.ARG_NEW_OBJECT),
                    arguments.getNewObject());
            assertEquals("MergeAttrs is not match",
                    testRuleContext.getArguments().get(MergeMapsRule.ARG_MERGE_ATTRS),
                    arguments.getMergeAttrs());
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
        for (String noneNullArgument : MergeMapsRule.NONE_NULL_ARGUMENTS_NAME) {
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
        assertEquals("Rule type is not match", Rule.Type.MergeMaps.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(MergeMapsRule.ARG_APPLICATION, new Application());
        ruleParameters.put(MergeMapsRule.ARG_SCHEMA, new Schema());
        ruleParameters.put(MergeMapsRule.ARG_CURRENT,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        ruleParameters.put(MergeMapsRule.ARG_NEW_OBJECT,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        ruleParameters.put(MergeMapsRule.ARG_MERGE_ATTRS, Collections.singletonList(UUID.randomUUID().toString()));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
