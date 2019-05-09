package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.certification.CertificationPreDelegationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.CertificationContext;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Certification;
import sailpoint.object.CertificationEntity;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.sailpoint.improved.JUnit4Helper.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link CertificationPreDelegationRule} class
 */
public class CertificationPreDelegationRuleTest {

    /**
     * Test instance of {@link CertificationPreDelegationRule}
     */
    private CertificationPreDelegationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link CertificationPreDelegationRuleTest#testRule} and {@link CertificationPreDelegationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(CertificationPreDelegationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - certification as in rule context args by name {@link CertificationPreDelegationRule#ARG_CERTIFICATION_NAME}
     * - entity as in rule context args by name {@link CertificationPreDelegationRule#ARG_ENTITY_NAME}
     * - certContext as in rule context args by name {@link CertificationPreDelegationRule#ARG_CERT_CONTEXT_NAME}
     * - state as in rule context args by name {@link CertificationPreDelegationRule#ARG_STATE_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = buildValidRuleResult().toMap();

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            CertificationPreDelegationRule.CertificationPreDelegationRuleArguments arguments = (CertificationPreDelegationRule.CertificationPreDelegationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Certification is not match",
                    testRuleContext.getArguments().get(CertificationPreDelegationRule.ARG_CERTIFICATION_NAME),
                    arguments.getCertification());
            assertEquals("Entity is not match",
                    testRuleContext.getArguments().get(CertificationPreDelegationRule.ARG_ENTITY_NAME),
                    arguments.getEntity());
            assertEquals("CertificationContext is not match",
                    testRuleContext.getArguments().get(CertificationPreDelegationRule.ARG_CERT_CONTEXT_NAME),
                    arguments.getCertContext());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(CertificationPreDelegationRule.ARG_STATE_NAME),
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
        for (String noneNullArgument : CertificationPreDelegationRule.NONE_NULL_ARGUMENTS_NAME) {

            JavaRuleContext testRuleContext = buildTestJavaRuleContext();
            testRuleContext.getArguments().remove(noneNullArgument);

            assertThrows(GeneralException.class, () -> testRule.execute(testRuleContext));
            verify(testRule).internalValidation(eq(testRuleContext));
            verify(testRule, never()).internalExecute(eq(sailPointContext), any());
        }
    }

    /**
     * Test rule result builder
     * Input:
     * - set all values to builder and call toMap
     * Output:
     * - map with all key-value pairs
     * Expectation:
     * - map contains all keys and values are the same as tests data
     */
    @Test
    public void ruleResultBuilderTest() {
        CertificationPreDelegationRule.RuleResult ruleResult = buildValidRuleResult();
        Map<String, Object> resultMap = ruleResult.toMap();
        assertNotNull("Result map cannot be null", resultMap);
        for (CertificationPreDelegationRule.OutputArguments outputArgument :
                CertificationPreDelegationRule.OutputArguments.values()) {
            assertNotNull("Attribute [" + outputArgument.getKeyValue() + "] cannot be null",
                    resultMap.get(outputArgument.getKeyValue()));
        }
        checkResultMapArguments(CertificationPreDelegationRule.OutputArguments.CERTIFICATION_NAME,
                ruleResult.getCertificationName(), resultMap);
        checkResultMapArguments(CertificationPreDelegationRule.OutputArguments.COMMENTS, ruleResult.getComments(),
                resultMap);
        checkResultMapArguments(CertificationPreDelegationRule.OutputArguments.DESCRIPTION, ruleResult.getDescription(),
                resultMap);
        checkResultMapArguments(CertificationPreDelegationRule.OutputArguments.REASSIGN, ruleResult.getReassign(),
                resultMap);
        checkResultMapArguments(CertificationPreDelegationRule.OutputArguments.RECIPIENT, ruleResult.getRecipient(),
                resultMap);
        checkResultMapArguments(CertificationPreDelegationRule.OutputArguments.RECIPIENT_NAME,
                ruleResult.getRecipientName(), resultMap);
    }

    /**
     * Test rule result builder
     * Input:
     * - set all values to builder to null and call toMap
     * Output:
     * - empty map
     * Expectation:
     * - map without elements
     */
    @Test
    public void ruleResultBuilderAllArgumentsNullTest() {
        CertificationPreDelegationRule.RuleResult ruleResult = CertificationPreDelegationRule.RuleResult.builder()
                .build();
        Map<String, Object> resultMap = ruleResult.toMap();
        assertNotNull("Result map cannot be null", resultMap);
        assertTrue("Result map must be empty", resultMap.isEmpty());
    }


    /**
     * Check expected value with value from result map after building
     *
     * @param argument  - argument to check
     * @param expected  - expected value
     * @param resultMap - current result map
     */
    private void checkResultMapArguments(CertificationPreDelegationRule.OutputArguments argument, Object expected,
                                         Map<String, Object> resultMap) {
        assertEquals(argument.getKeyValue(), expected, resultMap.get(argument.getKeyValue()));
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
        assertEquals("Rule type is not match", Rule.Type.CertificationPreDelegation.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(CertificationPreDelegationRule.ARG_CERTIFICATION_NAME, mock(Certification.class));
        ruleParameters.put(CertificationPreDelegationRule.ARG_ENTITY_NAME, mock(CertificationEntity.class));
        ruleParameters.put(CertificationPreDelegationRule.ARG_CERT_CONTEXT_NAME, mock(CertificationContext.class));
        ruleParameters.put(CertificationPreDelegationRule.ARG_STATE_NAME,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }

    /**
     * Build rule result with all attributes
     *
     * @return valid rule result
     */
    private CertificationPreDelegationRule.RuleResult buildValidRuleResult() {
        return CertificationPreDelegationRule.RuleResult.builder()
                .certificationName(UUID.randomUUID().toString())
                .comments(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .reassign(new Random().nextBoolean())
                .recipient(mock(Identity.class))
                .recipientName(UUID.randomUUID().toString())
                .build();
    }
}
