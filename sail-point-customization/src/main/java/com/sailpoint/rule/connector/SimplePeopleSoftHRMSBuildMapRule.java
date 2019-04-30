package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.connector.PeopleSoftHRMSBuildMapRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

import java.util.Collections;
import java.util.Map;

/**
 * Simple implementation of {@link PeopleSoftHRMSBuildMapRule} rule
 */
@Slf4j
@Rule(value = "Simple JDBC build map rule", type = sailpoint.object.Rule.Type.PeopleSoftHRMSBuildMap)
public class SimplePeopleSoftHRMSBuildMapRule extends PeopleSoftHRMSBuildMapRule {

    /**
     * Return empty map of name/values
     */
    @Override
    @Argument(name = "map", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(SailPointContext context,
                                                  PeopleSoftHRMSBuildMapRuleArguments arguments) {
        return Collections.emptyMap();
    }
}
