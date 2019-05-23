package com.sailpoint.rule.form;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.form.ValidationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.Message;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Simple implementation of {@link ValidationRule} rule
 */
@Slf4j
@Rule(value = "Simple validation rule", type = sailpoint.object.Rule.Type.Validation)
public class SimpleValidationRule extends ValidationRule {

    /**
     * Log current identity, application, form, field and value. Return list with one random message
     */
    @Override
    @Argument(name = "value", type = ArgumentType.RETURNS, isReturnsType = true)
    protected List<Message> internalExecute(JavaRuleContext context, ValidationRuleArguments arguments) {
        log.info("Current identity:[{}]", arguments.getIdentity());
        log.info("Current application:[{}]", arguments.getApplication());
        log.info("Current form:[{}]", arguments.getForm());
        log.info("Current field:[{}]", arguments.getField());
        log.info("Current value:[{}]", arguments.getValue());
        return Collections.singletonList(Message.info(UUID.randomUUID().toString()));
    }
}
