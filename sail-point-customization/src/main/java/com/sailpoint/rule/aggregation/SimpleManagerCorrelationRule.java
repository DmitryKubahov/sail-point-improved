package com.sailpoint.rule.aggregation;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.aggregation.ManagerCorrelationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation of {@link ManagerCorrelationRule} rule
 */
@Slf4j
@Rule(value = "Simple manager correlation rule", type = sailpoint.object.Rule.Type.ManagerCorrelation)
public class SimpleManagerCorrelationRule extends ManagerCorrelationRule {

    /**
     * Put to result map "identityAttributeValue" if managerAttributeValue is not null
     */
    @Override
    @Argument(name = "map", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(JavaRuleContext context,
                                                  ManagerCorrelationRule.ManagerCorrelationRuleArguments arguments) {
        Map<String, Object> returnMap = new HashMap<>();
        if (arguments.getManagerAttributeValue() != null) {
            returnMap.put("identityAttributeValue", arguments.getManagerAttributeValue());
        }
        return returnMap;
    }
}
