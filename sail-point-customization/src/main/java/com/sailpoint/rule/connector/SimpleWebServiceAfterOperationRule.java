package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.connector.WebServiceAfterOperationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

import java.util.Collections;
import java.util.Map;

/**
 * Simple implementation of {@link WebServiceAfterOperationRule} rule
 */
@Slf4j
@Rule(value = "Simple web service after operation rule", type = sailpoint.object.Rule.Type.WebServiceAfterOperationRule)
public class SimpleWebServiceAfterOperationRule extends WebServiceAfterOperationRule {

    /**
     * Just return empty map
     */
    @Override
    @Argument(name = "updatedAccountOrGroupList", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(SailPointContext context,
                                                  WebServiceAfterOperationRuleArguments arguments) {
        return Collections.emptyMap();
    }
}
