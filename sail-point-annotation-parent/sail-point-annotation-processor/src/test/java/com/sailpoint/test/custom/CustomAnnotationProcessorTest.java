package com.sailpoint.test.custom;

import com.sailpoint.processor.CustomAnnotationProcessor;
import com.sailpoint.test.AbstractAnnotationProcessorTest;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import sailpoint.object.Custom;
import sailpoint.server.SimulatedSailPointContext;

import javax.annotation.processing.Processor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link CustomAnnotationProcessor} class
 */
public class CustomAnnotationProcessorTest extends AbstractAnnotationProcessorTest {

    /**
     * Count of attributes in custom object (marked as attributes 5, all 6)
     */
    private static final int ATTRIBUTES_COUNT = 5;

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

        ATTRIBUTES_VALUES.put("stringValue", "single");
        ATTRIBUTES_VALUES.put("stringCollection", Collections.singletonList("singleCollection"));
        ATTRIBUTES_VALUES.put("stringsCollectionNatural", Arrays.asList("string1", "string2"));
        ATTRIBUTES_VALUES.put("stringsCollection", Arrays.asList("string1", "string2"));
        ATTRIBUTES_VALUES.put("attributeName", "nameTest");
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
    }

    /**
     * Create custom object annotation processor
     *
     * @return instance of custom object annotation processor
     */
    @Override
    protected Processor buildProcessor() {
        return new CustomAnnotationProcessor();
    }
}
