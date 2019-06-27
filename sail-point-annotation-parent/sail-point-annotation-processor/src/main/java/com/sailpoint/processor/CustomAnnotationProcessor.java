package com.sailpoint.processor;

import com.google.auto.service.AutoService;
import com.sailpoint.annotation.Custom;
import com.sailpoint.exception.CustomObjectXmlObjectWriteError;
import com.sailpoint.processor.builder.AttributesBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import sailpoint.object.Attributes;
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
@SupportedAnnotationTypes("com.sailpoint.annotation.Custom")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CustomAnnotationProcessor extends AbstractSailPointAnnotationProcessor {

    /**
     * Path pattern for generating xml of custom objects. Parameters:
     * 1 - main part of path
     * 2 - custom object file name
     */
    public static final String CUSTOM_PATH_XML_GENERATION_PATTERN = "{0}/Custom/{1}";
    /**
     * Attribute builder
     */
    protected AttributesBuilder attributesBuilder;

    /**
     * Processing elements with {@link Custom} annotations for generating xml
     *
     * @param annotations - all sets of annotations for custom object generation
     * @param roundEnv    - current environment for getting custom classes
     * @return return to complete handling {@link Custom} annotations
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        log.debug("Get all annotated custom object");
        Set<? extends Element> customElements = roundEnv.getElementsAnnotatedWith(Custom.class);
        log.debug("Annotated custom objects count:[{}]", customElements.size());
        log.trace("Annotated custom objects:[{}]", customElements);

        for (Element customElement : customElements) {
            log.debug("Start generating custom for class:[{}]", customElement.getSimpleName());
            sailpoint.object.Custom customObject = new sailpoint.object.Custom();

            log.debug("Read annotation custom");
            Custom customAnnotation = customElement.getAnnotation(Custom.class);

            log.debug("Setting custom object name");
            customObject.setName(Util.isEmpty(customAnnotation.value())
                    ? customElement.getSimpleName().toString() : customAnnotation.value());

            log.debug("Setting description from java doc of custom object class");
            customObject.setDescription(javaDocsStorageProvider.readJavaDoc(customElement));

            log.debug("Try to get all attributes fields");
            Attributes<String, Object> attributes = attributesBuilder.buildSignature(customElement);
            log.trace("Attributes:[{}]", attributes);

            log.debug("Set attributes to custom object:[{}]", customObject.getName());
            customObject.setAttributes(attributes);

            try {
                log.debug("Parse custom object to xml");
                String customXml = xmlObjectFactory.toXml(customObject);
                log.trace("XML:[{}]", customXml);

                String xmlName = MessageFormat
                        .format(SailPointAnnotationProcessorDictionary.XML_FILE_PATTERN, customObject.getName());
                String fileName = MessageFormat.format(CUSTOM_PATH_XML_GENERATION_PATTERN, xmlPath, xmlName);

                log.debug("Write custom object to file:[{}]", fileName);
                FileUtils.writeStringToFile(new File(fileName), customXml, StandardCharsets.UTF_8.name());
            } catch (IOException ex) {
                log.debug("Error while saving xml of custom object:[{}]", ex.getMessage());
                throw new CustomObjectXmlObjectWriteError(customObject.getName(), ex);
            }
        }
        return true;
    }

    /**
     * Init also attribute builder
     *
     * @param processingEnv - current environment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.attributesBuilder = new AttributesBuilder(javaDocsStorageProvider, processingEnv);
    }
}
