package com.sailpoint.processor.javadoc;

import com.sailpoint.exception.AnnotationProcessorException;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.util.Name;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import sailpoint.tools.Util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Storage provider for java docs
 */
@Slf4j
public class JavaDocsStorageProvider {

    /**
     * Supported elements kind
     */
    private static final List<ElementKind> SUPPORTED_ELEMENTS_KIND = Arrays.asList(
            ElementKind.CLASS, ElementKind.FIELD
    );

    /**
     * File json pattern for java doc file
     */
    private static final String JAVA_DOC_JSON_FILE_PATTERN = "{0}.jdoc";

    /**
     * Processor event instance
     */
    private final ProcessingEnvironment processingEnv;
    /**
     * Package resolver
     */
    private final JavaDocPackageResolver packageResolver;

    /**
     * Constructor with parameters
     *
     * @param processingEnv - processing environment instance
     */
    public JavaDocsStorageProvider(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.packageResolver = new JavaDocPackageResolver();
    }

    /**
     * Save (element kind is supported and not empty) java docs to simple file.
     *
     * @param element - element to check
     */
    public void saveJavaDoc(@NonNull Element element) {
        if (element.getSimpleName() == null || !SUPPORTED_ELEMENTS_KIND.contains(element.getKind())) {
            log.debug("Element will not be handled.");
            return;
        }
        log.debug("Check java docs for element:[{}]", element);
        String javaDocValue = handleJavaDocsValue(processingEnv.getElementUtils().getDocComment(element));
        if (!Util.isEmpty(javaDocValue)) {
            try (Writer javaDocWriter = getFileObject(element, false).openWriter()) {
                javaDocWriter.write(javaDocValue);
            } catch (IOException ex) {
                log.error("Got error:[{}] while writing javadocs", ex.getMessage());
                throw new AnnotationProcessorException(element.getSimpleName().toString(), ex);
            }
        }
    }

    /**
     * Read java docs from element or from source file
     *
     * @param element - element source of java docs
     * @return java doc if found and null otherwise
     */
    public String readJavaDoc(@NonNull Element element) {
        log.debug("Try to read java doc for:[{}]", element);
        String javaDoc = processingEnv.getElementUtils().getDocComment(element);
        if (!Util.isEmpty(javaDoc)) {
            log.debug("Java doc for:[{}] is:[{}]", element, javaDoc);
            return handleJavaDocsValue(javaDoc);
        }
        if (!SUPPORTED_ELEMENTS_KIND.contains(element.getKind())) {
            log.debug("Unsupported element:[{}], return null as java doc", element);
            return null;
        }
        try (Reader javaDocReader = getFileObject(element, true).openReader(false)) {
            return IOUtils.toString(javaDocReader);
        } catch (IOException ex) {
            log.debug("No file with java doc for element:[{}]", element);
        }
        return null;
    }

    /**
     * Trim current java docs value
     *
     * @param javaDoc - current java docs value
     * @return trimmed java docs
     */
    private String handleJavaDocsValue(String javaDoc) {
        return Optional.ofNullable(javaDoc).map(String::trim).orElse(null);
    }

    /**
     * Get file object for current element
     *
     * @param element - element source
     * @param forRead - flag for opening or creating file object
     * @return file object
     * @throws IOException error creating/opening file object
     */
    private FileObject getFileObject(Element element, boolean forRead) throws IOException {
        String elementName = element.getSimpleName().toString();
        String packageValue = element.accept(packageResolver, null);
        String fileName = MessageFormat.format(JAVA_DOC_JSON_FILE_PATTERN, elementName);
        return forRead ? processingEnv.getFiler().getResource(StandardLocation.CLASS_PATH, packageValue, fileName)
                : processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, packageValue, fileName);
    }

    /**
     * Java doc package file resolver. Current supported:
     * - FIELD (visitType)
     * - CLASS (visitVariable)
     */
    private static class JavaDocPackageResolver implements ElementVisitor<String, Void> {

        /**
         * Resolve package for class type kind
         *
         * @param element - class element
         * @param aVoid   - empty type of visitor parameter
         * @return package value
         */
        @Override
        public String visitType(TypeElement element, Void aVoid) {
            return Optional.of(((Symbol.ClassSymbol) element)).map(Symbol.ClassSymbol::packge).map(
                    Symbol.PackageSymbol::getQualifiedName).map(Name::toString).orElse("");
        }

        /**
         * Resolve package for class type kind
         *
         * @param element - field element
         * @param aVoid   - empty type of visitor parameter
         * @return package value for field element
         */
        @Override
        public String visitVariable(VariableElement element, Void aVoid) {
            return Optional.of(((Symbol.VarSymbol) element).owner).map(Symbol::getQualifiedName).map(Name::toString)
                    .orElse("");
        }

        /**
         * Unsupported stuff
         */
        @Override
        public String visit(Element e, Void aVoid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String visit(Element e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String visitPackage(PackageElement e, Void aVoid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String visitExecutable(ExecutableElement e, Void aVoid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String visitTypeParameter(TypeParameterElement e, Void aVoid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String visitUnknown(Element e, Void aVoid) {
            throw new UnsupportedOperationException();
        }
    }
}
