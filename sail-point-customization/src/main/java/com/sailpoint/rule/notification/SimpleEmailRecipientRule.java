package com.sailpoint.rule.notification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.notification.EmailRecipientRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

import java.util.Collections;
import java.util.List;

/**
 * Simple implementation of {@link EmailRecipientRule} rule
 */
@Slf4j
@Rule(value = "Simple email recipient rule", type = sailpoint.object.Rule.Type.EmailRecipient)
public class SimpleEmailRecipientRule extends EmailRecipientRule {

    /**
     * Log current item and return singleton list with 'spadmin' value
     */
    @Override
    @Argument(name = "result", type = ArgumentType.RETURNS, isReturnsType = true)
    protected List<String> internalExecute(SailPointContext sailPointContext,
                                           EmailRecipientRule.EmailRecipientRuleArguments arguments) {
        log.info("Report parameter value:[{}]", arguments.getItem());
        return Collections.singletonList("spadmin");
    }
}
