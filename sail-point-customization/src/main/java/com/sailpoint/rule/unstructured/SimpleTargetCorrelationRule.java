package com.sailpoint.rule.unstructured;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.unstructured.TargetCorrelationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.Correlator;
import sailpoint.object.JavaRuleContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Simple implementation of {@link TargetCorrelationRule} rule
 */
@Slf4j
@Rule(value = "Simple target correlation rule", type = sailpoint.object.Rule.Type.TargetCorrelation)
public class SimpleTargetCorrelationRule extends TargetCorrelationRule {

    /**
     * Log current application, native id, target and target source. Return new random name and value link attribute
     */
    @Override
    @Argument(name = "target", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(JavaRuleContext context,
                                                  TargetCorrelationRuleArguments arguments) {
        log.info("Current application:[{}]", arguments.getApplication().getName());
        log.info("Current native id:[{}]", arguments.getNativeId());
        log.info("Current target:[{}]", arguments.getTarget().getName());
        log.info("Current target source:[{}]", arguments.getTargetSource().getName());
        Map<String, Object> result = new HashMap<>();
        result.put(Correlator.RULE_RETURN_LINK_ATTRIBUTE, UUID.randomUUID().toString());
        result.put(Correlator.RULE_RETURN_LINK_ATTRIBUTE_VALUE, UUID.randomUUID().toString());
        return result;
    }
}
