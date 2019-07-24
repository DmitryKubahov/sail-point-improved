package com.sailpoint.custom;

import com.sailpoint.annotation.Custom;
import com.sailpoint.annotation.common.Attribute;
import com.sailpoint.annotation.common.AttributeValue;
import com.sailpoint.improved.custom.AbstractCustomObject;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Test class for simple custom object
 */
@Data
@Custom("Test simple custom object")
public class SimpleCustomObject extends AbstractCustomObject {

    /**
     * Test string value
     */
    @Attribute(@AttributeValue("single"))
    private String stringValue;
    /**
     * Test string value as collection
     */
    @Attribute(@AttributeValue("asd"))
    private List<String> stringCollection;
    /**
     * Test strings value with is collection flag = false
     */
    @Attribute({
            @AttributeValue("a"),
            @AttributeValue("b")
    })
    private List<String> stringsCollectionNatural;
    /**
     * Test strings value with is collection flag = false
     */
    @Attribute({
            @AttributeValue("true"),
            @AttributeValue("false")
    })
    private Map<String, Boolean> booleanMap;
    /**
     * Test date map
     */
    @Attribute({
            @AttributeValue("01/01/2019"),
            @AttributeValue("01/01/2019 01:01:01")
    })
    private Map<String, Date> dateMap;
    /**
     * Test attribute name
     */
    @Attribute(name = "attributeName", value = @AttributeValue("nameTest"))
    private String fieldName;

    /**
     * Test transient attribute name
     */
    private String transientField;

}
