package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.certification.CertificationExclusionRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.CertificationContext;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.AbstractCertifiableEntity;
import sailpoint.object.Certifiable;
import sailpoint.object.Certification;
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
 * Test for {@link CertificationExclusionRule} class
 */
public class CertificationExclusionRuleTest {

    /**
     * Test instance of {@link CertificationExclusionRule}
     */
    private CertificationExclusionRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link CertificationExclusionRuleTest#testRule} and {@link CertificationExclusionRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(CertificationExclusionRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - entity as in rule context args by name {@link CertificationExclusionRule#ARG_ENTITY_NAME}
     * - certification as in rule context args by name {@link CertificationExclusionRule#ARG_CERTIFICATION_NAME}
     * - certContext as in rule context args by name {@link CertificationExclusionRule#ARG_CERT_CONTEXT_NAME}
     * - items as in rule context args by name {@link CertificationExclusionRule#ARG_ITEMS_NAME}
     * - itemsToExclude as in rule context args by name {@link CertificationExclusionRule#ARG_ITEMS_TO_EXCLUDE_NAME}
     * - state as in rule context args by name {@link CertificationExclusionRule#ARG_STATE_NAME}
     * - identity as in rule context args by name {@link CertificationExclusionRule#ARG_IDENTITY_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        String testResult = UUID.randomUUID().toString();

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            CertificationExclusionRule.CertificationExclusionRuleArguments arguments = (CertificationExclusionRule.CertificationExclusionRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Entity is not match",
                    testRuleContext.getArguments().get(CertificationExclusionRule.ARG_ENTITY_NAME),
                    arguments.getEntity());
            assertEquals("Certification is not match",
                    testRuleContext.getArguments().get(CertificationExclusionRule.ARG_CERTIFICATION_NAME),
                    arguments.getCertification());
            assertEquals("CertificationContext is not match",
                    testRuleContext.getArguments().get(CertificationExclusionRule.ARG_CERT_CONTEXT_NAME),
                    arguments.getCertContext());
            assertEquals("Items is not match",
                    testRuleContext.getArguments().get(CertificationExclusionRule.ARG_ITEMS_NAME),
                    arguments.getItems());
            assertEquals("ItemsToExclude is not match",
                    testRuleContext.getArguments().get(CertificationExclusionRule.ARG_ITEMS_TO_EXCLUDE_NAME),
                    arguments.getItemsToExclude());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(CertificationExclusionRule.ARG_STATE_NAME),
                    arguments.getState());
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(CertificationExclusionRule.ARG_IDENTITY_NAME),
                    arguments.getIdentity());
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
        for (String noneNullArgument : CertificationExclusionRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.CertificationExclusion.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(CertificationExclusionRule.ARG_ENTITY_NAME, mock(AbstractCertifiableEntity.class));
        ruleParameters.put(CertificationExclusionRule.ARG_CERTIFICATION_NAME, mock(Certification.class));
        ruleParameters.put(CertificationExclusionRule.ARG_CERT_CONTEXT_NAME, mock(CertificationContext.class));
        ruleParameters
                .put(CertificationExclusionRule.ARG_ITEMS_NAME, Collections.singletonList(mock(Certifiable.class)));
        ruleParameters.put(CertificationExclusionRule.ARG_ITEMS_TO_EXCLUDE_NAME,
                Collections.singletonList(mock(Certifiable.class)));
        ruleParameters.put(CertificationExclusionRule.ARG_STATE_NAME,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        ruleParameters.put(CertificationExclusionRule.ARG_IDENTITY_NAME, mock(AbstractCertifiableEntity.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
