package sailpoint.tools.xml;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.tools.GeneralException;

import java.text.MessageFormat;

/**
 * XML object parser helper for all sailpoint objects.
 */
@Data
@Slf4j
public class XMLObjectFactoryHelper {

    /**
     * Error message if could not find serializer for certain type
     */
    private static final String SERIALIZER_NOT_FOUND_ERROR_MESSAGE = "Could not find serializer for:[{0}]";

    /**
     * Sail point object factory
     */
    private final XMLObjectFactory xmlObjectFactory;

    /**
     * Default constructor for instantiate xml object factory
     */
    public XMLObjectFactoryHelper() {
        this.xmlObjectFactory = XMLObjectFactory.getInstance();
    }

    /**
     * Parse sting value to certain type via xml
     *
     * @param type  - real type of value
     * @param value - string representation of value
     * @param <T>   - class type of real clazz
     * @return value of real type
     * @throws GeneralException error if serializer was not found
     */
    public <T> T parseStringToClazz(Class<T> type, @NonNull String value) throws GeneralException {
        log.debug("Try to find serializer for:[{}]", type);
        XMLSerializer serializer = xmlObjectFactory.getRegistry().getSerializerByRuntimeClass(type);
        if (serializer == null) {
            throw new GeneralException(MessageFormat.format(SERIALIZER_NOT_FOUND_ERROR_MESSAGE, type));
        }
        return (T) serializer.deserializeAttribute(value);
    }
}
