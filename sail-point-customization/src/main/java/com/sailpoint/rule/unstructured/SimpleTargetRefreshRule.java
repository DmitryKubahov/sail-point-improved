package com.sailpoint.rule.unstructured;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.unstructured.TargetRefreshRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Target;

/**
 * Simple implementation of {@link TargetRefreshRule} rule
 */
@Slf4j
@Rule(value = "Simple target refresh rule", type = sailpoint.object.Rule.Type.TargetRefresh)
public class SimpleTargetRefreshRule extends TargetRefreshRule {

    /**
     * Log current application, target and target source. Return current target.
     */
    @Override
    @Argument(name = "target", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Target internalExecute(JavaRuleContext context,
                                     TargetRefreshRuleArguments arguments) {
        log.info("Current application:[{}]", arguments.getApplication().getName());
        log.info("Current target:[{}]", arguments.getTarget().getName());
        log.info("Current target source:[{}]", arguments.getTargetSource().getName());
        return arguments.getTarget();
    }
}
