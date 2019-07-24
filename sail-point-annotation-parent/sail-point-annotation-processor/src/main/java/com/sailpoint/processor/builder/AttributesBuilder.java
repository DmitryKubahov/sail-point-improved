package com.sailpoint.processor.builder;

import com.sailpoint.annotation.common.Attribute;
import com.sailpoint.annotation.common.AttributeValue;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Attributes;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;
import sailpoint.tools.xml.DateString;
import sailpoint.tools.xml.XMLObjectFactoryHelper;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class for building attributes map with key-value pair. Key - always string (default value is ""),
 * value - depends on field type. It field type is collection -> than all values from 'value' attribute will be converted to
 * element type using serializer from {@link XMLObjectFactoryHelper#parseStringToClazz(Class, String)} by class type
 */
@Slf4j
public class AttributesBuilder {

    /**
     * Error message if could not determinate element type
     */
    private static final String COULD_NOT_DETERMINATE_ELEMENT_TYPE_ERROR_MESSAGE = "Could not determinate element type";

    /**
     * Processing environmental
     */
    private final ProcessingEnvironment processingEnvironment;

    /**
     * Xml object factory helper instance
     */
    private final XMLObjectFactoryHelper xmlObjectFactoryHelper;

    /**
     * Constructor with parameters
     *
     * @param processingEnvironment  - processing environment
     * @param xmlObjectFactoryHelper - xml object factory helper instance
     */
    public AttributesBuilder(ProcessingEnvironment processingEnvironment,
                             XMLObjectFactoryHelper xmlObjectFactoryHelper) {
        this.processingEnvironment = processingEnvironment;
        this.xmlObjectFactoryHelper = xmlObjectFactoryHelper;
    }

    /**
     * Build attributes from element element. Tries to find elements with annotation {@link Attribute} in current elements and also in all supers.
     *
     * @param element - current element
     * @return signature instance
     */
    public Attributes<String, Object> buildAttributes(Element element) throws GeneralException {
        log.debug("Initialize attributes");
        Attributes<String, Object> attributes = new Attributes<>();

        log.debug("Get all annotated fields in:[{}] by:[{}]", element, Attribute.class);
        for (Element argumentElement : BuilderHelper
                .getAnnotatedElements(processingEnvironment, element, Attribute.class)) {
            log.debug("Add element:[{}] to attributes", element);
            Attribute attributeAnnotation = argumentElement.getAnnotation(Attribute.class);
            log.trace("Annotation value:[{}]", attributeAnnotation);

            log.debug("Getting attribute name");
            String name = Util.isEmpty(attributeAnnotation.name()) ? argumentElement.getSimpleName()
                    .toString() : attributeAnnotation.name();
            log.debug("Adding name:[{}]", name);
            try {
                log.debug("Try to parse attribute:[{}] value", name);
                attributes.put(name, getAttributeValue(attributeAnnotation.value(), argumentElement.asType()));
            } catch (GeneralException ex) {
                log.error("Got error while parsing field:[{}]", argumentElement.getSimpleName());
                throw ex;
            }
        }
        log.trace("Attributes:[{}]", attributes);
        return attributes;
    }

    /**
     * Try to get value of attribute according its type and value/values
     *
     * @param value       - current value of attribute
     * @param elementType - current element type
     * @return value for element of root attribute
     * @throws GeneralException error getting value
     */
    private Object getAttributeValue(AttributeValue[] value, TypeMirror elementType)
            throws GeneralException {
        if (BuilderHelper.isAssignable(processingEnvironment, List.class, elementType)) {
            log.debug("Element type is list");
            return getListValue(value, elementType);
        } else if (BuilderHelper.isAssignable(processingEnvironment, Set.class, elementType)) {
            return getSetValue(value, elementType);
        } else if (BuilderHelper.isAssignable(processingEnvironment, Map.class, elementType)) {
            return getMapValue(value, elementType);
        } else {
            return getValue(elementType, value);
        }
    }

    /**
     * Return {@link HashMap} value of attribute values
     *
     * @param values  - attribute values
     * @param mapType - current type of value
     * @return list value of attribute
     * @throws GeneralException error of getting map pair element value
     */
    private Map getMapValue(AttributeValue[] values, TypeMirror mapType) throws GeneralException {
        log.debug("Try to get MAP value");
        if (values.length == 0) {
            log.debug("Values is empty, return empty map");
            return Collections.emptyMap();
        }
        Map<String, Object> result = new HashMap<>();
        for (AttributeValue value : values) {
            if (value.value().length != 0) {
                TypeMirror argumentType = ((DeclaredType) mapType).getTypeArguments().stream().skip(1).findFirst()
                        .orElseThrow(() -> new GeneralException(COULD_NOT_DETERMINATE_ELEMENT_TYPE_ERROR_MESSAGE));
                result.put(value.key(), getValue(argumentType, value));
            }
        }
        return result.isEmpty() ? Collections.emptyMap() : result;
    }

    /**
     * Return {@link HashSet} value of attribute values
     *
     * @param values      - attribute values
     * @param elementType - current type of value
     * @return list value of attribute
     * @throws GeneralException error of getting set element value
     */
    private Object getSetValue(AttributeValue[] values, TypeMirror elementType) throws GeneralException {
        log.debug("Try to get SET value");
        if (values.length == 0) {
            log.debug("Values is empty, return empty set");
            return Collections.emptySet();
        }
        Set result = new HashSet(values.length);
        fillCollection(result, values, elementType);
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Return {@link ArrayList} value of attribute values
     *
     * @param values      - attribute values
     * @param elementType - current type of value
     * @return list value of attribute
     * @throws GeneralException error of getting list element value
     */
    private List getListValue(AttributeValue[] values, TypeMirror elementType) throws GeneralException {
        log.debug("Try to get LIST value");
        if (values.length == 0) {
            log.debug("Values is empty, return empty list");
            return Collections.emptyList();
        }
        List result = new ArrayList(values.length);
        fillCollection(result, values, elementType);
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Fill collection instance from source value of attribute annotation
     *
     * @param collection     - instance of collection
     * @param values         - source values (string representation)
     * @param collectionType - annotated field real type
     * @throws GeneralException error getting value for type of element collection
     */
    private void fillCollection(Collection<Object> collection, AttributeValue[] values, TypeMirror collectionType)
            throws GeneralException {
        for (AttributeValue value : values) {
            if (value.value().length != 0) {
                TypeMirror argumentType = ((DeclaredType) collectionType).getTypeArguments().stream().findFirst()
                        .orElseThrow(() -> new GeneralException(COULD_NOT_DETERMINATE_ELEMENT_TYPE_ERROR_MESSAGE));
                collection.add(getValue(argumentType, value));
            }
        }
    }

    /**
     * Get real typed value from attribute value annotation
     *
     * @param attributeAnnotation - source annotation value
     * @param elementType         - current element type
     * @return typed value
     * @throws GeneralException error of getting type of element or parsing value to that type
     */
    private Object getValue(TypeMirror elementType, AttributeValue... attributeAnnotation) throws GeneralException {
        log.debug("Try to get value by type:[{}]", elementType);
        if (attributeAnnotation.length == 0 || attributeAnnotation[0].value().length == 0) {
            log.debug("Attribute value is empty, return null");
            return null;
        }
        String value = attributeAnnotation[0].value()[0];
        log.trace("String value;[{}]", value);
        try {
            Class<?> fieldType = Class.forName(elementType.toString());
            if (Date.class.equals(fieldType)) {
                return xmlObjectFactoryHelper.parseStringToClazz(DateString.class, value);
            }
            return xmlObjectFactoryHelper.parseStringToClazz(fieldType, value);
        } catch (ClassNotFoundException ex) {
            log.error("Could not find class");
            throw new GeneralException(ex);
        }

    }

}
