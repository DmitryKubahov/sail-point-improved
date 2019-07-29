package com.sailpoint.improved.custom;

import com.sailpoint.annotation.Custom;
import com.sailpoint.annotation.common.Attribute;
import com.sailpoint.annotation.common.AttributeValue;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test class for simple custom object
 */
@Data
@Custom("Test simple custom object")
public class TestSimpleCustomObject extends AbstractCustomObject {

    /**
     * Test string value
     */
    @Attribute(@AttributeValue("single"))
    private String stringValue;
    /**
     * Test string value as collection
     */
    @Attribute(@AttributeValue("singleElementInCollection"))
    private List<String> stringCollection;
    /**
     * Test strings values
     */
    @Attribute({
            @AttributeValue("string1"),
            @AttributeValue("string2")
    })
    private List<String> stringsCollectionNatural;
    /**
     * Test boolean value
     */
    @Attribute(@AttributeValue("true"))
    private Boolean booleanValue;
    /**
     * Test long value
     */
    @Attribute(@AttributeValue("123"))
    private Long longValue;
    /**
     * Test date value
     */
    @Attribute(@AttributeValue("01/01/2019 01:01:01"))
    private Date dateValue;
    /**
     * Test date map
     */
    @Attribute({
            @AttributeValue(key = "now", value = "now"),
            @AttributeValue(key = "01/01/2019 01:01:01", value = "01/01/2019 01:01:01")
    })
    private Map<String, Date> dateMap;
    /**
     * Test boolean map
     */
    @Attribute({
            @AttributeValue(key = "false", value = "false"),
            @AttributeValue(key = "true", value = "true")
    })
    private Map<String, Boolean> booleanMap;
    /**
     * Test set of string
     */
    @Attribute({
            @AttributeValue(value = "1"),
            @AttributeValue(value = "2"),
            @AttributeValue(value = "1")
    })
    private Set<String> setValue;

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
