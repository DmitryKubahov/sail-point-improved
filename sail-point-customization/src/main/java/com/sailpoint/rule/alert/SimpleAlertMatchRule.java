package com.sailpoint.rule.alert;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.alert.AlertMatchRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

import java.util.Random;

/**
 * Simple implementation of {@link AlertMatchRule} rule
 */
@Slf4j
@Rule(value = "Simple alert match rule", type = sailpoint.object.Rule.Type.AlertMatch)
public class SimpleAlertMatchRule extends AlertMatchRule {

    /**
     * Log current current alert and return random boolean
     */
    @Override
    @Argument(name = "status", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Boolean internalExecute(JavaRuleContext context,
                                      AlertMatchRuleArguments arguments) {
        log.info("Current alert:[{}]", arguments.getAlert().getName());
        return new Random().nextBoolean();
    }
}
