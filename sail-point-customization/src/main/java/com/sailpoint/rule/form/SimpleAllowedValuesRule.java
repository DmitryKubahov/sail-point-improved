package com.sailpoint.rule.form;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.form.AllowedValuesRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

import java.util.UUID;

/**
 * Simple implementation of {@link AllowedValuesRule} rule
 */
@Slf4j
@Rule(value = "Simple allowed values rule", type = sailpoint.object.Rule.Type.AllowedValues)
public class SimpleAllowedValuesRule extends AllowedValuesRule {

    /**
     * Log current identity, form and field. Return list with random string
     */
    @Override
    @Argument(name = "value", type = ArgumentType.RETURNS, isReturnsType = true)
    protected String internalExecute(JavaRuleContext context, AllowedValuesRuleArguments arguments) {
        log.info("Current identity:[{}]", arguments.getIdentity());
        log.info("Current form:[{}]", arguments.getForm());
        log.info("Current field:[{}]", arguments.getField());
        return UUID.randomUUID().toString();
    }
}
