package com.sailpoint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for classes to generate rule xml for sail point
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Rule {

    /**
     * Rule name value
     *
     * @return rule name
     */
    String value() default "";

    /**
     * Type of rule. It is a array - but must contains only 1 type
     *
     * @return type of rule
     */
    sailpoint.object.Rule.Type[] type() default {};
}
