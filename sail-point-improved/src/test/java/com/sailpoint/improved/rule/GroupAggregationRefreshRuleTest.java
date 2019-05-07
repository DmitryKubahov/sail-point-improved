package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.aggregation.GroupAggregationRefreshRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ManagedAttribute;
import sailpoint.object.ResourceObject;
import sailpoint.object.Rule;
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
 * Test for {@link GroupAggregationRefreshRule} class
 */
public class GroupAggregationRefreshRuleTest {

    /**
     * Test instance of {@link GroupAggregationRefreshRule}
     */
    private GroupAggregationRefreshRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link GroupAggregationRefreshRuleTest#testRule} and {@link GroupAggregationRefreshRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(GroupAggregationRefreshRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - environment as in rule context args by name {@link GroupAggregationRefreshRule#ARG_ENVIRONMENT_NAME}
     * - obj as in rule context args by name {@link GroupAggregationRefreshRule#ARG_OBJECT_NAME}
     * - accountGroup as in rule context args by name {@link GroupAggregationRefreshRule#ARG_ACCOUNT_GROUP_NAME}
     * - groupApplication as in rule context args by name {@link GroupAggregationRefreshRule#ARG_GROUP_APPLICATION_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID());

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            GroupAggregationRefreshRule.GroupAggregationRefreshRuleArguments arguments = (GroupAggregationRefreshRule.GroupAggregationRefreshRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Environment is not match",
                    testRuleContext.getArguments().get(GroupAggregationRefreshRule.ARG_ENVIRONMENT_NAME),
                    arguments.getEnvironment());
            assertEquals("Obj is not match",
                    testRuleContext.getArguments().get(GroupAggregationRefreshRule.ARG_OBJECT_NAME),
                    arguments.getObject());
            assertEquals("AccountGroup is not match",
                    testRuleContext.getArguments().get(GroupAggregationRefreshRule.ARG_ACCOUNT_GROUP_NAME),
                    arguments.getAccountGroup());
            assertEquals("GroupApplication is not match",
                    testRuleContext.getArguments().get(GroupAggregationRefreshRule.ARG_GROUP_APPLICATION_NAME),
                    arguments.getGroupApplication());
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
        for (String noneNullArgument : GroupAggregationRefreshRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.GroupAggregationRefresh.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(GroupAggregationRefreshRule.ARG_ENVIRONMENT_NAME,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        ruleParameters.put(GroupAggregationRefreshRule.ARG_OBJECT_NAME, mock(ResourceObject.class));
        ruleParameters.put(GroupAggregationRefreshRule.ARG_ACCOUNT_GROUP_NAME, mock(ManagedAttribute.class));
        ruleParameters.put(GroupAggregationRefreshRule.ARG_GROUP_APPLICATION_NAME, mock(Application.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
