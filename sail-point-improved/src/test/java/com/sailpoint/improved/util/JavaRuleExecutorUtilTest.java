package com.sailpoint.improved.util;

import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import org.junit.Before;
import org.junit.Test;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.GeneralException;

import java.util.HashMap;
import java.util.UUID;

import static com.sailpoint.improved.JUnit4Helper.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link JavaRuleExecutorUtil} class
 */
public class JavaRuleExecutorUtilTest {

    /**
     * Java rule context instance for testing
     */
    private JavaRuleContext javaRuleContext;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    @Before
    public void init() {
        this.sailPointContext = mock(SailPointContext.class);
        this.javaRuleContext = new JavaRuleContext(sailPointContext, new HashMap<>());
    }

    /**
     * Test of getting arguments value from rule context
     * Input:
     * - java rule context with argument value by tested name
     * Output:
     * - value of tested argument
     */
    @Test
    public void getArgumentValueByNameTest() {
        String argumentName = UUID.randomUUID().toString();
        String argumentValue = UUID.randomUUID().toString();
        javaRuleContext.getArguments().put(argumentName, argumentValue);

        assertEquals("Tested argument value is not match", argumentValue,
                JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext, argumentName));
    }

    /**
     * Test of getting arguments value from rule context
     * Input:
     * - java rule context with null arguments
     * Output:
     * - null value
     * Expectation:
     * - no exception
     */
    @Test
    public void ruleContextNulArgumentsTest() {
        String argumentName = UUID.randomUUID().toString();
        JavaRuleContext javaRuleContext = mock(JavaRuleContext.class);
        when(javaRuleContext.getArguments()).thenReturn(null);

        assertNull(JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext, argumentName));
    }

    /**
     * Test of not null validation of argument in rule context
     * Input:
     * - java rule context with not null argument value by tested name
     * Output:
     * - without error
     */
    @Test
    public void notNullArgumentValidationTest() throws GeneralException {
        String argumentName = UUID.randomUUID().toString();
        String argumentValue = UUID.randomUUID().toString();
        javaRuleContext.getArguments().put(argumentName, argumentValue);
        JavaRuleExecutorUtil.notNullArgumentValidation(javaRuleContext, argumentName);
    }

    /**
     * Test of not null validation of argument in rule context
     * Input:
     * - java rule context with null argument value by tested name
     * Output:
     * - general exception
     */
    @Test
    public void notNullArgumentValidationWithNullValueTest() {
        String argumentName = UUID.randomUUID().toString();
        javaRuleContext.getArguments().put(argumentName, null);
        assertThrows(GeneralException.class, () ->
                JavaRuleExecutorUtil.notNullArgumentValidation(javaRuleContext, argumentName));
    }

    /**
     * Test of not null validation of argument in rule context
     * Input:
     * - java rule context with null arguments
     * Output:
     * - general exception
     */
    @Test
    public void notNullArgumentValidationWithNullContextArgumentsValueTest() {
        String argumentName = UUID.randomUUID().toString();
        JavaRuleContext javaRuleContext = mock(JavaRuleContext.class);
        when(javaRuleContext.getArguments()).thenReturn(null);
        assertThrows(GeneralException.class, () ->
                JavaRuleExecutorUtil.notNullArgumentValidation(javaRuleContext, argumentName));
    }


}
