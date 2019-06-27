package com.sailpoint.custom;

import com.sailpoint.annotation.Custom;
import com.sailpoint.annotation.common.Attribute;
import com.sailpoint.improved.custom.AbstractCustomObject;
import lombok.Data;

import java.util.List;

/**
 * Test class for simple custom object
 */
@Data
@Custom("Test simple custom object")
public class SimpleCustomObject extends AbstractCustomObject {

    /**
     * Test string value
     */
    @Attribute(value = "single")
    private String stringValue;
    /**
     * Test string value as collection
     */
    @Attribute(value = "singleCollection", isCollection = true)
    private List<String> stringCollection;
    /**
     * Test strings value with is collection flag = false
     */
    @Attribute(value = {"string1", "string2"})
    private List<String> stringsCollectionNatural;
    /**
     * Test strings value with is collection flag = true
     */
    @Attribute(value = {"string1", "string2"}, isCollection = true)
    private List<String> stringsCollection;
    /**
     * Test attribute name
     */
    @Attribute(name = "attributeName", value = "nameTest")
    private String fieldName;

    /**
     * Test transient attribute name
     */
    private String transientField;

}
