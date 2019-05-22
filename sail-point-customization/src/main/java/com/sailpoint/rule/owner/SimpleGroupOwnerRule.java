package com.sailpoint.rule.owner;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.owner.GroupOwnerRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.Identity;
import sailpoint.tools.GeneralException;

/**
 * Simple implementation of {@link GroupOwnerRule} rule
 */
@Slf4j
@Rule(value = "Simple group owner rule", type = sailpoint.object.Rule.Type.GroupOwner)
public class SimpleGroupOwnerRule extends GroupOwnerRule {

    /**
     * Log current group and return Identity by name = 'spadmin'
     */
    @Override
    @Argument(name = "owner", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Identity internalExecute(SailPointContext sailPointContext,
                                       GroupOwnerRule.GroupOwnerRuleArguments arguments) throws GeneralException {
        log.info("Current group:[{}]", arguments.getGroup());
        return sailPointContext.getObjectByName(Identity.class, "spadmin");
    }
}
