package com.sailpoint.improved.rule;

import com.sailpoint.improved.rule.mapping.ListenerRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ObjectAttribute;
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
 * Test for {@link ListenerRule} class
 */
public class ListenerRuleTest {

    /**
     * Test instance of {@link ListenerRule}
     */
    private ListenerRule testRule;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init {@link ListenerRuleTest#testRule} and {@link ListenerRuleTest#sailPointContext} for test
     */
    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.testRule = mock(ListenerRule.class,
                Mockito.withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Test of normal execution
     * Input:
     * - valid rule context
     * Output:
     * - null, as rule does not return anything
     * Expectation:
     * - environment as in rule context args by name {@link ListenerRule#ARG_ENVIRONMENT}
     * - identity as in rule context args by name {@link ListenerRule#ARG_IDENTITY}
     * - object as in rule context args by name {@link ListenerRule#ARG_OBJECT
     * - attributeDefinition as in rule context args by name {@link ListenerRule#ARG_ATTRIBUTE_DEFINITION}
     * - attributeName as in rule context args by name {@link ListenerRule#ARG_ATTRIBUTE_NAME}
     * - oldValue as in rule context args by name {@link ListenerRule#ARG_OLD_VALUE}
     * - newValue as in rule context args by name {@link ListenerRule#ARG_NEW_VALUE}
     * - context as in sailpoint context in rule context
     */
    @Test
    public void normalExecutionTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();

        doAnswer(invocation -> {
            assertEquals("JavaRuleContext is not match", testRuleContext, invocation.getArguments()[0]);
            ListenerRule.ListenerRuleArguments arguments = (ListenerRule.ListenerRuleArguments) invocation
                    .getArguments()[1];
            assertEquals("Environment is not match",
                    testRuleContext.getArguments().get(ListenerRule.ARG_ENVIRONMENT),
                    arguments.getEnvironment());
            assertEquals("Identity is not match",
                    testRuleContext.getArguments().get(ListenerRule.ARG_IDENTITY),
                    arguments.getIdentity());
            assertEquals("Object is not match",
                    testRuleContext.getArguments().get(ListenerRule.ARG_OBJECT),
                    arguments.getObject());
            assertEquals("AttributeDefinition is not match",
                    testRuleContext.getArguments().get(ListenerRule.ARG_ATTRIBUTE_DEFINITION),
                    arguments.getAttributeDefinition());
            assertEquals("AttributeName is not match",
                    testRuleContext.getArguments().get(ListenerRule.ARG_ATTRIBUTE_NAME),
                    arguments.getAttributeName());
            assertEquals("OldValue is not match",
                    testRuleContext.getArguments().get(ListenerRule.ARG_OLD_VALUE),
                    arguments.getOldValue());
            assertEquals("NewValue is not match",
                    testRuleContext.getArguments().get(ListenerRule.ARG_NEW_VALUE),
                    arguments.getNewValue());
            return null;
        }).when(testRule).internalExecuteNoneOutput(eq(testRuleContext), any());

        assertNull(testRule.execute(testRuleContext));
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).execute(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
        verify(testRule).internalExecuteNoneOutput(eq(testRuleContext), any());
    }

    /**
     * Test execution with valid NULL arguments.
     * Input:
     * - valid rule context, but {@link ListenerRule#ARG_OLD_VALUE} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - call internalExecute
     */
    @Test
    public void nullOldValueArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(ListenerRule.ARG_OLD_VALUE);

        testRule.execute(testRuleContext);
        verify(testRule).internalValidation(eq(testRuleContext));
        verify(testRule).internalExecute(eq(testRuleContext), any());
        verify(testRule).internalExecuteNoneOutput(eq(testRuleContext), any());
    }

    /**
     * Test execution with valid NULL arguments.
     * Input:
     * - valid rule context, but {@link ListenerRule#ARG_NEW_VALUE} attribute null
     * Output:
     * - General exception
     * Expectation:
     * - call internalValidation
     * - call internalExecute
     */
    @Test
    public void nullNewValueArgumentValueTest() throws GeneralException {
        JavaRuleContext testRuleContext = buildTestJavaRuleContext();
        testRuleContext.getArguments().remove(ListenerRule.ARG_NEW_VALUE);

        testRule.execute(testRuleContext);
        verify(testRule).internalValidation(eq(testRuleContext));
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
        for (String noneNullArgument : ListenerRule.NONE_NULL_ARGUMENTS_NAME) {

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
        assertEquals("Rule type is not match", Rule.Type.Listener.name(),
                testRule.getRuleType());
    }

    /**
     * Create valid java rule context for current rule
     *
     * @return valid rule context
     */
    private JavaRuleContext buildTestJavaRuleContext() {
        Map<String, Object> ruleParameters = new HashMap<>();
        ruleParameters.put(ListenerRule.ARG_ENVIRONMENT,
                Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID()));
        ruleParameters.put(ListenerRule.ARG_IDENTITY, mock(Identity.class));
        ruleParameters.put(ListenerRule.ARG_OBJECT, mock(Identity.class));
        ruleParameters.put(ListenerRule.ARG_ATTRIBUTE_DEFINITION, mock(ObjectAttribute.class));
        ruleParameters.put(ListenerRule.ARG_ATTRIBUTE_NAME, UUID.randomUUID().toString());
        ruleParameters.put(ListenerRule.ARG_OLD_VALUE, UUID.randomUUID());
        ruleParameters.put(ListenerRule.ARG_NEW_VALUE, UUID.randomUUID());
        return new JavaRuleContext(this.sailPointContext, ruleParameters);
    }
}
