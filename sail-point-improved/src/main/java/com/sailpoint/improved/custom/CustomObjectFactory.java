package com.sailpoint.improved.custom;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.tools.GeneralException;

/**
 * Common factory for creating all custom object
 */
@Slf4j
public class CustomObjectFactory {

    /**
     * Only factory static methods
     */
    private CustomObjectFactory() {

    }

    /**
     * Just create new instance of custom object
     *
     * @param type - class of current object
     * @param <T>  - type of current object
     * @return new instance of custom object
     * @throws GeneralException error creating instance of custom object
     */
    public static <T extends AbstractCustomObject> T create(Class<T> type) throws GeneralException {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            log.error("Got error while creating custom object:[{}]", type, ex);
            throw new GeneralException(ex);
        }
    }

    /**
     * Create and load custom object form current sailpoint context
     *
     * @param type - class of current object
     * @param <T>  - type of current object
     * @return instance of custom object with data from sailpoint context
     * @throws GeneralException error of getting current sailpoint context or creating/loading of custom object
     */
    public static <T extends AbstractCustomObject> T load(Class<T> type) throws GeneralException {
        return load(type, SailPointFactory.getCurrentContext());
    }

    /**
     * Create and load custom object form passed sailpoint context
     *
     * @param type             - class of current object
     * @param sailPointContext - sailpoint context source instance
     * @param <T>              - type of current object
     * @return instance of custom object with data from sailpoint context
     * @throws GeneralException error creating or loading of custom object
     */
    public static <T extends AbstractCustomObject> T load(Class<T> type, @NonNull SailPointContext sailPointContext)
            throws GeneralException {
        T customObject = create(type);
        customObject.load(sailPointContext);
        return customObject;
    }
}
