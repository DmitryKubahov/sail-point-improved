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
     * - source as in rule context args by name {@link AccountSelectorRule#ARG_SOURCE_NAME}
     * - role as in rule context args by name {@link AccountSelectorRule#ARG_ROLE_NAME}
     * - identity as in rule context args by name {@link AccountSelectorRule#ARG_IDENTITY_NAME}
     * - application as in rule context args by name {@link AccountSelectorRule#ARG_APPLICATION_NAME}
     * - links as in rule context args by name {@link AccountSelectorRule#ARG_LINKS_NAME}
     * - isSecondary as in rule context args by name {@link AccountSelectorRule#ARG_IS_SECONDARY_NAME}
     * - project as in rule context args by name {@link AccountSelectorRule#ARG_PROJECT_NAME}
     * - accountRequest as in rule context args by name {@link AccountSelectorRule#ARG_ACCOUNT_REQUEST_NAME}
     * - allowCreate as in rule context args by name {@link AccountSelectorRule#ARG_ALLOW_CREATE_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test

    public void normalLinkReturnTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Link testResult = mock(Link.class);

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            AccountSelectorRule.AccountSelectorRuleArguments arguments = (AccountSelectorRule.AccountSelectorRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Source is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_SOURCE_NAME),
                    arguments.getSource().name());
            assertEquals("Role is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_ROLE_NAME),
                    arguments.getRole());
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_IDENTITY_NAME),
                    arguments.getIdentity());
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_APPLICATION_NAME),
                    arguments.getApplication());
            assertEquals("Links is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_LINKS_NAME),
                    arguments.getLinks());
            assertEquals("IsSecondary is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_IS_SECONDARY_NAME),
                    arguments.getIsSecondary());
            assertEquals("Project is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_PROJECT_NAME),
                    arguments.getProject());
            assertEquals("AccountRequest is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_ACCOUNT_REQUEST_NAME),
                    arguments.getAccountRequest());
            assertEquals("AllowCreate is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_ALLOW_CREATE_NAME),
                    arguments.getAllowCreate());
            return testResult;
        }).when(testRule).internalExecute(eq(sailPointContext), any());

        assertEquals(testResult, testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(sailPointContext), any());
    }

    /**
     * Test of normal execution with string return type
     * Input:
     * - valid rule context
     * Output:
     * - test string of execution
     * Expectation:
     * - source as in rule context args by name {@link AccountSelectorRule#ARG_SOURCE_NAME}
     * - role as in rule context args by name {@link AccountSelectorRule#ARG_ROLE_NAME}
     * - identity as in rule context args by name {@link AccountSelectorRule#ARG_IDENTITY_NAME}
     * - application as in rule context args by name {@link AccountSelectorRule#ARG_APPLICATION_NAME}
     * - links as in rule context args by name {@link AccountSelectorRule#ARG_LINKS_NAME}
     * - isSecondary as in rule context args by name {@link AccountSelectorRule#ARG_IS_SECONDARY_NAME}
     * - project as in rule context args by name {@link AccountSelectorRule#ARG_PROJECT_NAME}
     * - accountRequest as in rule context args by name {@link AccountSelectorRule#ARG_ACCOUNT_REQUEST_NAME}
     * - allowCreate as in rule context args by name {@link AccountSelectorRule#ARG_ALLOW_CREATE_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test

    public void normalStringReturnTypeTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        String testResult = UUID.randomUUID().toString();

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            AccountSelectorRule.AccountSelectorRuleArguments arguments = (AccountSelectorRule.AccountSelectorRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Source is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_SOURCE_NAME),
                    arguments.getSource().name());
            assertEquals("Role is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_ROLE_NAME),
                    arguments.getRole());
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_IDENTITY_NAME),
                    arguments.getIdentity());
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_APPLICATION_NAME),
                    arguments.getApplication());
            assertEquals("Links is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_LINKS_NAME),
                    arguments.getLinks());
            assertEquals("IsSecondary is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_IS_SECONDARY_NAME),
                    arguments.getIsSecondary());
            assertEquals("Project is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_PROJECT_NAME),
                    arguments.getProject());
            assertEquals("AccountRequest is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_ACCOUNT_REQUEST_NAME),
                    arguments.getAccountRequest());
            assertEquals("AllowCreate is not match",
                    testRuleContext.getArguments().get(AccountSelectorRule.ARG_ALLOW_CREATE_NAME),
                    arguments.getAllowCreate());
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
        for (String noneNullArgument : AccountSelectorRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.AccountSelector.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(AccountSelectorRule.ARG_SOURCE_NAME, Source.Aggregation.name());
        ruleParameters.put(AccountSelectorRule.ARG_ROLE_NAME, mock(Bundle.class));
        ruleParameters.put(AccountSelectorRule.ARG_IDENTITY_NAME, mock(Identity.class));
        ruleParameters.put(AccountSelectorRule.ARG_APPLICATION_NAME, mock(Application.class));
        ruleParameters.put(AccountSelectorRule.ARG_LINKS_NAME, Collections.singletonList(mock(Link.class)));
        ruleParameters.put(AccountSelectorRule.ARG_IS_SECONDARY_NAME, new Random().nextBoolean());
        ruleParameters.put(AccountSelectorRule.ARG_PROJECT_NAME, mock(ProvisioningProject.class));
        ruleParameters.put(AccountSelectorRule.ARG_ACCOUNT_REQUEST_NAME, mock(ProvisioningPlan.AccountRequest.class));
        ruleParameters.put(AccountSelectorRule.ARG_ALLOW_CREATE_NAME, new Random().nextBoolean());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
