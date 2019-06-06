package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.connector.WebServiceAfterOperationRule;
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
 * Test for {@link WebServiceAfterOperationRule} class
 */
public class WebServiceAfterOperationRuleTest {

    /**
     * Test instance of {@link WebServiceAfterOperationRule}
     */
    private WebServiceAfterOperationRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link WebServiceAfterOperationRuleTest#testRule} and {@link WebServiceAfterOperationRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(WebServiceAfterOperationRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - test map of execution
     * Expectation:
     * - application as in rule context args by name {@link WebServiceAfterOperationRule#ARG_APPLICATION}
     * - requestEndPoint as in rule context args by name {@link WebServiceAfterOperationRule#ARG_REQUEST_END_POINT}
     * - processedResponseObject as in rule context args by name {@link WebServiceAfterOperationRule#ARG_PROCESSED_RESPONSE_OBJECT}
     * - rawResponseObject as in rule context args by name {@link WebServiceAfterOperationRule#ARG_RAW_RESPONSE_OBJECT}
     * - restClient as in rule context args by name {@link WebServiceAfterOperationRule#ARG_REST_CLIENT}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        Map<String, Object> testResult = Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID());

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            WebServiceAfterOperationRule.WebServiceAfterOperationRuleArguments arguments = (WebServiceAfterOperationRule.WebServiceAfterOperationRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Application is not match",
                    testRuleContext.getArguments().get(WebServiceAfterOperationRule.ARG_APPLICATION),
                    arguments.getApplication());
            assertEquals("RequestEndPoint is not match",
                    testRuleContext.getArguments().get(WebServiceAfterOperationRule.ARG_REQUEST_END_POINT),
                    arguments.getRequestEndPoint());
            assertEquals("ProcessedResponseObject is not match",
                    testRuleContext.getArguments().get(WebServiceAfterOperationRule.ARG_PROCESSED_RESPONSE_OBJECT),
                    arguments.getProcessedResponseObject());
            assertEquals("RawResponseObject is not match",
                    testRuleContext.getArguments().get(WebServiceAfterOperationRule.ARG_RAW_RESPONSE_OBJECT),
                    arguments.getRawResponseObject());
            assertEquals("RestClient is not match",
                    testRuleContext.getArguments().get(WebServiceAfterOperationRule.ARG_REST_CLIENT),
                    arguments.getRestClient());
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
        for (String noneNullArgument : WebServiceAfterOperationRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.WebServiceAfterOperationRule.name(), testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(WebServiceAfterOperationRule.ARG_APPLICATION, new Application());
        ruleParameters.put(WebServiceAfterOperationRule.ARG_REQUEST_END_POINT, mock(EndPoint.class));
        ruleParameters.put(WebServiceAfterOperationRule.ARG_PROCESSED_RESPONSE_OBJECT,
                Collections.singletonList(Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID())));
        ruleParameters.put(WebServiceAfterOperationRule.ARG_RAW_RESPONSE_OBJECT, UUID.randomUUID().toString());
        ruleParameters.put(WebServiceAfterOperationRule.ARG_REST_CLIENT, mock(WebServicesClient.class));
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
