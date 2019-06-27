package com.sailpoint.improved.custom;

import com.sailpoint.annotation.common.Attribute;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Attributes;
import sailpoint.object.Custom;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Common class for all custom objects
 */
@Slf4j
public abstract class AbstractCustomObject {

    /**
     * Name of custom object
     */
    private final String name;

    /**
     * Constructor for determinate name of custom object
     */
    protected AbstractCustomObject() {
        this.name = Optional.ofNullable(this.getClass().getAnnotation(com.sailpoint.annotation.Custom.class)).map(
                com.sailpoint.annotation.Custom::value).orElse(getClass().getName());
    }

    /**
     * Build attributes with all values
     *
     * @return attributes for current custom object
     * @throws GeneralException error of building attributes
     */
    public Attributes<String, Object> getAttributes() throws GeneralException {
        log.debug("Get all attributes with values");
        Attributes<String, Object> attributes = new Attributes<>();

        for (Field field : getAttributeFields()) {
            Attribute attribute = field.getAnnotation(Attribute.class);
            log.trace("Try to get field:[{}] value", field.getName());
            try {
                String attributeName = Util.isEmpty(attribute.name()) ? field.getName() : attribute.name();

                field.setAccessible(true);
                Object attributeValue = field.get(this);
                if (attributeValue == null) {
                    log.trace("Attribute:[{}] value is null -> do not add it to custom", attributeName);
                } else {
                    log.trace("Put attribute name:[{}], value:[{}] to result", attributeName, attributeValue);
                    attributes.put(attributeName, attributeValue);
                }
            } catch (IllegalAccessException ex) {
                log.error("Could not read field:[{}] value", field.getName(), ex);
                throw new GeneralException(ex);
            }
        }
        return attributes;
    }

    /**
     * Try to fill real custom object from sail point by name via current sail point context
     *
     * @throws GeneralException error of getting current sail point context or loading custom object
     */
    public void load() throws GeneralException {
        log.debug("Start loading custom object by current sail point context by name:[{}]", name);
        load(SailPointFactory.getCurrentContext());
        log.debug("Finish loading custom object by current sail point context by name:[{}]", name);
    }

    /**
     * Try to fill real custom object from sail point by name using sail point context
     *
     * @param sailPointContext - sail point context for loading custom object
     */
    public void load(@NonNull SailPointContext sailPointContext) throws GeneralException {
        log.debug("Loading custom object by name:[{}]", name);
        Custom custom = sailPointContext.getObjectByName(Custom.class, name);
        if (custom == null) {
            log.warn("Could not find custom object by name:[{}]", name);
            return;
        }
        log.debug("Loaded custom object by name:[{}]", name);
        log.trace("Custom object:{}", custom);

        log.debug("Get all fields and check attribute annotation");
        for (Field field : getAttributeFields()) {
            Attribute attribute = field.getAnnotation(Attribute.class);
            log.trace("Field:[{}] is attribute", field.getName());
            String attributeName = Util.isEmpty(attribute.name()) ? field.getName() : attribute.name();
            log.trace("Field:[{}], attribute real name:[{}]", field.getName(), attributeName);
            if (!custom.containsAttribute(attributeName)) {
                log.warn("Current custom object name:[{}], id:[{}], does not contain attribute by name:[{}]", name,
                        custom.getId(), attributeName);
            } else {
                Object attributeValue = custom.get(attributeName);
                log.debug("Try to set value to field:[{}], attribute name:[{}]", field.getName(), attributeName);
                log.trace("Attribute value:[{}]", attributeValue);
                field.setAccessible(true);
                try {
                    log.trace("Try to set field value");
                    field.set(this, attributeValue);
                } catch (Exception ex) {
                    log.error("Got error while setting field:[{}] to custom object:[{}]", field.getName(), name);
                    throw new GeneralException(ex);
                }
            }
        }
    }

    /**
     * Try to save current custom object to sailpoint via current context
     *
     * @throws GeneralException error getting context or saving custom object
     */
    public void save() throws GeneralException {
        log.debug("Try to save custom object via current context");
        save(SailPointFactory.getCurrentContext());
    }

    /**
     * Saving current custom object to sailpoint
     *
     * @param sailPointContext - sailpoint context
     * @throws GeneralException error of saving custom object
     */
    public void save(SailPointContext sailPointContext) throws GeneralException {
        log.debug("Start saving current custom object to db");
        log.debug("Try to get current object from DB");
        Custom existedCustom = sailPointContext.getObjectByName(Custom.class, name);
        if (existedCustom == null) {
            log.debug("It is a new custom object");
            existedCustom = new Custom();
            existedCustom.setName(name);
        }
        log.debug("Update attributes");
        existedCustom.setAttributes(getAttributes());

        log.debug("Saving current custom object to db");
        sailPointContext.saveObject(existedCustom);
        log.debug("Finish saving current custom object to db");
    }

    /**
     * Get all declared fields marked by {@link Attribute}
     *
     * @return attributes fields
     */
    private List<Field> getAttributeFields() {
        return Arrays.stream(getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(Attribute.class) != null)
                .collect(Collectors.toList());
    }
}
