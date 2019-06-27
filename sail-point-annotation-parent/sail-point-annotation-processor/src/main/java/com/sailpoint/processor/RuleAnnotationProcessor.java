package com.sailpoint.processor;

import com.google.auto.service.AutoService;
import com.sailpoint.annotation.Rule;
import com.sailpoint.exception.RuleXmlObjectWriteError;
import com.sailpoint.processor.builder.SignatureBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import sailpoint.tools.Util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Set;

@Slf4j
@AutoService(Processor.class)
@SupportedOptions({SailPointAnnotationProcessorDictionary.GENERATION_PATH})
@SupportedAnnotationTypes("com.sailpoint.annotation.Rule")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RuleAnnotationProcessor extends AbstractSailPointAnnotationProcessor {

    /**
     * Rule language value
     */
    public static final String RULE_LANGUAGE = "java";
    /**
     * Pattern for path for generating xml rules. Parameters:
     * 1 - main part of path
     * 2 - rule file name
     */
    public static final String RULE_PATH_XML_GENERATION_PATTERN = "{0}/Rule/{1}";
    /**
     * Signature builder
     */
    protected SignatureBuilder signatureBuilder;

    /**
     * Processing elements with rule annotations for generating rule xml
     *
     * @param annotations - all sets of annotations for rule generation
     * @param roundEnv    - current environment for getting rule classes
     * @return return to complete handling {@link Rule} annotations
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        log.debug("Get all annotated rules");
        Set<? extends Element> ruleElements = roundEnv.getElementsAnnotatedWith(Rule.class);
        log.debug("Annotated rules count:[{}]", ruleElements.size());
        log.trace("Annotated rules objects:[{}]", ruleElements);

        for (Element ruleElement : ruleElements) {
            log.debug("Start generating rule for class:[{}]", ruleElement.getSimpleName());
            sailpoint.object.Rule rule = new sailpoint.object.Rule();

            log.debug("Read annotation Rule");
            Rule ruleAnnotation = ruleElement.getAnnotation(Rule.class);

            log.debug("Setting rule name");
            rule.setName(Util.isEmpty(ruleAnnotation.value())
                    ? ruleElement.getSimpleName().toString() : ruleAnnotation.value());

            log.debug("Setting rule type");
            if (!Util.isEmpty(ruleAnnotation.type())) {
                if (ruleAnnotation.type().length > 1) {
                    log.warn("Rule:[{}] contains more than 1 type. First will be accepted", rule.getName());
                }
                rule.setType(ruleAnnotation.type()[0]);
            }

            log.debug("Setting language [{}] to rule", RULE_LANGUAGE);
            rule.setLanguage(RULE_LANGUAGE);

            log.debug("Setting rule java class[{}] to rule source", ruleElement.getSimpleName());
            rule.setSource(ruleElement.asType().toString());

            log.debug("Setting description from java doc of rule class");
            rule.setDescription(javaDocsStorageProvider.readJavaDoc(ruleElement));

            log.debug("Generating signature for the rule");
            rule.setSignature(signatureBuilder.buildSignature(ruleElement));

            log.debug("Parse rule to xml");
            String ruleXml = xmlObjectFactory.toXml(rule);

            try {
                String xmlName = MessageFormat
                        .format(SailPointAnnotationProcessorDictionary.XML_FILE_PATTERN, rule.getName());
                String fileName = MessageFormat.format(RULE_PATH_XML_GENERATION_PATTERN, xmlPath, xmlName);
                log.debug("Write rule to file:[{}]", fileName);
                FileUtils.writeStringToFile(new File(fileName), ruleXml, StandardCharsets.UTF_8.name());
            } catch (IOException ex) {
                log.debug("Error while saving xml rule:[{}]", ex.getMessage());
                throw new RuleXmlObjectWriteError(rule.getName(), ex);
            }
        }
        return true;
    }

    /**
     * Init signature builder
     *
     * @param processingEnv - current environment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.signatureBuilder = new SignatureBuilder(javaDocsStorageProvider, processingEnv);
    }
}
