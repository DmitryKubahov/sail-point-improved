package com.sailpoint.processor;

import com.sailpoint.annotation.Rule;
import com.sailpoint.exception.XmlWriteError;
import com.sailpoint.processor.builder.SignatureBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import sailpoint.tools.Util;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.Set;

@Slf4j
@SupportedOptions({"ruleGenerationPath"})
@SupportedAnnotationTypes("com.sailpoint.annotation.Rule")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RuleAnnotationProcessor extends AbstractSailPointAnnotationProcessor {

    /**
     * Rule file patters. Parameters:
     * 0 - path to rules folder
     * 1 - rule name
     */
    public static final String RULE_FILE_PATTERN = "{0}/{1}.xml";
    /**
     * Option name - path to generate xml from rules
     */
    public static final String RULE_GENERATION_PATH = "ruleGenerationPath";
    /**
     * Rule language value
     */
    public static final String RULE_LANGUAGE = "java";
    /**
     * Rule class attribute name in options
     */
    public static final String ATTR_RULE_JAVA_CLASS = "ruleClassAttributeName";
    /**
     * Default rule class attribute name
     */
    public static final String DEFAULT_RULE_JAVA_CLASS = "ruleClass";
    /**
     * Default path for generating xml rules
     */
    public static final String DEFAULT_PATH_XML_GENERATION = "../config/Rule";

    /**
     * Rule generation result path
     */
    private String xmlPath;
    /**
     * Java class name rule attribute name
     */
    private String ruleClassAttributeName;

    /**
     * Processing elements with rule annotations for generating rule xml
     *
     * @param annotations - all sets of annotations for rule generation
     * @param roundEnv    - current environment for getting rule classes
     * @return success generating or not
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

            log.debug("Setting rule java class[{}] to attribute:[{}]", ruleElement.getSimpleName(),
                    ruleClassAttributeName);
            rule.setAttribute(ruleClassAttributeName, ruleElement.asType().toString());

            log.debug("Setting description from java doc of rule class");
            rule.setDescription(Util.trimWhitespace(processingEnv.getElementUtils().getDocComment(ruleElement)));

            log.debug("Generating signature for the rule");
            rule.setSignature(SignatureBuilder.getInstance().buildSignature(processingEnv, ruleElement));

            log.debug("Parse rule to xml");
            String ruleXml = XML_OBJECT_FACTORY.toXml(rule);
            try {
                String fileName = MessageFormat.format(RULE_FILE_PATTERN, xmlPath, rule.getName());
                log.debug("Write rule to file:[{}]", fileName);
                FileUtils.writeStringToFile(new File(fileName), ruleXml, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                log.debug("Error while saving xml rule:[{}]", ex.getMessage());
                throw new XmlWriteError(rule.getName(), ex);
            }
        }

        return true;
    }

    /**
     * Initialize all necessary stuff
     *
     * @param processingEnv - current processing environment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.xmlPath = Optional.ofNullable(processingEnv.getOptions().get(RULE_GENERATION_PATH))
                .orElse(DEFAULT_PATH_XML_GENERATION);
        this.ruleClassAttributeName = Optional
                .ofNullable(processingEnv.getOptions().get(ATTR_RULE_JAVA_CLASS))
                .orElse(DEFAULT_RULE_JAVA_CLASS);
    }
}
