package com.sailpoint.rule.aggregation;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.aggregation.GroupAggregationRefreshRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

import java.util.Collections;
import java.util.Map;

/**
 * Simple implementation of {@link GroupAggregationRefreshRule} rule
 */
@Slf4j
@Rule(value = "Simple group aggregation refresh rule", type = sailpoint.object.Rule.Type.GroupAggregationRefresh)
public class SimpleGroupAggregationRefreshRule extends GroupAggregationRefreshRule {

    /**
     * Log object by INFO and return  empty map
     */
    @Override
    @Argument(name = "map", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(SailPointContext context,
                                                  GroupAggregationRefreshRuleArguments arguments) {
        log.info("Object:[{}]", arguments.getObject());
        return Collections.emptyMap();
    }
}
