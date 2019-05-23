package com.sailpoint.rule.form;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.form.FieldValueRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

import java.util.UUID;

/**
 * Simple implementation of {@link FieldValueRule} rule
 */
@Slf4j
@Rule(value = "Simple field value rule", type = sailpoint.object.Rule.Type.FieldValue)
public class SimpleFieldValueRule extends FieldValueRule {

    /**
     * Log current identity and return random string
     */
    @Override
    @Argument(name = "value", type = ArgumentType.RETURNS, isReturnsType = true)
    protected String internalExecute(SailPointContext sailPointContext,
                                     FieldValueRule.FieldValueRuleArguments arguments) {
        log.info("Current identity:[{}]", arguments.getIdentity());
        return UUID.randomUUID().toString();
    }
}
