package com.sailpoint.rule.activity;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.activity.ActivityPositionBuilderRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

import java.util.Collections;
import java.util.Map;

/**
 * Simple implementation of {@link ActivityPositionBuilderRule} rule
 */
@Slf4j
@Rule(value = "Simple activity position builder rule", type = sailpoint.object.Rule.Type.ActivityPositionBuilder)
public class SimpleActivityPositionBuilderRule extends ActivityPositionBuilderRule {

    /**
     * Log current result set position and return empty map.
     */
    @Override
    @Argument(name = "map", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(JavaRuleContext context,
                                                  ActivityPositionBuilderRuleArguments arguments) {
        log.info("Current row:[{}]", arguments.getRow());
        return Collections.emptyMap();
    }
}
