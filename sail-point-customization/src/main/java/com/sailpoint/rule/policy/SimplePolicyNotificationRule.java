package com.sailpoint.rule.policy;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.policy.PolicyNotificationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.GeneralException;

import java.util.Collections;
import java.util.List;

/**
 * Simple implementation of {@link PolicyNotificationRule} rule
 */
@Slf4j
@Rule(value = "Simple policy notification rule", type = sailpoint.object.Rule.Type.PolicyNotification)
public class SimplePolicyNotificationRule extends PolicyNotificationRule {

    /**
     * Log current policy and return list with one "spadmin" identity
     *
     * @throws GeneralException error if "spadmin" not found
     */
    @Override
    @Argument(name = "listener", type = ArgumentType.RETURNS, isReturnsType = true)
    protected List<Identity> internalExecute(JavaRuleContext context,
                                             PolicyNotificationRule.PolicyNotificationRuleArguments arguments)
            throws GeneralException {
        log.info("Current policy:[{}]", arguments.getPolicy());
        return Collections.singletonList(context.getContext().getObjectByName(Identity.class, "spadmin"));
    }
}
