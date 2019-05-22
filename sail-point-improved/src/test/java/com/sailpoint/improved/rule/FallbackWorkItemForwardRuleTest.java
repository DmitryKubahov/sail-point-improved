package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.notification.FallbackWorkItemForwardRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Certification;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.WorkItem;
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
import static org.mockito.Mockito.when;

/**
 * Test for {@link FallbackWorkItemForwardRule} class
 */
public class FallbackWorkItemForwardRuleTest {

    /**
     * Test instance of {@link FallbackWorkItemForwardRule}
     */
    private FallbackWorkItemForwardRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link FallbackWorkItemForwardRuleTest#testRule} and {@link FallbackWorkItemForwardRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(FallbackWorkItemForwardRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - item as in rule context args by name {@link FallbackWorkItemForwardRule#ARG_ITEM}
     * - owner as in rule context args by name {@link FallbackWorkItemForwardRule#ARG_OWNER}
     * - creator as in rule context args by name {@link FallbackWorkItemForwardRule#ARG_CREATOR}
     * - certifiers as in rule context args by name {@link FallbackWorkItemForwardRule#ARG_CERTIFIERS}
     * - certificationName as in rule context args by name {@link FallbackWorkItemForwardRule#ARG_CERTIFICATION_NAME}
     * - certificationType as in rule context args by name {@link FallbackWorkItemForwardRule#ARG_CERTIFICATION_TYPE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Identity testResult = mock(Identity.class);

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            FallbackWorkItemForwardRule.FallbackWorkItemForwardRuleArguments arguments = (FallbackWorkItemForwardRule.FallbackWorkItemForwardRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Item is not match",
                    testRuleContext.getArguments().get(FallbackWorkItemForwardRule.ARG_ITEM),
                    arguments.getItem());
            assertEquals("Owner is not match",
                    testRuleContext.getArguments().get(FallbackWorkItemForwardRule.ARG_OWNER),
                    arguments.getOwner());
            assertEquals("Creator is not match",
                    testRuleContext.getArguments().get(FallbackWorkItemForwardRule.ARG_CREATOR),
                    arguments.getCreator());
            assertEquals("Certifiers is not match",
                    testRuleContext.getArguments().get(FallbackWorkItemForwardRule.ARG_CERTIFIERS),
                    arguments.getCertifiers());
            assertEquals("CertificationName is not match",
                    testRuleContext.getArguments().get(FallbackWorkItemForwardRule.ARG_CERTIFICATION_NAME),
                    arguments.getCertificationName());
            assertEquals("CertificationType is not match",
                    testRuleContext.getArguments().get(FallbackWorkItemForwardRule.ARG_CERTIFICATION_TYPE),
                    arguments.getCertificationType());
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
     * - valid rule context, but {@link FallbackWorkItemForwardRule#ARG_CERTIFICATION_NAME} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - call internalExecute
     */
    @Test
    public void nullCertificationAttributeValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Identity testResult = mock(Identity.class);

        testRuleContext.getArguments().remove(FallbackWorkItemForwardRule.ARG_CERTIFICATION_NAME);
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
        for (String noneNullArgument : FallbackWorkItemForwardRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.FallbackWorkItemForward.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(FallbackWorkItemForwardRule.ARG_ITEM, mock(WorkItem.class));
        ruleParameters.put(FallbackWorkItemForwardRule.ARG_OWNER, mock(Identity.class));
        ruleParameters.put(FallbackWorkItemForwardRule.ARG_CREATOR, UUID.randomUUID().toString());
        ruleParameters.put(FallbackWorkItemForwardRule.ARG_CERTIFIERS,
                Collections.singletonList(UUID.randomUUID().toString()));
        ruleParameters.put(FallbackWorkItemForwardRule.ARG_CERTIFICATION_NAME, UUID.randomUUID().toString());
        ruleParameters
                .put(FallbackWorkItemForwardRule.ARG_CERTIFICATION_TYPE, Certification.Type.ApplicationOwner);
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
