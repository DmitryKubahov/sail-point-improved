package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.certification.CertificationEntityCustomizationRule;
import com.sailpoint.improved.rule.certification.CertificationSignOffApproverRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Certification;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
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
 * Test for {@link CertificationSignOffApproverRule} class
 */
public class CertificationSignOffApproverRuleTest {

    /**
     * Test instance of {@link CertificationSignOffApproverRule}
     */
    private CertificationSignOffApproverRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link CertificationSignOffApproverRuleTest#testRule} and {@link CertificationSignOffApproverRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(CertificationSignOffApproverRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map object value
     * Expectation:
     * - certification as in rule context args by name {@link CertificationSignOffApproverRule#ARG_CERTIFICATION}
     * - certifier as in rule context args by name {@link CertificationSignOffApproverRule#ARG_CERTIFIER}
     * - state as in rule context args by name {@link CertificationSignOffApproverRule#ARG_STATE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID());

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            CertificationSignOffApproverRule.CertificationSignOffApproverRuleArguments arguments = (CertificationSignOffApproverRule.CertificationSignOffApproverRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Certification is not match",
                    testRuleContext.getArguments()
                            .get(CertificationSignOffApproverRule.ARG_CERTIFICATION),
                    arguments.getCertification());
            assertEquals("Certifier is not match",
                    testRuleContext.getArguments()
                            .get(CertificationSignOffApproverRule.ARG_CERTIFIER),
                    arguments.getCertifier());
            assertEquals("State is not match",
                    testRuleContext.getArguments()
                            .get(CertificationSignOffApproverRule.ARG_STATE),
                    arguments.getState());
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
        for (String noneNullArgument : CertificationSignOffApproverRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.CertificationSignOffApprover.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(CertificationSignOffApproverRule.ARG_CERTIFICATION, mock(Certification.class));
        ruleParameters.put(CertificationSignOffApproverRule.ARG_CERTIFIER, mock(Identity.class));
        ruleParameters.put(CertificationEntityCustomizationRule.ARG_STATE,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
