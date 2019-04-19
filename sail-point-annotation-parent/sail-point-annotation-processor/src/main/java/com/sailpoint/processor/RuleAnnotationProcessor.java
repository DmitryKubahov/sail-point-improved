package com.sailpoint.processor;

import com.google.auto.service.AutoService;
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
     * Path for generating xml rules
     */
    public static final String RULE_PATH_XML_GENERATION = "rule/";

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

            log.debug("Setting rule java class[{}] to rule source", ruleElement.getSimpleName());
            rule.setSource(ruleElement.asType().toString());

            log.debug("Setting description from java doc of rule class");
            rule.setDescription(Util.trimWhitespace(processingEnv.getElementUtils().getDocComment(ruleElement)));

            log.debug("Generating signature for the rule");
            rule.setSignature(SignatureBuilder.getInstance().buildSignature(processingEnv, ruleElement));

            log.debug("Parse rule to xml");
            String ruleXml = xmlObjectFactory.toXml(rule);

            try {
                String xmlName = MessageFormat
                        .format(SailPointAnnotationProcessorDictionary.XML_FILE_PATTERN, rule.getName());
                String fileName = xmlPath.concat(RULE_PATH_XML_GENERATION).concat(xmlName);
                log.debug("Write rule to file:[{}]", fileName);
                FileUtils.writeStringToFile(new File(fileName), ruleXml, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                log.debug("Error while saving xml rule:[{}]", ex.getMessage());
                throw new XmlWriteError(rule.getName(), ex);
            }
        }

        return true;
    }
}
