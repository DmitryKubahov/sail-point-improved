package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.aggregation.AccountSelectorRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Application;
import sailpoint.object.Bundle;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningProject;
import sailpoint.object.Rule;
import sailpoint.object.Source;
import sailpoint.tools.GeneralException;

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
import static org.mockito.Mockito.when;

/**
 * Test for {@link AccountSelectorRule} class
 */
public class AccountSelectorRuleTest {

    /**
     * Test instance of {@link AccountSelectorRule}
     */
    private AccountSelectorRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link AccountSelectorRuleTest#testRule} and {@link AccountSelectorRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(AccountSelectorRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution with link return type
     * Input:
     * - valid rule context
     * Output:
     * - test link of execution
     * Expectation:
     * - source as in rule context args by name {@link AccountSelectorRule#ARG_SOURCE}
     * - role as in rule context args by name {@link AccountSelectorRule#ARG_ROLE}
     * - identity as in rule context args by name {@link AccountSelectorRule#ARG_IDENTITY}
     * - application as in rule context args by name {@link AccountSelectorRule#ARG_APPLICATION}
     * - links as in rule context args by name {@link AccountSelectorRule#ARG_LINKS}
     * - isSecondary as in rule context args by name {@link AccountSelectorRule#ARG_IS_SECONDARY}
     * - project as in rule context args by name {@link AccountSelectorRule#ARG_PROJECT}
     * - accountRequest as in rule context args by name {@link AccountSelectorRule#ARG_ACCOUNT_REQUEST}
     * - allowCreate as in rule context args by name {@link AccountSelectorRule#ARG_ALLOW_CREATE}
     * - context as in sailpoint context in rule context
     */
    @Test

    public void normalLinkReturnTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Link testResult = mock(Link.class);

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            AccountSelectorRule.AccountSelectorRuleArguments arguments = (AccountSelectorRule.AccountSelectorRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Source is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_SOURCE),
                    arguments.getSource().name());
            assertEquals("Role is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_ROLE),
                    arguments.getRole());
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_IDENTITY),
                    arguments.getIdentity());
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("Links is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_LINKS),
                    arguments.getLinks());
            assertEquals("IsSecondary is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_IS_SECONDARY),
                    arguments.getIsSecondary());
            assertEquals("Project is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_PROJECT),
                    arguments.getProject());
            assertEquals("AccountRequest is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_ACCOUNT_REQUEST),
                    arguments.getAccountRequest());
            assertEquals("AllowCreate is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_ALLOW_CREATE),
                    arguments.getAllowCreate());
            return testResult;
        }).when(testRule).internalExecute(eq(testRuleContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
    }

    /**
     * Test of normal execution with string return type
     * Input:
     * - valid rule context
     * Output:
     * - test string of execution
     * Expectation:
     * - source as in rule context args by name {@link AccountSelectorRule#ARG_SOURCE}
     * - role as in rule context args by name {@link AccountSelectorRule#ARG_ROLE}
     * - identity as in rule context args by name {@link AccountSelectorRule#ARG_IDENTITY}
     * - application as in rule context args by name {@link AccountSelectorRule#ARG_APPLICATION}
     * - links as in rule context args by name {@link AccountSelectorRule#ARG_LINKS}
     * - isSecondary as in rule context args by name {@link AccountSelectorRule#ARG_IS_SECONDARY}
     * - project as in rule context args by name {@link AccountSelectorRule#ARG_PROJECT}
     * - accountRequest as in rule context args by name {@link AccountSelectorRule#ARG_ACCOUNT_REQUEST}
     * - allowCreate as in rule context args by name {@link AccountSelectorRule#ARG_ALLOW_CREATE}
     * - context as in sailpoint context in rule context
     */
    @Test

    public void normalStringReturnTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        String testResult = UUID.randomUUID().toString();

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            AccountSelectorRule.AccountSelectorRuleArguments arguments = (AccountSelectorRule.AccountSelectorRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Source is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_SOURCE),
                    arguments.getSource().name());
            assertEquals("Role is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_ROLE),
                    arguments.getRole());
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_IDENTITY),
                    arguments.getIdentity());
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("Links is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_LINKS),
                    arguments.getLinks());
            assertEquals("IsSecondary is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_IS_SECONDARY),
                    arguments.getIsSecondary());
            assertEquals("Project is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_PROJECT),
                    arguments.getProject());
            assertEquals("AccountRequest is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_ACCOUNT_REQUEST),
                    arguments.getAccountRequest());
            assertEquals("AllowCreate is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_ALLOW_CREATE),
                    arguments.getAllowCreate());
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
        for (String noneNullArgument : AccountSelectorRule.NONE_NULL_ARGUMENTS_NAME) {

            JavaRuleContext testRuleContext = buildTestJavaRuleContext();
            testRuleContext.getArguments().remove(noneNullArgument);

            assertThrows(GeneralException.class, () -> testRule.execute(testRuleContext));
            verify(testRule).internalValidation(eq(testRuleContext));
            verify(testRule, never()).internalExecute(eq(testRuleContext), any());
        }
    }

    /**
     * Test execution with valid NULL arguments.
     * Input:
     * - valid rule context, but {@link AccountSelectorRule#ARG_LINKS} attribute null
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

        testRuleContext.getArguments().remove(AccountSelectorRule.ARG_LINKS);
        when(testRule.internalExecute(eq(testRuleContext), any())).thenReturn(testResult);

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
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
        assertEquals("Rule type is not match", Rule.Type.AccountSelector.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(AccountSelectorRule.ARG_SOURCE, Source.Aggregation.name());
        ruleParameters.put(AccountSelectorRule.ARG_ROLE, mock(Bundle.class));
        ruleParameters.put(AccountSelectorRule.ARG_IDENTITY, mock(Identity.class));
        ruleParameters.put(AccountSelectorRule.ARG_APPLICATION, mock(Application.class));
        ruleParameters.put(AccountSelectorRule.ARG_LINKS, Collections.singletonList(mock(Link.class)));
        ruleParameters.put(AccountSelectorRule.ARG_IS_SECONDARY, new Random().nextBoolean());
        ruleParameters.put(AccountSelectorRule.ARG_PROJECT, mock(ProvisioningProject.class));
        ruleParameters.put(AccountSelectorRule.ARG_ACCOUNT_REQUEST, mock(ProvisioningPlan.AccountRequest.class));
        ruleParameters.put(AccountSelectorRule.ARG_ALLOW_CREATE, new Random().nextBoolean());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
