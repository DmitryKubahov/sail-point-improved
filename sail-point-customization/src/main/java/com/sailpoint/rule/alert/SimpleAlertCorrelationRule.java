package com.sailpoint.rule.alert;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.alert.AlertCorrelationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.SailPointObject;

/**
 * Simple implementation of {@link AlertCorrelationRule} rule
 */
@Slf4j
@Rule(value = "Simple alert correlation rule", type = sailpoint.object.Rule.Type.AlertCorrelation)
public class SimpleAlertCorrelationRule extends AlertCorrelationRule {

    /**
     * Log current source and return it
     */
    @Override
    @Argument(name = "object", type = ArgumentType.RETURNS, isReturnsType = true)
    protected SailPointObject internalExecute(JavaRuleContext context,
                                              AlertCorrelationRuleArguments arguments) {
        log.info("Current source name:[{}]", arguments.getSource().getName());
        return arguments.getSource();
    }
}
