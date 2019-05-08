package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.certification.CertificationPhaseChangeRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Certification;
import sailpoint.object.CertificationItem;
import sailpoint.object.JavaRuleContext;
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
 * Test for {@link CertificationPhaseChangeRule} class
 */
public class CertificationPhaseChangeRuleTest {

    /**
     * Test instance of {@link CertificationPhaseChangeRule}
     */
    private CertificationPhaseChangeRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link CertificationPhaseChangeRuleTest#testRule} and {@link CertificationPhaseChangeRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(CertificationPhaseChangeRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - certification as in rule context args by name {@link CertificationPhaseChangeRule#ARG_CERTIFICATION_NAME}
     * - certificationItem as in rule context args by name {@link CertificationPhaseChangeRule#ARG_CERTIFICATION_ITEM_NAME}
     * - previousPhase as in rule context args by name {@link CertificationPhaseChangeRule#ARG_PREVIOUS_PHASE_NAME}
     * - nextPhase as in rule context args by name {@link CertificationPhaseChangeRule#ARG_NEXT_PHASE_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalStringReturnTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        String testResult = UUID.randomUUID().toString();

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            CertificationPhaseChangeRule.CertificationPhaseChangeRuleArguments arguments = (CertificationPhaseChangeRule.CertificationPhaseChangeRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Certification is not match",
                    testRuleContext.getArguments()
                            .get(CertificationPhaseChangeRule.ARG_CERTIFICATION_NAME),
                    arguments.getCertification());
            assertEquals("CertificationItem is not match",
                    testRuleContext.getArguments()
                            .get(CertificationPhaseChangeRule.ARG_CERTIFICATION_ITEM_NAME),
                    arguments.getCertificationItem());
            assertEquals("PreviousPhase is not match",
                    testRuleContext.getArguments().get(CertificationPhaseChangeRule.ARG_PREVIOUS_PHASE_NAME),
                    arguments.getPreviousPhase());
            assertEquals("NextPhase is not match",
                    testRuleContext.getArguments().get(CertificationPhaseChangeRule.ARG_NEXT_PHASE_NAME),
                    arguments.getNextPhase());
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
        for (String noneNullArgument : CertificationPhaseChangeRule.NONE_NULL_ARGUMENTS_NAME) {

            JavaRuleContext testRuleContext = buildTestJavaRuleContext();
            testRuleContext.getArguments().remove(noneNullArgument);

            assertThrows(GeneralException.class, () -> testRule.execute(testRuleContext));
            verify(testRule).internalValidation(eq(testRuleContext));
            verify(testRule, never()).internalExecute(eq(sailPointContext), any());
        }
    }

    /**
     * Test execution with valid NULL arguments.
     * Input:
     * - valid rule context, but {@link CertificationPhaseChangeRule#ARG_PREVIOUS_PHASE_NAME} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - call internalExecute
     */
    @Test
    public void nullInstanceArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        String testResult = UUID.randomUUID().toString();

        testRuleContext.getArguments().remove(CertificationPhaseChangeRule.ARG_PREVIOUS_PHASE_NAME);
        when(testRule.internalExecute(eq(sailPointContext), any())).thenReturn(testResult);

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).internalExecute(eq(sailPointContext), any());
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
        assertEquals("Rule type is not match", Rule.Type.CertificationPhaseChange.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(CertificationPhaseChangeRule.ARG_CERTIFICATION_NAME, mock(Certification.class));
        ruleParameters.put(CertificationPhaseChangeRule.ARG_CERTIFICATION_ITEM_NAME, mock(CertificationItem.class));
        ruleParameters.put(CertificationPhaseChangeRule.ARG_PREVIOUS_PHASE_NAME, Certification.Phase.Active);
        ruleParameters.put(CertificationPhaseChangeRule.ARG_NEXT_PHASE_NAME, Certification.Phase.Challenge);
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
