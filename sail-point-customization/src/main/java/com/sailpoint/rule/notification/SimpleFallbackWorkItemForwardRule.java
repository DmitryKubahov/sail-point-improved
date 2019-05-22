package com.sailpoint.rule.notification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.notification.FallbackWorkItemForwardRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.Identity;
import sailpoint.tools.GeneralException;

/**
 * Simple implementation of {@link FallbackWorkItemForwardRule} rule
 */
@Slf4j
@Rule(value = "Simple fallback workitem forward rule", type = sailpoint.object.Rule.Type.FallbackWorkItemForward)
public class SimpleFallbackWorkItemForwardRule extends FallbackWorkItemForwardRule {

    /**
     * Log current owner and return 'spadmin' identity
     *
     * @throws GeneralException if no identity with 'spadmin' name in db
     */
    @Override
    @Argument(name = "newOwner", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Identity internalExecute(SailPointContext sailPointContext,
                                       FallbackWorkItemForwardRuleArguments arguments) throws GeneralException {
        log.info("Current owner:{}", arguments.getOwner());
        return sailPointContext.getObjectByName(Identity.class, "spadmin");
    }
}
