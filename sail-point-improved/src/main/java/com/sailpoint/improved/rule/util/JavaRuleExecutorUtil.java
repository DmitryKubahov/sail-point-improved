package com.sailpoint.improved.rule.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.GeneralException;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * Util class for using in java rule executors
 */
@Slf4j
public final class JavaRuleExecutorUtil {

    /**
     * Argument not null check error message. Parameters:
     * 0 - argument name
     */
    public static final String NOT_NULL_ARGUMENT_ERROR_MESSAGE = "Argument:[{0}] is null in rule context";

    /**
     * It is utils class, no need to instantiate it
     */
    private JavaRuleExecutorUtil() {
    }

    /**
     * Get argument value by name
     *
     * @param javaRuleContext - java rule context
     * @param argumentName    - argument name
     * @return argument value. Can be null.
     */
    public static Object getArgumentValueByName(@NonNull JavaRuleContext javaRuleContext,
                                                @NonNull String argumentName) {
        log.debug("Start getting argument:[{}] value from java rule context", argumentName);
        return Optional.ofNullable(javaRuleContext.getArguments()).map(arguments -> arguments.get(argumentName))
                .orElse(null);
    }

    /**
     * Not null validation of argument in rule context
     *
     * @param javaRuleContext - rule context to check
     * @param argumentName    - name of argument to validate
     * @throws GeneralException - attribute is null in rule context
     */
    public static void notNullArgumentValidation(@NonNull JavaRuleContext javaRuleContext, @NonNull String argumentName)
            throws GeneralException {
        log.debug("Validate argument:[{}] not null value", argumentName);
        if (getArgumentValueByName(javaRuleContext, argumentName) == null) {
            throw new GeneralException(MessageFormat.format(NOT_NULL_ARGUMENT_ERROR_MESSAGE, argumentName));
        }
    }
}
