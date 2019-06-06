package com.sailpoint.rule.unstructured;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.unstructured.TargetCreationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Target;

/**
 * Simple implementation of {@link TargetCreationRule} rule
 */
@Slf4j
@Rule(value = "Simple target creation rule", type = sailpoint.object.Rule.Type.TargetCreation)
public class SimpleTargetCreationRule extends TargetCreationRule {

    /**
     * Log current application, target and target source. Return current target.
     */
    @Override
    @Argument(name = "target", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Target internalExecute(JavaRuleContext context,
                                     TargetCreationRule.TargetCreationRuleArguments arguments) {
        log.info("Current application:[{}]", arguments.getApplication().getName());
        log.info("Current target:[{}]", arguments.getTarget().getName());
        log.info("Current target source:[{}]", arguments.getTargetSource().getName());
        return arguments.getTarget();
    }
}
