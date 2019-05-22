package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.certification.CertificationEntityCompletionRule;
import com.sailpoint.improved.rule.certification.CertificationEntityCustomizationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Certification;
import sailpoint.object.CertificationEntity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Message;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
 * Test for {@link CertificationEntityCompletionRule} class
 */
public class CertificationEntityCompletionRuleTest {

    /**
     * Test instance of {@link CertificationEntityCompletionRule}
     */
    private CertificationEntityCompletionRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link CertificationEntityCompletionRuleTest#testRule} and {@link CertificationEntityCompletionRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(CertificationEntityCompletionRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution with return list type of {@link Message}
     * Input:
     * - valid rule context
     * Output:
     * - test list of test messages value
     * Expectation:
     * - certification as in rule context args by name {@link CertificationEntityCompletionRule#ARG_CERTIFICATION}
     * - certificationEntity as in rule context args by name {@link CertificationEntityCompletionRule#ARG_CERTIFICATION_ENTITY}
     * - state as in rule context args by name {@link CertificationEntityCompletionRule#ARG_STATE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalMessagesReturnTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        List<Message> testResult = Collections.singletonList(Message.info(UUID.randomUUID().toString()));

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            CertificationEntityCompletionRule.CertificationEntityCompletionRuleArguments arguments = (CertificationEntityCompletionRule.CertificationEntityCompletionRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Certification is not match",
                    testRuleContext.getArguments()
                            .get(CertificationEntityCompletionRule.ARG_CERTIFICATION),
                    arguments.getCertification());
            assertEquals("CertificationEntity is not match",
                    testRuleContext.getArguments()
                            .get(CertificationEntityCompletionRule.ARG_CERTIFICATION_ENTITY),
                    arguments.getCertificationEntity());
            assertEquals("State is not match",
                    testRuleContext.getArguments()
                            .get(CertificationEntityCompletionRule.ARG_STATE),
                    arguments.getState());
            return testResult;
        }).when(testRule).internalExecute(eq(sailPointContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test of normal execution with return list type of {@link String}
     * Input:
     * - valid rule context
     * Output:
     * - test list of test strings value
     * Expectation:
     * - certification as in rule context args by name {@link CertificationEntityCompletionRule#ARG_CERTIFICATION}
     * - certificationEntity as in rule context args by name {@link CertificationEntityCompletionRule#ARG_CERTIFICATION_ENTITY}
     * - state as in rule context args by name {@link CertificationEntityCompletionRule#ARG_STATE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalStringsReturnTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        List<String> testResult = Collections.singletonList(UUID.randomUUID().toString());

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            CertificationEntityCompletionRule.CertificationEntityCompletionRuleArguments arguments = (CertificationEntityCompletionRule.CertificationEntityCompletionRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Certification is not match",
                    testRuleContext.getArguments()
                            .get(CertificationEntityCompletionRule.ARG_CERTIFICATION),
                    arguments.getCertification());
            assertEquals("CertificationEntity is not match",
                    testRuleContext.getArguments()
                            .get(CertificationEntityCompletionRule.ARG_CERTIFICATION_ENTITY),
                    arguments.getCertificationEntity());
            assertEquals("State is not match",
                    testRuleContext.getArguments()
                            .get(CertificationEntityCompletionRule.ARG_STATE),
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
    public void noneNullArgumentIsNullTest() throws GeneralException {
        for (String noneNullArgument : CertificationEntityCompletionRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.CertificationEntityCompletion.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(CertificationEntityCompletionRule.ARG_CERTIFICATION, mock(Certification.class));
        ruleParameters
                .put(CertificationEntityCompletionRule.ARG_CERTIFICATION_ENTITY, mock(CertificationEntity.class));
        ruleParameters.put(CertificationEntityCustomizationRule.ARG_STATE,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
