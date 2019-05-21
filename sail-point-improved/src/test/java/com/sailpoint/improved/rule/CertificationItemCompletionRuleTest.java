package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.certification.CertificationEntityCustomizationRule;
import com.sailpoint.improved.rule.certification.CertificationItemCompletionRule;
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
import sailpoint.tools.Message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
 * Test for {@link CertificationItemCompletionRule} class
 */
public class CertificationItemCompletionRuleTest {

    /**
     * Test instance of {@link CertificationItemCompletionRule}
     */
    private CertificationItemCompletionRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link CertificationItemCompletionRuleTest#testRule} and {@link CertificationItemCompletionRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(CertificationItemCompletionRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - random boolean test value
     * Expectation:
     * - certification as in rule context args by name {@link CertificationItemCompletionRule#ARG_CERTIFICATION_NAME}
     * - item as in rule context args by name {@link CertificationItemCompletionRule#ARG_ITEM_NAME}
     * - entity as in rule context args by name {@link CertificationItemCompletionRule#ARG_ENTITY_NAME}
     * - state as in rule context args by name {@link CertificationItemCompletionRule#ARG_STATE_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Boolean testResult = new Random().nextBoolean();

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            CertificationItemCompletionRule.CertificationItemCompletionRuleArguments arguments = (CertificationItemCompletionRule.CertificationItemCompletionRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Certification is not match",
                    testRuleContext.getArguments()
                            .get(CertificationItemCompletionRule.ARG_CERTIFICATION_NAME),
                    arguments.getCertification());
            assertEquals("Item is not match",
                    testRuleContext.getArguments()
                            .get(CertificationItemCompletionRule.ARG_ITEM_NAME),
                    arguments.getItem());
            assertEquals("Entity is not match",
                    testRuleContext.getArguments()
                            .get(CertificationItemCompletionRule.ARG_ENTITY_NAME),
                    arguments.getEntity());
            assertEquals("State is not match",
                    testRuleContext.getArguments()
                            .get(CertificationItemCompletionRule.ARG_STATE_NAME),
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
        for (String noneNullArgument : CertificationItemCompletionRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.CertificationItemCompletion.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(CertificationItemCompletionRule.ARG_CERTIFICATION_NAME, mock(Certification.class));
        ruleParameters
                .put(CertificationItemCompletionRule.ARG_ENTITY_NAME, mock(CertificationItem.class));
        ruleParameters
                .put(CertificationItemCompletionRule.ARG_ITEM_NAME, mock(CertificationItem.class));
        ruleParameters.put(CertificationEntityCustomizationRule.ARG_STATE_NAME,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
