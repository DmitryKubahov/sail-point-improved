package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.certification.CertificationEntityRefreshRule;
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
 * Test for {@link CertificationEntityRefreshRule} class
 */
public class CertificationEntityRefreshRuleTest {

    /**
     * Test instance of {@link CertificationEntityRefreshRule}
     */
    private CertificationEntityRefreshRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link CertificationEntityRefreshRuleTest#testRule} and {@link CertificationEntityRefreshRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(CertificationEntityRefreshRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - null, as it is a rule without outputs
     * Expectation:
     * - certification as in rule context args by name {@link CertificationEntityRefreshRule#ARG_CERTIFICATION}
     * - certificationEntity as in rule context args by name {@link CertificationEntityRefreshRule#ARG_CERTIFICATION_ENTITY}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            CertificationEntityRefreshRule.CertificationEntityRefreshRuleArguments arguments = (CertificationEntityRefreshRule.CertificationEntityRefreshRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Certification is not match",
                    testRuleContext.getArguments()
                            .get(CertificationEntityRefreshRule.ARG_CERTIFICATION),
                    arguments.getCertification());
            assertEquals("CertificationEntity is not match",
                    testRuleContext.getArguments()
                            .get(CertificationEntityRefreshRule.ARG_CERTIFICATION_ENTITY),
                    arguments.getCertificationEntity());
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
        for (String noneNullArgument : CertificationEntityRefreshRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.CertificationEntityRefresh.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(CertificationEntityRefreshRule.ARG_CERTIFICATION, mock(Certification.class));
        ruleParameters
                .put(CertificationEntityRefreshRule.ARG_CERTIFICATION_ENTITY, mock(CertificationEntity.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
