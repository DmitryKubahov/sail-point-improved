package com.sailpoint.rule.notification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.notification.EscalationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.Identity;
import sailpoint.tools.GeneralException;

import java.util.Optional;

/**
 * Simple implementation of {@link EscalationRule} rule
 */
@Slf4j
@Rule(value = "Simple escalation rule", type = sailpoint.object.Rule.Type.Escalation)
public class SimpleEscalationRule extends EscalationRule {

    /**
     * Get current item owner and return 'spadmin'
     */
    @Override
    @Argument(name = "newOwner", type = ArgumentType.RETURNS, isReturnsType = true)
    protected String internalExecute(SailPointContext sailPointContext,
                                     EscalationRuleArguments arguments) throws GeneralException {
        Identity owner = arguments.getItem().getNotificationOwner(sailPointContext);
        log.info("Current owner:[{}]", Optional.ofNullable(owner).map(Identity::getName).orElse(null));
        return "spadmin";
    }
}
