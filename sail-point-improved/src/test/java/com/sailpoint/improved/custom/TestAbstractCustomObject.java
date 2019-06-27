package com.sailpoint.improved.custom;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.Custom;
import sailpoint.tools.GeneralException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link AbstractCustomObject} class
 */
@RunWith(MockitoJUnitRunner.class)
public class TestAbstractCustomObject {

    /**
     * Test context
     */
    private SailPointContext context;

    /**
     * Custom object instance to test
     */
    private TestSimpleCustomObject customObjectInstance;

    /**
     * Test custom object for testing of loading
     */
    private Custom custom;

    /**
     * Init all properties for test
     */
    @Before
    public void init() {
        this.context = mock(SailPointContext.class);
        this.customObjectInstance = buildTestSimpleCustomObject();
    }

    /**
     * Build {@link TestSimpleCustomObject} with test random data
     *
     * @return instance of test custom object with random data
     */
    private TestSimpleCustomObject buildTestSimpleCustomObject() {
        TestSimpleCustomObject customObjectInstance = new TestSimpleCustomObject();
        customObjectInstance.setStringValue(UUID.randomUUID().toString());
        customObjectInstance.setFieldName(UUID.randomUUID().toString());
        customObjectInstance.setStringCollection(buildRandomStringList());
        customObjectInstance.setStringsCollection(buildRandomStringList());
        customObjectInstance.setStringsCollectionNatural(buildRandomStringList());
        customObjectInstance.setTransientField(UUID.randomUUID().toString());
        return customObjectInstance;
    }

    /**
     * Build random string list
     *
     * @return random string list
     */
    private List<String> buildRandomStringList() {
        int size = new Random().nextInt(500);
        List<String> result = new ArrayList<>(size);
        while (size-- > 0) {
            result.add(UUID.randomUUID().toString());
        }
        return result;
    }

    /**
     * Testing generation custom object and saving it.
     * Input:
     * - test instance of java custom object with test random data
     * Expected:
     * saving sailpoint custom object with:
     * - same name
     * - same attributes
     */
    @Test
    public void saveNewCustomObjectTest() throws GeneralException {
        doAnswer(invocation -> {
            Custom custom = invocation.getArgument(0, Custom.class);
            assertNotNull("Result custom object can not be null ", custom);

            String name = TestSimpleCustomObject.class.getAnnotation(com.sailpoint.annotation.Custom.class).value();
            assertEquals("Name is not match", name, custom.getName());

            compareAttributes(getAttributes(this.customObjectInstance), custom.getAttributes());

            return null;
        }).when(this.context).saveObject(any());

        this.customObjectInstance.save(this.context);
        verify(this.context).getObjectByName(eq(Custom.class), any());
    }

    /**
     * Testing generation custom object and saving it.
     * Input:
     * - test instance of java custom object with null data
     * Expected:
     * saving sailpoint custom object with:
     * - same name
     * - empty attributes
     */
    @Test
    public void saveNewCustomObjectFromNullsFieldsTest() throws GeneralException {
        doAnswer(invocation -> {
            Custom custom = invocation.getArgument(0, Custom.class);
            assertNotNull("Result custom object can not be null ", custom);

            String name = TestSimpleCustomObject.class.getAnnotation(com.sailpoint.annotation.Custom.class).value();
            assertEquals("Name is not match", name, custom.getName());

            assertTrue("Custom object must contains empty attributes", custom.getAttributes().isEmpty());

            return null;
        }).when(this.context).saveObject(any());

        TestSimpleCustomObject loadTestSimpleCustomObject = new TestSimpleCustomObject();
        loadTestSimpleCustomObject.save(this.context);
        verify(this.context).getObjectByName(eq(Custom.class), any());
    }

    /**
     * Testing filling java custom object from sailpoint source data
     * Input:
     * - test sailpoint custom object
     * Expected:
     * - filled test java custom object with the same data as sailpoint custom object
     * - transient value must be null
     */
    @Test
    public void loadCustomObjectTest() throws GeneralException {
        doAnswer(invocation -> {
            String actualName = invocation.getArgument(1, String.class);
            String name = TestSimpleCustomObject.class.getAnnotation(com.sailpoint.annotation.Custom.class).value();
            assertEquals("Name is not match", name, actualName);

            this.custom = new Custom();
            this.custom.setName(name);
            this.custom.setAttributes(new Attributes<>(getAttributes(buildTestSimpleCustomObject())));

            return this.custom;
        }).when(this.context).getObjectByName(eq(Custom.class), any());

        TestSimpleCustomObject loadTestSimpleCustomObject = new TestSimpleCustomObject();
        loadTestSimpleCustomObject.load(this.context);

        verify(this.context).getObjectByName(eq(Custom.class), any());
        compareAttributes(this.custom.getAttributes(), getAttributes(loadTestSimpleCustomObject));
        assertNull("Transient value must be null", loadTestSimpleCustomObject.getTransientField());
    }

