package com.sailpoint.processor.builder;

import com.sailpoint.annotation.common.Attribute;
import com.sailpoint.processor.javadoc.JavaDocsStorageProvider;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Attributes;
import sailpoint.tools.Util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.util.Arrays;

/**
 * Class for building attributes instance from annotated elements
 */
@Slf4j
public class AttributesBuilder {

    /**
     * Java doc storage provider instance
     */
    private final JavaDocsStorageProvider javaDocsStorageProvider;

    /**
     * Processing environmental
     */
    private final ProcessingEnvironment processingEnvironment;

    /**
     * Constructor with parameters
     *
     * @param javaDocsStorageProvider - java doc storage provider
     * @param processingEnvironment   - processing environment
     */
    public AttributesBuilder(JavaDocsStorageProvider javaDocsStorageProvider,
                             ProcessingEnvironment processingEnvironment) {
        this.javaDocsStorageProvider = javaDocsStorageProvider;
        this.processingEnvironment = processingEnvironment;
    }

    /**
     * Build attributes from element element. Tries to find elements with annotation {@link Attribute} in current elements and also in all supers.
     *
     * @param element - current element
     * @return signature instance
     */
    public Attributes<String, Object> buildSignature(Element element) {
        log.debug("Initialize attributes");
        Attributes<String, Object> attributes = new Attributes<>();

        log.debug("Get all annotated fields in:[{}] by:[{}]", element, Attribute.class);
        for (Element argumentElement : BuilderHelper
                .getAnnotatedElements(processingEnvironment, element, Attribute.class)) {
            log.debug("Add element:[{}] to attributes", element);
            Attribute attributeAnnotation = argumentElement.getAnnotation(Attribute.class);

            log.debug("Getting attribute name");
            String name = Util.isEmpty(attributeAnnotation.name()) ? argumentElement.getSimpleName()
                    .toString() : attributeAnnotation.name();

            log.debug("Getting attribute:[{}] value", name);
            String[] value = attributeAnnotation.value();

            log.debug("Add attribute:[{}]", name);
            log.trace("Attribute name:[{}]. value:[{}]", name, value);
            attributes.put(name, convertValue(value, attributeAnnotation));

        }
        log.trace("Attributes:[{}]", attributes);
        return attributes;
    }

    /**
     * Convert value to single string or to list.
     * Rule:
     * if annotation marked as collection or value contains more than 1 elements -> return list, else simple string
     *
     * @param value      - value to convert
     * @param annotation - argument annotation
     * @return converted value
     */
    private Object convertValue(String[] value, Attribute annotation) {
        if (annotation.isCollection() || value.length > 1) {
            return Arrays.asList(value);
        }
        return value[0];
    }
}
