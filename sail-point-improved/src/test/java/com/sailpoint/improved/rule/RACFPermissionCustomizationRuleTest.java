package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.connector.RACFPermissionCustomizationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Permission;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;

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
 * Test for {@link RACFPermissionCustomizationRule} class
 */
public class RACFPermissionCustomizationRuleTest {

    /**
     * Test instance of {@link RACFPermissionCustomizationRule}
     */
    private RACFPermissionCustomizationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link RACFPermissionCustomizationRuleTest#testRule} and {@link RACFPermissionCustomizationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(RACFPermissionCustomizationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test permission value
     * Expectation:
     * - permission as in rule context args by name {@link RACFPermissionCustomizationRule#ARG_PERMISSION}
     * - line as in rule context args by name {@link RACFPermissionCustomizationRule#ARG_LINE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Permission testResult = mock(Permission.class);

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            RACFPermissionCustomizationRule.RACFPermissionCustomizationRuleArguments arguments = (RACFPermissionCustomizationRule.RACFPermissionCustomizationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Permission is not match",
                    testRuleContext.getArguments().get(RACFPermissionCustomizationRule.ARG_PERMISSION),
                    arguments.getPermission());
            assertEquals("Line is not match",
                    testRuleContext.getArguments().get(RACFPermissionCustomizationRule.ARG_LINE),
                    arguments.getLine());
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
        for (String noneNullArgument : RACFPermissionCustomizationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.RACFPermissionCustomization.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(RACFPermissionCustomizationRule.ARG_PERMISSION, mock(Permission.class));
        ruleParameters.put(RACFPermissionCustomizationRule.ARG_LINE, UUID.randomUUID().toString());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
