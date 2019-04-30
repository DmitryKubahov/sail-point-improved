package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.connector.JDBCBuildMapRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.connector.JDBCConnector;
import sailpoint.tools.GeneralException;

import java.util.Map;

/**
 * Simple implementation of {@link JDBCBuildMapRule} rule
 */
@Slf4j
@Rule(value = "Simple JDBC build map rule", type = sailpoint.object.Rule.Type.JDBCBuildMap)
public class SimpleJDBCBuildMapRule extends JDBCBuildMapRule {

    /**
     * Active status attribute name
     */
    static final String ATTR_ACTIVE_STATUS_NAME = "active_status";

    /**
     * Active status true value
     */
    static final String TRUE_ACTIVE_STATUS_VALUE = "T";

    /**
     * Check default string attribute {@link SimpleJDBCBuildMapRule#ATTR_ACTIVE_STATUS_NAME} and replace it with boolean value:
     * Y - true
     * all other values - false
     */
    @Override
    @Argument(name = "map", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(SailPointContext context,
                                              JDBCBuildMapRuleArguments arguments) throws GeneralException {
        try {
            log.debug("Try to get default map");
            Map<String, Object> map = JDBCConnector.buildMapFromResultSet(arguments.getResult());

            log.debug("Check:[{}] attribute in default map", ATTR_ACTIVE_STATUS_NAME);
            log.trace("Default map:[{}]", map);
            String activeStatus = (String) map.get(ATTR_ACTIVE_STATUS_NAME);
            log.trace("[{}] attribute value in default map:[{}]", ATTR_ACTIVE_STATUS_NAME, activeStatus);
            map.put(ATTR_ACTIVE_STATUS_NAME, TRUE_ACTIVE_STATUS_VALUE.equalsIgnoreCase(activeStatus));
            return map;
        } catch (Exception ex) {
            log.error("Got error:[{}] while executing rule", ex.getMessage(), ex);
            throw new GeneralException(ex);
        }
    }
}
