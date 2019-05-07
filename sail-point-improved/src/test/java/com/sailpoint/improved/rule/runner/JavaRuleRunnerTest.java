package com.sailpoint.improved.rule.runner;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Before;
import org.junit.Test;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.JavaRuleExecutor;
import sailpoint.object.Rule;
import sailpoint.tools.GeneralException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.sailpoint.improved.JUnit4Helper.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link JavaRuleRunner} class
 */
public class JavaRuleRunnerTest {

    /**
     * Instance of {@link JavaRuleRunner} for testing
     */
    private JavaRuleRunner javaRuleRunner;

    /**
     * Mock of {@link SailPointContext} for returning from {@link SailPointFactory#getCurrentContext()}
     */
    private SailPointContext sailPointContext;

    /**
     * Init java rule runner instance
     */
    @Before
    public void init() {
        this.javaRuleRunner = new JavaRuleRunner();
        this.sailPointContext = mock(SailPointContext.class);

        new MockUp<SailPointFactory>() {
            @Mock
            public SailPointContext getCurrentContext() {
                return sailPointContext;
            }
        };
    }

    /**
     * Test of normal execution
     * Input:
     * - rule with context = {@link JMockRule} class name
     * Output:
     * - result of mocked {@link JMockRule} instance
     * Expectation:
     * - call execute in mocked instance
     */
    @Test
    public void normalRun() throws Exception {
        String expectedResult = UUID.randomUUID().toString();

        JMockRule.ruleMock = mock(JMockRule.class);
        when(JMockRule.ruleMock.execute(any())).thenReturn(expectedResult);

        Rule rule = createTestRule();
        Object actualResult = javaRuleRunner.runJavaRule(rule, Collections.emptyMap(), Collections.emptyList());

        assertEquals("Expected result of rule execution is not match with actual", expectedResult, actualResult);
    }

    /**
     * Test of java rule executor rules storage
     * Input:
     * - rule with context = {@link JMockRule} class name
     * Expectation:
     * - test rule must instantiated only once
     */
    @Test
    public void rulesInstanceStorageTest() throws Exception {
        int executions = 1000;
        JMockRule.instanceCount = 0;

        JMockRule.ruleMock = mock(JMockRule.class);
        Rule rule = createTestRule();

        assertEquals("Instance count before test is not 0", 0, JMockRule.ruleMock.instanceCount);
        while (executions-- > 0) {
            javaRuleRunner.runJavaRule(rule, Collections.emptyMap(), Collections.emptyList());
        }
        assertEquals("Instance count after test is not 1", 1, JMockRule.ruleMock.instanceCount);
    }

    /**
     * Test of invalid rule execution
     * Input:
     * - rule without source (null value)
     * Output:
     * - general exception
     * Expectation:
     * - have not called execute in mocked instance
     */
    @Test
    public void nullSourceRuleRun() {
        String expectedResult = UUID.randomUUID().toString();

        JMockRule.ruleMock = mock(JMockRule.class);
        when(JMockRule.ruleMock.execute(any())).thenReturn(expectedResult);

        Rule rule = createTestRule();
        when(rule.getSource()).thenReturn(null);

        assertThrows(GeneralException.class,
                () -> javaRuleRunner.runJavaRule(rule, Collections.emptyMap(), Collections.emptyList()));
        verify(JMockRule.ruleMock, never()).execute(any());

    }

    /**
     * Test of invalid rule execution
     * Input:
     * - rule with empty source ("", " ", "             ")
     * Output:
     * - general exception
     * Expectation:
     * - have not called execute in mocked instance
     */
    @Test
    public void emptySourceRuleRun() {
        List<String> emptySourceValues = Arrays.asList("", " ", "             ");
        String expectedResult = UUID.randomUUID().toString();

        JMockRule.ruleMock = mock(JMockRule.class);
        when(JMockRule.ruleMock.execute(any())).thenReturn(expectedResult);

        emptySourceValues.forEach(emptySourceValue -> {
            Rule rule = createTestRule();
            when(rule.getSource()).thenReturn(emptySourceValue);
            assertThrows(GeneralException.class,
                    () -> javaRuleRunner.runJavaRule(rule, Collections.emptyMap(), Collections.emptyList()));
            verify(JMockRule.ruleMock, never()).execute(any());
        });
    }

    /**
     * Test of rule execution with runtime exception
     * Input:
     * - rule with execution error
     * Output:
     * - general exception
     * Expectation:
     * - call execute in mocked instance
     */
    @Test
    public void ruleExceptionExecutionRun() {
        JMockRule.ruleMock = mock(JMockRule.class);
        when(JMockRule.ruleMock.execute(any())).thenThrow(RuntimeException.class);

        Rule rule = createTestRule();
        assertThrows(GeneralException.class,
                () -> javaRuleRunner.runJavaRule(rule, Collections.emptyMap(), Collections.emptyList()));
        verify(JMockRule.ruleMock).execute(any());

    }

    /**
     * Create test rule for mock
     *
     * @return rule for mock
     */
    private Rule createTestRule() {
        Rule rule = mock(Rule.class);
        when(rule.getSource()).thenReturn(JMockRule.class.getName());
        return rule;
    }


    /**
     * Test class for testing java rule runner. It is wrapper class of mock same class.
     */
    public static class JMockRule implements JavaRuleExecutor {

        /**
         * Mock instance of java rule executor
         */
        private static JMockRule ruleMock;
        /**
         * Count for testing java rule executor rule instance storage
         */
        private static int instanceCount = 0;

        /**
         * Increment count of instances each time of instantiating rule
         */
        public JMockRule() {
            JMockRule.instanceCount++;
        }

        /**
         * Call execute method of mock rule
         *
         * @param javaRuleContext - rule context
         * @return execution result of mock
         */
        @Override
        public Object execute(JavaRuleContext javaRuleContext) {
            return JMockRule.ruleMock.execute(javaRuleContext);
        }
    }
}
