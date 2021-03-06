package com.sailpoint.annotation.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Attribute annotation
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {

    /**
     * Name of attribute. If default value - name will be get from field name
     *
     * @return name of current attribute
     */
    String name() default "";

    /**
     * Default value of current attribute
     *
     * @return value of attribute
     */
    AttributeValue[] value() default {};
}
