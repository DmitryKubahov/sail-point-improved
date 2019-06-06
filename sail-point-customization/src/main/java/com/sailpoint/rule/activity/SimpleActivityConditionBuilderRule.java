package com.sailpoint.rule.activity;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.activity.ActivityConditionBuilderRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

/**
 * Simple implementation of {@link ActivityConditionBuilderRule} rule
 */
@Slf4j
@Rule(value = "Simple activity condition builder rule", type = sailpoint.object.Rule.Type.ActivityConditionBuilder)
public class SimpleActivityConditionBuilderRule extends ActivityConditionBuilderRule {

    /**
     * Log current config and return null
     */
    @Override
    @Argument(name = "condition", type = ArgumentType.RETURNS, isReturnsType = true)
    protected String internalExecute(JavaRuleContext context,
                                     ActivityConditionBuilderRuleArguments arguments) {
        log.info("Current config:[{}]", arguments.getConfig());
        return null;
    }
}
