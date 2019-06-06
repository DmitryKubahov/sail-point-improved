package com.sailpoint.rule.notification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.notification.WorkItemForwardRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.GeneralException;

/**
 * Simple implementation of {@link WorkItemForwardRule} rule
 */
@Slf4j
@Rule(value = "Simple workitem forward rule", type = sailpoint.object.Rule.Type.WorkItemForward)
public class SimpleWorkItemForwardRule extends WorkItemForwardRule {

    /**
     * Log current owner and return 'spadmin' identity
     *
     * @throws GeneralException if no identity with 'spadmin' name in db
     */
    @Override
    @Argument(name = "newOwner", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Identity internalExecute(JavaRuleContext context,
                                       WorkItemForwardRuleArguments arguments) throws GeneralException {
        log.info("Current owner:{}", arguments.getOwner());
        return context.getContext().getObjectByName(Identity.class, "spadmin");
    }
}
