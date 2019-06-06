package com.sailpoint.rule.policy;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.policy.PolicyOwnerRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.GeneralException;


/**
 * Simple implementation of {@link PolicyOwnerRule} rule
 */
@Slf4j
@Rule(value = "Simple policy owner rule", type = sailpoint.object.Rule.Type.PolicyOwner)
public class SimplePolicyOwnerRule extends PolicyOwnerRule {

    /**
     * Log current policy and return identity for 'spadmin' name
     *
     * @throws GeneralException error if "spadmin" not found
     */
    @Override
    @Argument(name = "owner", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Identity internalExecute(JavaRuleContext context, PolicyOwnerRuleArguments arguments)
            throws GeneralException {
        log.info("Current policy:[{}]", arguments.getPolicy());
        return context.getContext().getObjectByName(Identity.class, "spadmin");
    }
}
