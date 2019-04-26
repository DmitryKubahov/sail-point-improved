package com.sailpoint.processor.builder;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.processor.javadoc.JavaDocsStorageProvider;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Signature;
import sailpoint.tools.Util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Class for building signature instance from annotated elements
 */
@Slf4j
public class SignatureBuilder {

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
    public SignatureBuilder(JavaDocsStorageProvider javaDocsStorageProvider,
                            ProcessingEnvironment processingEnvironment) {
        this.javaDocsStorageProvider = javaDocsStorageProvider;
        this.processingEnvironment = processingEnvironment;
    }

    /**
     * Build signature from rule element. Tries to find elements with annotation {@link Argument}
     * or container of arguments {@link ArgumentsContainer} in current elements and also in all supers.
     *
     * @param element - current element
     * @return signature instance
     */
    public Signature buildSignature(Element element) {
        log.debug("Initialize signature, inputs and returns arguments");
        Signature signature = new Signature();
        signature.setArguments(new ArrayList<>());
        signature.setReturns(new ArrayList<>());

        log.debug("Enrich signature");
        enrichSignature(element, signature);

        log.debug("Return signature");
        log.trace("Signature:[{}]", signature);
        return signature;
    }

    /**
     * Analyses rule element.
     *
     * @param element   - element to handle
     * @param signature - signature instance to enrich
     */
    private void enrichSignature(Element element, Signature signature) {
        if (element == null) {
            log.debug("Element is null. Do no do anything.");
            return;
        }
        log.debug("Check element:[{}] type", element.getKind());
        if (ElementKind.CLASS.equals(element.getKind())) {
            log.debug("Element:[{}] is a class. Try to enrich signature for super class", element.getSimpleName());
            enrichSignature(Optional.of((Symbol.ClassSymbol) element).map(Symbol.ClassSymbol::getSuperclass)
                            .map(type -> processingEnvironment.getTypeUtils().asElement(type)).orElse(null),
                    signature);
        }
        log.debug("Check all elements from:[{}]", element.getSimpleName());
        element.getEnclosedElements()
                .forEach(innerElement -> this.enrichSignature(innerElement, signature));

        log.debug("Check element:[{}] argument annotation", element.getSimpleName());
        Argument argumentAnnotation = element.getAnnotation(Argument.class);
        if (argumentAnnotation == null) {
            log.debug("Element:[{}] is not mark as argument, skip it", element.getSimpleName());
            return;
        }
        sailpoint.object.Argument argument = new sailpoint.object.Argument();

        log.debug("Setting name:[{}] for argument", element.getSimpleName().toString());
        argument.setName(Util.isEmpty(argumentAnnotation.name())
                ? element.getSimpleName().toString()
                : argumentAnnotation.name());


        log.debug("Setting prompt:[{}] for argument", argumentAnnotation.prompt());
        String prompt = argumentAnnotation.prompt();
        if (!Util.isEmpty(prompt)) {
            argument.setPrompt(prompt.trim());
        }

        log.debug("Setting description for argument");
        argument.setDescription(javaDocsStorageProvider.readJavaDoc(element));

        log.debug("Setting required flag to argument");
        argument.setRequired(argumentAnnotation.required());

        log.debug("Determinate type of argument");
        TypeMirror elementType = getArgumentType(element);

        if (argumentAnnotation.isReturnsType()) {
            String returnType = processingEnvironment.getTypeUtils().erasure(elementType).toString();
            log.debug("Element:[{}] mark as returns type. Set:[{}] as return type", element.getSimpleName(),
                    returnType);
            signature.setReturnType(returnType);
        }

        String argumentType = elementType.toString();
        if (isCollectionType(elementType)) {
            log.debug("Type of element:[{}] is collection", elementType);
            argument.setMulti(true);
            if (elementType instanceof DeclaredType) {
                log.debug("Try to determinate collection element type of:[{}]", elementType);
                argumentType = ((DeclaredType) elementType).getTypeArguments().stream().findFirst()
                        .map(TypeMirror::toString).orElse(argumentType);
                log.debug("Type of collection is:[{}]", argumentType);
            }
        } else {
            argumentType = processingEnvironment.getTypeUtils().erasure(elementType).toString();
        }
        log.debug("Type of argument is:[{}]", argumentType);
        argument.setType(argumentType);

        log.debug("Argument type:[{}]", argumentAnnotation.type());
        if (ArgumentType.RETURNS.equals(argumentAnnotation.type())) {
            log.debug("Add argument to returns");
            signature.getReturns().add(argument);
        } else {
            log.debug("Add argument to inputs");
            signature.getArguments().add(argument);
        }
    }

    /**
     * Check is element is collection
     *
     * @param elementType - type to check
     * @return is collection type
     */
    private boolean isCollectionType(TypeMirror elementType) {
        log.trace("Init collection type from collection class:[{}]", Collection.class);
        TypeMirror collectionType = processingEnvironment.getTypeUtils()
                .erasure(processingEnvironment.getElementUtils().getTypeElement(Collection.class.getName()).asType());
        return processingEnvironment.getTypeUtils().isAssignable(elementType, collectionType);
    }

    /**
     * Get real type of element. If it a method - get return type
     *
     * @param element - source element of type
     * @return real type of argument
     */
    private TypeMirror getArgumentType(Element element) {
        TypeMirror elementType = element.asType();
        if (TypeKind.EXECUTABLE.equals(elementType.getKind())) {
            log.debug("It a method. Get return type");
            return ((Type.MethodType) elementType).getReturnType();
        }
        return elementType;
    }
}
