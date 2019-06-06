package com.sailpoint.rule.alert;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.alert.AlertCreationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Alert;
import sailpoint.object.JavaRuleContext;

/**
 * Simple implementation of {@link AlertCreationRule} rule
 */
@Slf4j
@Rule(value = "Simple alert creation rule", type = sailpoint.object.Rule.Type.AlertCreation)
public class SimpleAlertCreationRule extends AlertCreationRule {

    /**
     * Log current application and return current alert
     */
    @Override
    @Argument(name = "alert", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Alert internalExecute(JavaRuleContext context,
                                    AlertCreationRule.AlertCreationRuleArguments arguments) {
        log.info("Current application name:[{}]", arguments.getApplication().getName());
        return arguments.getAlert();
    }
}
