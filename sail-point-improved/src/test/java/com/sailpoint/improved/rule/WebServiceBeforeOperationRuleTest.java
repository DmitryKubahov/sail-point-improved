package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.connector.WebServiceBeforeOperationRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.connector.webservices.EndPoint;
import sailpoint.connector.webservices.WebServicesClient;
import sailpoint.object.Application;
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
 * Test for {@link WebServiceBeforeOperationRule} class
 */
public class WebServiceBeforeOperationRuleTest {

    /**
     * Test instance of {@link WebServiceBeforeOperationRule}
     */
    private WebServiceBeforeOperationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link WebServiceBeforeOperationRuleTest#testRule} and {@link WebServiceBeforeOperationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(WebServiceBeforeOperationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - application as in rule context args by name {@link WebServiceBeforeOperationRule#ARG_APPLICATION_NAME}
     * - requestEndPoint as in rule context args by name {@link WebServiceBeforeOperationRule#ARG_REQUEST_END_POINT_NAME}
     * - oldResponseMap as in rule context args by name {@link WebServiceBeforeOperationRule#ARG_OLD_RESPONSE_MAP_NAME}
     * - restClient as in rule context args by name {@link WebServiceBeforeOperationRule#ARG_REST_CLIENT_NAME}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        EndPoint testResult = mock(EndPoint.class);

        doAnswer(invocation -> {
            assertEquals("SailPoint context is not match", testRuleContext.getContext(), invocation.getArguments()[0]);
            WebServiceBeforeOperationRule.WebServiceBeforeOperationRuleArguments arguments = (WebServiceBeforeOperationRule.WebServiceBeforeOperationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(WebServiceBeforeOperationRule.ARG_APPLICATION_NAME),
                    arguments.getApplication());
            assertEquals("RequestEndPoint is not match",
                    testRuleContext.getArguments().get(WebServiceBeforeOperationRule.ARG_REQUEST_END_POINT_NAME),
                    arguments.getRequestEndPoint());
            assertEquals("OldResponseMap is not match",
                    testRuleContext.getArguments().get(WebServiceBeforeOperationRule.ARG_OLD_RESPONSE_MAP_NAME),
                    arguments.getOldResponseMap());
            assertEquals("RestClient is not match",
                    testRuleContext.getArguments().get(WebServiceBeforeOperationRule.ARG_REST_CLIENT_NAME),
                    arguments.getRestClient());
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
        for (String noneNullArgument : WebServiceBeforeOperationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.WebServiceBeforeOperationRule.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(WebServiceBeforeOperationRule.ARG_APPLICATION_NAME, new Application());
        ruleParameters.put(WebServiceBeforeOperationRule.ARG_REQUEST_END_POINT_NAME, mock(EndPoint.class));
        ruleParameters.put(WebServiceBeforeOperationRule.ARG_OLD_RESPONSE_MAP_NAME,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        ruleParameters.put(WebServiceBeforeOperationRule.ARG_REST_CLIENT_NAME, mock(WebServicesClient.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}