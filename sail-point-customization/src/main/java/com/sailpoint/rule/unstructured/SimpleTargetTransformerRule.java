package com.sailpoint.rule.unstructured;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.unstructured.TargetTransformerRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Target;

/**
 * Simple implementation of {@link TargetTransformerRule} rule
 */
@Slf4j
@Rule(value = "Simple target transformer rule", type = sailpoint.object.Rule.Type.TargetTransformer)
public class SimpleTargetTransformerRule extends TargetTransformerRule {

    /**
     * Log current application, target and target source. Return current target.
     */
    @Override
    @Argument(name = "target", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Target internalExecute(JavaRuleContext context,
                                     TargetTransformerRuleArguments arguments) {
        log.info("Current application:[{}]", arguments.getApplication().getName());
        log.info("Current target:[{}]", arguments.getTarget().getName());
        log.info("Current target source:[{}]", arguments.getTargetSource().getName());
        return arguments.getTarget();
    }
}
