package com.sailpoint.processor.builder;

import com.sun.tools.javac.code.Symbol;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Contains common methods for all builders
 */
@Slf4j
public class BuilderHelper {

    /**
     * Only statics method
     */
    private BuilderHelper() {

    }

    /**
     * Get all elements with passed annotations
     *
     * @param processingEnvironment - processing environment
     * @param element               - element to handle
     * @param annotation            - annotation type to find
     */
    public static List<Element> getAnnotatedElements(ProcessingEnvironment processingEnvironment,
                                                     Element element,
                                                     Class<? extends Annotation> annotation) {
        if (element == null) {
            log.debug("Element is null -> return empty collection");
            return Collections.emptyList();
        }
        List<Element> result = new ArrayList<>();
        log.debug("Check element type:[{}]", element.getKind());
        if (ElementKind.CLASS.equals(element.getKind())) {
            log.debug("Element:[{}] is a class. Try to find annotated elements in super class",
                    element.getSimpleName());
            result.addAll(getAnnotatedElements(processingEnvironment,
                    Optional.of((Symbol.ClassSymbol) element).map(Symbol.ClassSymbol::getSuperclass)
                            .map(type -> processingEnvironment.getTypeUtils().asElement(type)).orElse(null),
                    annotation));
        }
        log.debug("Check all elements from:[{}]", element.getSimpleName());
        element.getEnclosedElements().forEach(
                innerElement -> result.addAll(getAnnotatedElements(processingEnvironment, innerElement, annotation)));

        log.debug("Check element:[{}] annotation", element.getSimpleName());
        if (element.getAnnotation(annotation) != null) {
            result.add(element);
        }
        return result;
    }

    /**
     * Check is element of passed class (e.g. collection, list, map...)
     *
     * @param processingEnvironment - current processing environment
     * @param type                  - type to check
     * @param elementType           - type to check
     * @return is element is assignable of passed class
     */
    public static boolean isAssignable(ProcessingEnvironment processingEnvironment, Class type,
                                       TypeMirror elementType) {
        log.trace("Init collection type from collection class:[{}]", Collection.class);
        TypeMirror collectionType = processingEnvironment.getTypeUtils()
                .erasure(processingEnvironment.getElementUtils().getTypeElement(type.getName()).asType());
        return processingEnvironment.getTypeUtils().isAssignable(elementType, collectionType);
    }
}
