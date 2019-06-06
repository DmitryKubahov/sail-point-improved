package com.sailpoint.rule.activity;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.activity.ActivityCorrelationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;

/**
 * Simple implementation of {@link ActivityCorrelationRule} rule
 */
@Slf4j
@Rule(value = "Simple activity correlation rule", type = sailpoint.object.Rule.Type.ActivityCorrelation)
public class SimpleActivityCorrelationRule extends ActivityCorrelationRule {

    /**
     * Log current application, datasource and activity. Return null
     */
    @Override
    @Argument(name = "link", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Link internalExecute(JavaRuleContext context,
                                   ActivityCorrelationRuleArguments arguments) {
        log.info("Current application name:[{}]", arguments.getApplication().getName());
        log.info("Current datasource name:[{}]", arguments.getDataSource().getName());
        log.info("Current activity name:[{}]", arguments.getActivity().getName());
        return null;
    }
}