    /**
     * Testing filling java custom object from sailpoint source data with unknown attributes
     * Input:
     * - test sailpoint custom object with unknown attributes
     * Expected:
     * - test java custom object with nulls fields
     */
    @Test
    public void loadCustomObjectWithUnknownAttributesTest() throws GeneralException {
        doAnswer(invocation -> {
            String actualName = invocation.getArgument(1, String.class);
            String name = TestSimpleCustomObject.class.getAnnotation(com.sailpoint.annotation.Custom.class).value();
            assertEquals("Name is not match", name, actualName);

            this.custom = new Custom();
            this.custom.setName(name);
            this.custom.setAttributes(new Attributes<>(
                    Collections.singletonMap(UUID.randomUUID().toString(), UUID.randomUUID().toString())));
            return this.custom;
        }).when(this.context).getObjectByName(eq(Custom.class), any());

        TestSimpleCustomObject loadTestSimpleCustomObject = new TestSimpleCustomObject();
        loadTestSimpleCustomObject.load(this.context);

        verify(this.context).getObjectByName(eq(Custom.class), any());
        assertTrue("All properties must be NULL", getAttributes(loadTestSimpleCustomObject).entrySet().stream()
                .noneMatch(entry -> entry.getValue() != null));
    }

    /**
     * Testing filling java custom object with no custom object in sailpoint
     * Input:
     * - null as sailpoint custom object
     * Expected:
     * - test java custom object with nulls fields
     */
    @Test
    public void loadNoneExistedCustomObjectTest() throws GeneralException {
        TestSimpleCustomObject loadTestSimpleCustomObject = new TestSimpleCustomObject();
        loadTestSimpleCustomObject.load(this.context);

        verify(this.context).getObjectByName(eq(Custom.class), any());
        assertTrue("All properties must be NULL", getAttributes(loadTestSimpleCustomObject).entrySet().stream()
                .noneMatch(entry -> entry.getValue() != null));
    }

    /**
     * Testing filling java custom object from sailpoint source data with incapability argument type
     * Input:
     * - test sailpoint custom object with incapability attributes types
     * Expected:
     * - GeneralException
     */
    @Test(expected = GeneralException.class)
    public void loadCustomObjectWithIncapabilityArgumentsTypeTest() throws GeneralException {
        doAnswer(invocation -> {
            String actualName = invocation.getArgument(1, String.class);
            String name = TestSimpleCustomObject.class.getAnnotation(com.sailpoint.annotation.Custom.class).value();
            assertEquals("Name is not match", name, actualName);

            this.custom = new Custom();
            this.custom.setName(name);

            this.custom.setAttributes(new Attributes<>(Collections.singletonMap("stringValue", this)));
            return this.custom;
        }).when(this.context).getObjectByName(eq(Custom.class), any());

        TestSimpleCustomObject loadTestSimpleCustomObject = new TestSimpleCustomObject();
        loadTestSimpleCustomObject.load(this.context);
    }

    /**
     * Compare attributes data
     *
     * @param attributesExpected - expected values
     * @param attributesActual   - actual values
     */
    private void compareAttributes(Map<String, Object> attributesExpected, Map<String, Object> attributesActual) {
        assertEquals("Count of attributes is not match", attributesExpected.size(), attributesActual.size());
        for (Map.Entry<String, Object> entry : attributesExpected.entrySet()) {
            assertTrue("Does not contain attribute:" + entry.getKey(), attributesActual.containsKey(entry.getKey()));
            assertEquals("Value of attribute:" + entry.getKey() + " is not match", entry.getValue(),
                    attributesActual.get(entry.getKey()));
        }
    }

    /**
     * Get map of all valid attributes from java custom object
     *
     * @param customObjectInstance - test java custom object implementation
     */
    private Map<String, Object> getAttributes(TestSimpleCustomObject customObjectInstance) {
        Map<String, Object> result = new HashMap<>();
        result.put("stringValue", customObjectInstance.getStringValue());
        result.put("stringCollection", customObjectInstance.getStringCollection());
        result.put("stringsCollectionNatural", customObjectInstance.getStringsCollectionNatural());
        result.put("stringsCollection", customObjectInstance.getStringsCollection());
        result.put("attributeName", customObjectInstance.getFieldName());
        return result;
    }
}
