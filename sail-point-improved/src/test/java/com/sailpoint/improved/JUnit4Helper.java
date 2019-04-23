package com.sailpoint.improved;

import static org.junit.Assert.fail;

/**
 * Helper for junit tests
 */
public class JUnit4Helper {

    /**
     * Check exception after execution with expected exception class
     * If not is instance - failed
     *
     * @param exceptionClass - expected exception class
     * @param executable     - execution (exception source)
     */
    public static void assertThrows(Class<? extends Throwable> exceptionClass, Executable executable) {
        try {
            executable.execute();
        } catch (Throwable realException) {
            if (!exceptionClass.isInstance(realException)) {
                fail("Expected exception:[" + exceptionClass.getSimpleName() + "], real:[" + realException.getClass()
                        .getSimpleName() + "]");
            }
            return;
        }
        fail("Expected exception:[" + exceptionClass.getSimpleName() + "] but NO exception");
    }

    /**
     * Executable interface
     */
    @FunctionalInterface
    public interface Executable {
        /**
         * Functional method of interface
         *
         * @throws Throwable - expected exception instance
         */
        void execute() throws Throwable;
    }
}