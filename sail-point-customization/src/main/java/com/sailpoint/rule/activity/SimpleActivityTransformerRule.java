package com.sailpoint.rule.activity;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.activity.ActivityTransformerRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.ApplicationActivity;
import sailpoint.object.JavaRuleContext;

/**
 * Simple implementation of {@link ActivityTransformerRule} rule
 */
@Slf4j
@Rule(value = "Simple activity transformer rule", type = sailpoint.object.Rule.Type.ActivityTransformer)
public class SimpleActivityTransformerRule extends ActivityTransformerRule {

    /**
     * Log all arguments and return null
     */
    @Override
    @Argument(name = "activity", type = ArgumentType.RETURNS, isReturnsType = true)
    protected ApplicationActivity internalExecute(JavaRuleContext context,
                                                  ActivityTransformerRule.ActivityTransformerRuleArguments arguments) {
        log.info("Current arguments:[{}]", context.getArguments());
        return null;
    }
}
