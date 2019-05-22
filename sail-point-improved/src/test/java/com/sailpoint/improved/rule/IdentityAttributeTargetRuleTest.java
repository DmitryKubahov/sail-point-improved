package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.mapping.IdentityAttributeTargetRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.AttributeTarget;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ObjectAttribute;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningProject;
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
import static org.mockito.Mockito.when;

/**
 * Test for {@link IdentityAttributeTargetRule} class
 */
public class IdentityAttributeTargetRuleTest {

    /**
     * Test instance of {@link IdentityAttributeTargetRule}
     */
    private IdentityAttributeTargetRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link IdentityAttributeTargetRuleTest#testRule} and {@link IdentityAttributeTargetRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(IdentityAttributeTargetRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - value as in rule context args by name {@link IdentityAttributeTargetRule#ARG_VALUE}
     * - sourceIdentityAttribute as in rule context args by name {@link IdentityAttributeTargetRule#ARG_SOURCE_IDENTITY_ATTRIBUTE}
     * - sourceIdentityAttributeName as in rule context args by name {@link IdentityAttributeTargetRule#ARG_SOURCE_IDENTITY_ATTRIBUTE_NAME}
     * - sourceAttributeRequest as in rule context args by name {@link IdentityAttributeTargetRule#ARG_SOURCE_ATTRIBUTE_REQUEST}
     * - target as in rule context args by name {@link IdentityAttributeTargetRule#ARG_TARGET}
     * - identity as in rule context args by name {@link IdentityAttributeTargetRule#ARG_IDENTITY}
     * - project as in rule context args by name {@link IdentityAttributeTargetRule#ARG_PROJECT}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Object testResult = UUID.randomUUID();

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            IdentityAttributeTargetRule.IdentityAttributeTargetRuleArguments arguments = (IdentityAttributeTargetRule.IdentityAttributeTargetRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Value is not match",
                    testRuleContext.getArguments().get(IdentityAttributeTargetRule.ARG_VALUE),
                    arguments.getValue());
            assertEquals("SourceIdentityAttribute is not match",
                    testRuleContext.getArguments().get(IdentityAttributeTargetRule.ARG_SOURCE_IDENTITY_ATTRIBUTE),
                    arguments.getSourceIdentityAttribute());
            assertEquals("SourceIdentityAttributeName is not match",
                    testRuleContext.getArguments().get(IdentityAttributeTargetRule.ARG_SOURCE_IDENTITY_ATTRIBUTE_NAME),
                    arguments.getSourceIdentityAttributeName());
            assertEquals("SourceAttributeRequest is not match",
                    testRuleContext.getArguments().get(IdentityAttributeTargetRule.ARG_SOURCE_ATTRIBUTE_REQUEST),
                    arguments.getSourceAttributeRequest());
            assertEquals("Target is not match",
                    testRuleContext.getArguments().get(IdentityAttributeTargetRule.ARG_TARGET),
                    arguments.getTarget());
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(IdentityAttributeTargetRule.ARG_IDENTITY),
                    arguments.getIdentity());
            assertEquals("Project is not match",
                    testRuleContext.getArguments().get(IdentityAttributeTargetRule.ARG_PROJECT),
                    arguments.getProject());
            return testResult;
        }).when(testRule).internalExecute(eq(sailPointContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test execution with valid NULL arguments.
     * Input:
     * - valid rule context, but {@link IdentityAttributeTargetRule#ARG_VALUE} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - call internalExecute
     */
    @Test
    public void nullValueArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Object testResult = UUID.randomUUID();

        testRuleContext.getArguments().remove(IdentityAttributeTargetRule.ARG_VALUE);
        when(testRule.internalExecute(eq(sailPointContext), any())).thenReturn(testResult);

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
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
        for (String noneNullArgument : IdentityAttributeTargetRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.IdentityAttributeTarget.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(IdentityAttributeTargetRule.ARG_VALUE, UUID.randomUUID());
        ruleParameters.put(IdentityAttributeTargetRule.ARG_SOURCE_IDENTITY_ATTRIBUTE, mock(ObjectAttribute.class));
        ruleParameters
                .put(IdentityAttributeTargetRule.ARG_SOURCE_IDENTITY_ATTRIBUTE_NAME, UUID.randomUUID().toString());
        ruleParameters.put(IdentityAttributeTargetRule.ARG_SOURCE_ATTRIBUTE_REQUEST,
                mock(ProvisioningPlan.AttributeRequest.class));
        ruleParameters.put(IdentityAttributeTargetRule.ARG_TARGET, mock(AttributeTarget.class));
        ruleParameters.put(IdentityAttributeTargetRule.ARG_IDENTITY, mock(Identity.class));
        ruleParameters.put(IdentityAttributeTargetRule.ARG_PROJECT, mock(ProvisioningProject.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
