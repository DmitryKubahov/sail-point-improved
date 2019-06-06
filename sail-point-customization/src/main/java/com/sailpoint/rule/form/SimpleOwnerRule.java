package com.sailpoint.rule.form;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.form.OwnerRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Form;
import sailpoint.object.JavaRuleContext;

/**
 * Simple implementation of {@link OwnerRule} rule
 */
@Slf4j
@Rule(value = "Simple owner rule", type = sailpoint.object.Rule.Type.Owner)
public class SimpleOwnerRule extends OwnerRule {

    /**
     * Log current identity, role, application, template and field. Return {@link Form#OWNER_APPLICATION} value
     */
    @Override
    @Argument(name = "identity", type = ArgumentType.RETURNS, isReturnsType = true)
    protected String internalExecute(JavaRuleContext context, OwnerRuleArguments arguments) {
        log.info("Current identity:[{}]", arguments.getIdentity());
        log.info("Current role:[{}]", arguments.getRole());
        log.info("Current application:[{}]", arguments.getApplication());
        log.info("Current template:[{}]", arguments.getTemplate());
        log.info("Current field:[{}]", arguments.getField());
        return Form.OWNER_APPLICATION;
    }
}
