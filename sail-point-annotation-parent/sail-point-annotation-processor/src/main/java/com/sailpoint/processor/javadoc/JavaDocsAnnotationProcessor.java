package com.sailpoint.processor.javadoc;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import sailpoint.tools.Util;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Generate simple json file with one property - java doc
 */
@Slf4j
@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class JavaDocsAnnotationProcessor extends AbstractProcessor {

    /**
     * Java doc provider
     */
    private JavaDocsStorageProvider javaDocsProvider;

    /**
     * Processing elements with rule annotations for generating rule xml
     *
     * @param annotations - all sets of annotations for rule generation
     * @param roundEnv    - current environment for getting rule classes
     * @return complete handling or not. Return false to avoid not running all others processors
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        log.debug("Run throw all classes and saves java docs to each of them");
        roundEnv.getRootElements().forEach(this::saveJavaDoc);
        return false;
    }

    /**
     * Initialize java doc provider
     *
     * @param processingEnv - current processing environment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.javaDocsProvider = new JavaDocsStorageProvider(processingEnv);
    }

    /**
     * Save java docs to simple json file
     *
     * @param ruleElement - element to check java docs
     */
    private void saveJavaDoc(Element ruleElement) {
        log.debug("Try to save java docs for element:[{}]", ruleElement.getSimpleName());
        if (!Util.isEmpty(ruleElement.getEnclosedElements())) {
            ruleElement.getEnclosedElements().forEach(this::saveJavaDoc);
        }
        this.javaDocsProvider.saveJavaDoc(ruleElement);
    }
}
