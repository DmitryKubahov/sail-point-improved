package com.sailpoint.improved.rule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.GeneralException;

import java.util.Collections;
import java.util.UUID;

import static com.sailpoint.improved.JUnit4Helper.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link AbstractJavaRuleExecutor} class
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractJavaRuleExecutorTest {

    /**
     * Instance of {@link AbstractJavaRuleExecutor} to test
     */
    private AbstractJavaRuleExecutor abstractJavaRuleExecutor;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init instance of {@link AbstractJavaRuleExecutorTest#abstractJavaRuleExecutor} for testing
     */
    @Before
    public void init() {
        this.abstractJavaRuleExecutor = mock(AbstractJavaRuleExecutor.class, Mockito.CALLS_REAL_METHODS);
        this.sailPointContext = mock(SailPointContext.class);
    }

    /**
     * Test of normal execution
     * Input:
     * - valid java rule context
     * Output
     * - test data
     * Expectation:
     * - call validation
     * - call internalValidation
     *
     * @throws GeneralException error of validation
     */
    @Test
    public void normalTest() throws GeneralException {
        String testValue = UUID.randomUUID().toString();
        JavaRuleContext javaRuleContext = new JavaRuleContext(this.sailPointContext, Collections.emptyMap());
        when(abstractJavaRuleExecutor.internalExecute(eq(this.sailPointContext), any())).thenReturn(testValue);

        assertEquals("Expected result of rule execution is not match", testValue,
                this.abstractJavaRuleExecutor.execute(javaRuleContext));
        verify(abstractJavaRuleExecutor).validate(eq(javaRuleContext));
        verify(abstractJavaRuleExecutor).internalValidation(eq(javaRuleContext));
    }

    /**
     * Test of execution with invalid rule context
     * Input:
     * - invalid java rule context (without sailpoint context)
     * Output
     * - General exception
     * Expectation:
     * - call validation
     * - NOT call internalValidation
     * - NOT call internalExecute
     */
    @Test
    public void nullSailPointContextTest() throws GeneralException {
        JavaRuleContext javaRuleContext = new JavaRuleContext(null, Collections.emptyMap());
        assertThrows(NullPointerException.class, () -> this.abstractJavaRuleExecutor.execute(javaRuleContext));

        verify(abstractJavaRuleExecutor).validate(eq(javaRuleContext));
        verify(abstractJavaRuleExecutor, never()).internalValidation(any());
        verify(abstractJavaRuleExecutor, never()).internalExecute(any(), any());
    }

}
