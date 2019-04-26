package com.sailpoint.annotation.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Argument annotation for rule
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {

    /**
     * Name of argument. If default value - name will be get from field name or method name
     *
     * @return name of current argument
     */
    String name() default "";

    /**
     * Inputs or returns type. Default - INPUTS
     *
     * @return type for argument
     */
    ArgumentType type() default ArgumentType.INPUTS;

    /**
     * Prompt of attribute
     *
     * @return prompt current argument
     */
    String prompt() default "";

    /**
     * Required flag of argument. Default - false.
     *
     * @return is argument required
     */
    boolean required() default false;

    /**
     * Flag to set returns type from current elements.
     * Can be set only for 1 annotation per class. Default - false
     *
     * @return is this arguments must be used as return type for signature
     */
    boolean isReturnsType() default false;

}
