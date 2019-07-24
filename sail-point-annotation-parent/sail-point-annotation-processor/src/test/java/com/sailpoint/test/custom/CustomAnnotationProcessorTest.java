package com.sailpoint.test.custom;

import com.sailpoint.processor.CustomObjectAnnotationProcessor;
import com.sailpoint.test.AbstractAnnotationProcessorTest;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import sailpoint.object.Custom;
import sailpoint.server.SimulatedSailPointContext;
import sailpoint.tools.Util;

import javax.annotation.processing.Processor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link CustomObjectAnnotationProcessor} class
 */
public class CustomAnnotationProcessorTest extends AbstractAnnotationProcessorTest {

    /**
     * Count of attributes in custom object (marked as attributes 10, all 11)
     */
    private static final int ATTRIBUTES_COUNT = 10;

    /**
     * Test date string representation for testing data
     */
    private static final String DATE_TEST_STRING_CONSTANT = "02/15/2019 10:35:45";
    /**
     * Test date testing data
     */
    private static final Date DATE_TEST_CONSTANT;
    /**
     * Date map attribute name
     */
    private static final String ATTR_DATE_MAP = "dateMap";

    /**
     * Test attributes
     */
    private static final Map<String, Object> ATTRIBUTES_VALUES;
    /**
     * Path to simple custom object java class for test
     */
    private static final String PATH_TO_SIMPLE_CUSTOM_OBJECT = "custom/SimpleCustomObject.java";
    /**
     * Path to xml of current custom object after generating
     */
    private static final String PATH_TO_GENERATED_CUSTOM_OBJECT_XML = "Custom/Simple custom object.xml";

    static {
        ATTRIBUTES_VALUES = new HashMap<>();

        Map<String, Boolean> booleanMap = new HashMap<>();
        booleanMap.put("true", Boolean.TRUE);
        booleanMap.put("false", Boolean.FALSE);

        Set<String> setValue = new HashSet<>();
        setValue.add("1");
        setValue.add("2");

        try {
            DATE_TEST_CONSTANT = Util.stringToDate(DATE_TEST_STRING_CONSTANT);
            ATTRIBUTES_VALUES.put("stringValue", "single");
            ATTRIBUTES_VALUES.put("stringCollection", Collections.singletonList("stringCollection"));
            ATTRIBUTES_VALUES.put("booleanValue", Boolean.TRUE);
            ATTRIBUTES_VALUES.put("longValue", 5L);
            ATTRIBUTES_VALUES.put("dateValue", DATE_TEST_CONSTANT);
            ATTRIBUTES_VALUES.put("stringsCollectionNatural", Arrays.asList("string1", "string2"));
            ATTRIBUTES_VALUES.put("booleanMap", booleanMap);
            ATTRIBUTES_VALUES.put("setValue", setValue);
            ATTRIBUTES_VALUES.put("attributeName", "nameTest");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Return current rule file path
     *
     * @return rule path
     */
    @Override
    protected String getJavaClassFilePathName() {
        return PATH_TO_SIMPLE_CUSTOM_OBJECT;
    }

    /**
     * Try to read custom object from generated file and check all attributes
     */
    @Test
    public void checkCustomObjectRuleGenerator() throws IOException {
        String xml = IOUtils
                .toString(getClass().getClassLoader().getResourceAsStream(PATH_TO_GENERATED_CUSTOM_OBJECT_XML),
                        StandardCharsets.UTF_8.name());
        assertNotNull("Xml of custom object can not be null", xml);

        Custom custom = (Custom) XML_OBJECT_FACTORY.parseXml(new SimulatedSailPointContext(), xml, false);
        assertNotNull("Custom object can not be null", custom);

        assertEquals("Count of marked attributes does not match", ATTRIBUTES_COUNT, custom.getAttributes().size());
        for (Map.Entry<String, Object> attribute : ATTRIBUTES_VALUES.entrySet()) {
            String attributeName = attribute.getKey();
            assertTrue("Attribute:" + attributeName + " not found on xml", custom.containsAttribute(attributeName));
            assertEquals("Attribute:" + attributeName + " is not match", attribute.getValue(),
                    custom.get(attributeName));
        }
        assertTrue("Custom object must contains attribute:" + ATTR_DATE_MAP, custom.containsAttribute(ATTR_DATE_MAP));
        Map<String, Date> dateMap = (Map<String, Date>) custom.get(ATTR_DATE_MAP);
        assertNotNull("Date map must contains 'now' key", dateMap.get("now"));
        assertEquals("Value of test date is not equals", DATE_TEST_CONSTANT, dateMap.get(DATE_TEST_STRING_CONSTANT));
    }

    /**
     * Create custom object annotation processor
     *
     * @return instance of custom object annotation processor
     */
    @Override
    protected Processor buildProcessor() {
        return new CustomObjectAnnotationProcessor();
    }
}
