package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.certification.CertifierRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.GroupDefinition;
import sailpoint.object.GroupFactory;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;

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
 * Test for {@link CertifierRule} class
 */
public class CertifierRuleTest {

    /**
     * Test instance of {@link CertifierRule}
     */
    private CertifierRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link CertifierRuleTest#testRule} and {@link CertifierRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(CertifierRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution with {@link String} as result
     * Input:
     * - valid rule context
     * Output:
     * - test string value
     * Expectation:
     * - factory as in rule context args by name {@link CertifierRule#ARG_FACTORY}
     * - group as in rule context args by name {@link CertifierRule#ARG_GROUP}
     * - state as in rule context args by name {@link CertifierRule#ARG_STATE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalStringReturnTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        String testResult = UUID.randomUUID().toString();

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            CertifierRule.CertifierRuleArguments arguments = (CertifierRule.CertifierRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Factory is not match",
                    testRuleContext.getArguments().get(CertifierRule.ARG_FACTORY),
                    arguments.getFactory());
            assertEquals("Group is not match",
                    testRuleContext.getArguments().get(CertifierRule.ARG_GROUP),
                    arguments.getGroup());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(CertifierRule.ARG_STATE),
                    arguments.getState());
            return testResult;
        }).when(testRule).internalExecute(eq(testRuleContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
    }

    /**
     * Test of normal execution with {@link Identity} as result
     * Input:
     * - valid rule context
     * Output:
     * - test identity value
     * Expectation:
     * - factory as in rule context args by name {@link CertifierRule#ARG_FACTORY}
     * - group as in rule context args by name {@link CertifierRule#ARG_GROUP}
     * - state as in rule context args by name {@link CertifierRule#ARG_STATE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalIdentityReturnTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Identity testResult = mock(Identity.class);

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            CertifierRule.CertifierRuleArguments arguments = (CertifierRule.CertifierRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Factory is not match",
                    testRuleContext.getArguments().get(CertifierRule.ARG_FACTORY),
                    arguments.getFactory());
            assertEquals("Group is not match",
                    testRuleContext.getArguments().get(CertifierRule.ARG_GROUP),
                    arguments.getGroup());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(CertifierRule.ARG_STATE),
                    arguments.getState());
            return testResult;
        }).when(testRule).internalExecute(eq(testRuleContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
    }

    /**
     * Test of normal execution with {@link List<String>} as result
     * Input:
     * - valid rule context
     * Output:
     * - test list of string value
     * Expectation:
     * - factory as in rule context args by name {@link CertifierRule#ARG_FACTORY}
     * - group as in rule context args by name {@link CertifierRule#ARG_GROUP}
     * - state as in rule context args by name {@link CertifierRule#ARG_STATE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalStringListReturnTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        List<String> testResult = Collections.singletonList(UUID.randomUUID().toString());

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            CertifierRule.CertifierRuleArguments arguments = (CertifierRule.CertifierRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Factory is not match",
                    testRuleContext.getArguments().get(CertifierRule.ARG_FACTORY),
                    arguments.getFactory());
            assertEquals("Group is not match",
                    testRuleContext.getArguments().get(CertifierRule.ARG_GROUP),
                    arguments.getGroup());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(CertifierRule.ARG_STATE),
                    arguments.getState());
            return testResult;
        }).when(testRule).internalExecute(eq(testRuleContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
    }

    /**
     * Test of normal execution with {@link List<Identity>} as result
     * Input:
     * - valid rule context
     * Output:
     * - list of test identity value
     * Expectation:
     * - factory as in rule context args by name {@link CertifierRule#ARG_FACTORY}
     * - group as in rule context args by name {@link CertifierRule#ARG_GROUP}
     * - state as in rule context args by name {@link CertifierRule#ARG_STATE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalIdentityListReturnTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        List<Identity> testResult = Collections.singletonList(mock(Identity.class));

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            CertifierRule.CertifierRuleArguments arguments = (CertifierRule.CertifierRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Factory is not match",
                    testRuleContext.getArguments().get(CertifierRule.ARG_FACTORY),
                    arguments.getFactory());
            assertEquals("Group is not match",
                    testRuleContext.getArguments().get(CertifierRule.ARG_GROUP),
                    arguments.getGroup());
            assertEquals("State is not match",
                    testRuleContext.getArguments().get(CertifierRule.ARG_STATE),
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
        for (String noneNullArgument : CertifierRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.Certifier.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(CertifierRule.ARG_FACTORY, mock(GroupFactory.class));
        ruleParameters.put(CertifierRule.ARG_GROUP, mock(GroupDefinition.class));
        ruleParameters.put(CertifierRule.ARG_STATE,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
