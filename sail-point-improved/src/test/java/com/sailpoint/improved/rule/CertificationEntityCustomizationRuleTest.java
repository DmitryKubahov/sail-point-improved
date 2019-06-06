package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.certification.CertificationEntityCustomizationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.CertificationContext;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.AbstractCertifiableEntity;
import sailpoint.object.Certification;
import sailpoint.object.CertificationEntity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.sailpoint.improved.JUnit4Helper.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link CertificationEntityCustomizationRule} class
 */
public class CertificationEntityCustomizationRuleTest {

    /**
     * Test instance of {@link CertificationEntityCustomizationRule}
     */
    private CertificationEntityCustomizationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link CertificationEntityCustomizationRuleTest#testRule} and {@link CertificationEntityCustomizationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(CertificationEntityCustomizationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - null as it is a rule without outputs
     * Expectation:
     * - certification as in rule context args by name {@link CertificationEntityCustomizationRule#ARG_CERTIFICATION}
     * - certifiableEntity as in rule context args by name {@link CertificationEntityCustomizationRule#ARG_CERTIFIABLE_ENTITY}
     * - entity as in rule context args by name {@link CertificationEntityCustomizationRule#ARG_ENTITY}
     * - certContext as in rule context args by name {@link CertificationEntityCustomizationRule#ARG_CERT_CONTEXT}
     * - state as in rule context args by name {@link CertificationEntityCustomizationRule#ARG_STATE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            CertificationEntityCustomizationRule.CertificationEntityCustomizationRuleArguments arguments = (CertificationEntityCustomizationRule.CertificationEntityCustomizationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Certification is not match",
                    testRuleContext.getArguments()
                            .get(CertificationEntityCustomizationRule.ARG_CERTIFICATION),
                    arguments.getCertification());
            assertEquals("CertifiableEntity is not match",
                    testRuleContext.getArguments()
                            .get(CertificationEntityCustomizationRule.ARG_CERTIFIABLE_ENTITY),
                    arguments.getCertifiableEntity());
            assertEquals("Entity is not match",
                    testRuleContext.getArguments().get(CertificationEntityCustomizationRule.ARG_ENTITY),
                    arguments.getEntity());
            assertEquals("CertificationContext is not match",
                    testRuleContext.getArguments().get(CertificationEntityCustomizationRule.ARG_CERT_CONTEXT),
                    arguments.getCertContext());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(CertificationEntityCustomizationRule.ARG_STATE),
                    arguments.getState());
            return null;
        }).when(testRule).internalExecuteNoneOutput(eq(testRuleContext), any());

        assertNull(testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
        verify(testRule).internalExecuteNoneOutput(eq(testRuleContext), any());
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
        for (String noneNullArgument : CertificationEntityCustomizationRule.NONE_NULL_ARGUMENTS_NAME) {

            JavaRuleContext testRuleContext = buildTestJavaRuleContext();
            testRuleContext.getArguments().remove(noneNullArgument);

            assertThrows(GeneralException.class, () -> testRule.execute(testRuleContext));
            verify(testRule).internalValidation(eq(testRuleContext));
            verify(testRule, never()).internalExecute(eq(testRuleContext), any());
            verify(testRule, never()).internalExecuteNoneOutput(eq(testRuleContext), any());
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
        assertEquals("Rule type is not match", Rule.Type.CertificationEntityCustomization.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(CertificationEntityCustomizationRule.ARG_CERTIFICATION, mock(Certification.class));
        ruleParameters.put(CertificationEntityCustomizationRule.ARG_CERTIFIABLE_ENTITY, mock(
                AbstractCertifiableEntity.class));
        ruleParameters.put(CertificationEntityCustomizationRule.ARG_ENTITY, mock(CertificationEntity.class));
        ruleParameters
                .put(CertificationEntityCustomizationRule.ARG_CERT_CONTEXT, mock(CertificationContext.class));
        ruleParameters.put(CertificationEntityCustomizationRule.ARG_STATE,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
