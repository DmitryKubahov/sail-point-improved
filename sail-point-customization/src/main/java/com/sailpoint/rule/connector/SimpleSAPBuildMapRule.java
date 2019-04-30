package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.connector.SAPBuildMapRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;

/**
 * Simple implementation of {@link SAPBuildMapRule} rule
 */
@Slf4j
@Rule(value = "Simple SAP build map rule", type = sailpoint.object.Rule.Type.SAPBuildMap)
public class SimpleSAPBuildMapRule extends SAPBuildMapRule {

    /**
     * Only for checking generating xml
     */
    @Override
    @Argument(name = "object", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Attributes internalExecute(SailPointContext context,
                                         SAPBuildMapRuleArguments arguments) {
        return arguments.getObject();
    }
}
