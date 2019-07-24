package com.sailpoint.annotation.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Attribute value. Type of value takes from field type.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AttributeValue {

    /**
     * Key of attribute, uses for map type
     *
     * @return name of current attribute
     */
    String key() default "";

    /**
     * Value of current attribute
     *
     * @return value of attribute
     */
    String[] value();
}
