package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.mapping.IdentityAttributeRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.AttributeDefinition;
import sailpoint.object.AttributeSource;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;
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
import static org.mockito.Mockito.when;

/**
 * Test for {@link IdentityAttributeRule} class
 */
public class IdentityAttributeRuleTest {

    /**
     * Test instance of {@link IdentityAttributeRule}
     */
    private IdentityAttributeRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link IdentityAttributeRuleTest#testRule} and {@link IdentityAttributeRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(IdentityAttributeRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test object value
     * Expectation:
     * - environment as in rule context args by name {@link IdentityAttributeRule#ARG_ENVIRONMENT}
     * - identity as in rule context args by name {@link IdentityAttributeRule#ARG_IDENTITY}
     * - attributeDefinition as in rule context args by name {@link IdentityAttributeRule#ARG_ATTRIBUTE_DEFINITION}
     * - link as in rule context args by name {@link IdentityAttributeRule#ARG_LINK}
     * - attributeSource as in rule context args by name {@link IdentityAttributeRule#ARG_ATTRIBUTE_SOURCE}
     * - oldValue as in rule context args by name {@link IdentityAttributeRule#ARG_OLD_VALUE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Object testResult = UUID.randomUUID();

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            IdentityAttributeRule.IdentityAttributeRuleArguments arguments = (IdentityAttributeRule.IdentityAttributeRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Environment is not match",
                    testRuleContext.getArguments().get(IdentityAttributeRule.ARG_ENVIRONMENT),
                    arguments.getEnvironment());
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(IdentityAttributeRule.ARG_IDENTITY),
                    arguments.getIdentity());
            assertEquals("AttributeDefinition is not match",
                    testRuleContext.getArguments().get(IdentityAttributeRule.ARG_ATTRIBUTE_DEFINITION),
                    arguments.getAttributeDefinition());
            assertEquals("Link is not match",
                    testRuleContext.getArguments().get(IdentityAttributeRule.ARG_LINK),
                    arguments.getLink());
            assertEquals("AttributeSource is not match",
                    testRuleContext.getArguments().get(IdentityAttributeRule.ARG_ATTRIBUTE_SOURCE),
                    arguments.getAttributeSource());
            assertEquals("OldValue is not match",
                    testRuleContext.getArguments().get(IdentityAttributeRule.ARG_OLD_VALUE),
                    arguments.getOldValue());
            return testResult;
        }).when(testRule).internalExecute(eq(testRuleContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
    }

    /**
     * Test execution with valid NULL arguments.
     * Input:
     * - valid rule context, but {@link IdentityAttributeRule#ARG_LINK} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - call internalExecute
     */
    @Test
    public void nullLinkArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Object testResult = UUID.randomUUID();

        testRuleContext.getArguments().remove(IdentityAttributeRule.ARG_LINK);
        when(testRule.internalExecute(eq(testRuleContext), any())).thenReturn(testResult);

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
    }

    /**
     * Test execution with valid NULL arguments.
     * Input:
     * - valid rule context, but {@link IdentityAttributeRule#ARG_OLD_VALUE} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - call internalExecute
     */
    @Test
    public void nullOldValueArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Object testResult = UUID.randomUUID();

        testRuleContext.getArguments().remove(IdentityAttributeRule.ARG_OLD_VALUE);
        when(testRule.internalExecute(eq(testRuleContext), any())).thenReturn(testResult);

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
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
        for (String noneNullArgument : IdentityAttributeRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.IdentityAttribute.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(IdentityAttributeRule.ARG_ENVIRONMENT,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        ruleParameters.put(IdentityAttributeRule.ARG_IDENTITY, mock(Identity.class));
        ruleParameters.put(IdentityAttributeRule.ARG_ATTRIBUTE_DEFINITION, mock(AttributeDefinition.class));
        ruleParameters.put(IdentityAttributeRule.ARG_LINK, mock(Link.class));
        ruleParameters.put(IdentityAttributeRule.ARG_ATTRIBUTE_SOURCE, mock(AttributeSource.class));
        ruleParameters.put(IdentityAttributeRule.ARG_OLD_VALUE, UUID.randomUUID());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
