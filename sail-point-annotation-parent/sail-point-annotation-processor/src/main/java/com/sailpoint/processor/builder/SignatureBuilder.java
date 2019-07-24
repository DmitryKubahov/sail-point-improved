package com.sailpoint.processor.builder;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.processor.javadoc.JavaDocsStorageProvider;
import com.sun.tools.javac.code.Type;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Signature;
import sailpoint.tools.Util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Collection;

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

        log.debug("Get all annotated fields in:[{}] by:[{}]", element, Argument.class);
        for (Element argumentElement : BuilderHelper
                .getAnnotatedElements(processingEnvironment, element, Argument.class)) {
            log.debug("Add element:[{}] to signature", element);
            Argument argumentAnnotation = argumentElement.getAnnotation(Argument.class);
            sailpoint.object.Argument argument = new sailpoint.object.Argument();

            log.debug("Setting name:[{}] for argument", element.getSimpleName().toString());
            setArgumentName(argument, argumentAnnotation, argumentElement);

            log.debug("Setting prompt:[{}] for argument", argumentAnnotation.prompt());
            setPrompt(argument, argumentAnnotation);

            log.debug("Setting description for argument");
            argument.setDescription(javaDocsStorageProvider.readJavaDoc(argumentElement));

            log.debug("Setting required flag to argument");
            argument.setRequired(argumentAnnotation.required());

            log.debug("Determinate type of argument");
            TypeMirror elementType = getRealElementType(argumentElement);

            if (argumentAnnotation.isReturnsType()) {
                String returnType = processingEnvironment.getTypeUtils().erasure(elementType).toString();
                log.debug("Element:[{}] mark as returns type. Set:[{}] as return type", argumentElement.getSimpleName(),
                        returnType);
                signature.setReturnType(returnType);
            }

            log.debug("Setting argument type for:[{}]", elementType);
            setArgumentType(argument, elementType);

            log.debug("Argument type:[{}]", argumentAnnotation.type());
            if (ArgumentType.RETURNS.equals(argumentAnnotation.type())) {
                log.debug("Add argument to returns");
                signature.getReturns().add(argument);
            } else {
                log.debug("Add argument to inputs");
                signature.getArguments().add(argument);
            }
        }
        log.debug("Return signature");
        log.trace("Signature:[{}]", signature);
        return signature;
    }

    /**
     * Determinate real type of argument
     *
     * @param argument    - argument source
     * @param elementType - element type
     */
    private void setArgumentType(sailpoint.object.Argument argument, TypeMirror elementType) {
        String argumentType = elementType.toString();
        if (BuilderHelper.isAssignable(processingEnvironment, Collection.class, elementType)) {
            log.debug("Type of element:[{}] is collection", elementType);
            argument.setMulti(true);
            log.debug("Try to determinate collection element type of:[{}]", elementType);
            argumentType = ((DeclaredType) elementType).getTypeArguments().stream().findFirst()
                    .map(TypeMirror::toString).orElse(argumentType);
            log.debug("Type of collection is:[{}]", argumentType);
        } else {
            argumentType = processingEnvironment.getTypeUtils().erasure(elementType).toString();
        }
        argument.setType(argumentType);
        log.debug("Type of argument is:[{}]", argumentType);
    }

    /**
     * Setting prompt to argument if it is not empty in annotation
     *
     * @param argument           - target argument
     * @param argumentAnnotation - annotation instance for current argument
     */
    private void setPrompt(sailpoint.object.Argument argument, Argument argumentAnnotation) {
        String prompt = argumentAnnotation.prompt();
        if (!Util.isEmpty(prompt)) {
            argument.setPrompt(prompt.trim());
        }
    }

    /**
     * Set argument name as element name if annotation contains empty name or from annotation
     *
     * @param argument           - current argument to set name
     * @param argumentAnnotation - annotation value of current argument
     * @param argumentElement    - argument element
     */
    private void setArgumentName(sailpoint.object.Argument argument, Argument argumentAnnotation,
                                 Element argumentElement) {
        argument.setName(Util.isEmpty(argumentAnnotation.name())
                ? argumentElement.getSimpleName().toString()
                : argumentAnnotation.name());
    }

    /**
     * Get real type of element. If it a method - get return type
     *
     * @param element - source element of type
     * @return real type of argument
     */
    private TypeMirror getRealElementType(Element element) {
        TypeMirror elementType = element.asType();
        if (TypeKind.EXECUTABLE.equals(elementType.getKind())) {
            log.debug("It a method. Get return type");
            return ((Type.MethodType) elementType).getReturnType();
        }
        return elementType;
    }
}
