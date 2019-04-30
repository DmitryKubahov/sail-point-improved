package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.connector.BuildMapRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.connector.DelimitedFileConnector;

import java.util.Map;

/**
 * Simple implementation of {@link BuildMapRule} rule
 */
@Slf4j
@Rule(value = "Simple build map rule", type = sailpoint.object.Rule.Type.BuildMap)
public class SimpleBuildMapRule extends BuildMapRule {

    /**
     * Log default map with INFO level
     */
    @Override
    @Argument(name = "map", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(SailPointContext context,
                                                  BuildMapRuleArguments arguments) {
        log.debug("Try to get default map");
        Map<String, Object> map = DelimitedFileConnector.defaultBuildMap(arguments.getColumns(), arguments.getRecord());
        log.info("Default map:[{}]", map);
        return map;
    }
}
