package com.sailpoint.rule.policy;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.policy.PolicyRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.PolicyViolation;

/**
 * Simple implementation of {@link PolicyRule} rule
 */
@Slf4j
@Rule(value = "Simple policy rule", type = sailpoint.object.Rule.Type.Policy)
public class SimplePolicyRule extends PolicyRule {

    /**
     * Log current policy, identity and constraint and return null
     */
    @Override
    @Argument(name = "violation", type = ArgumentType.RETURNS, isReturnsType = true)
    protected PolicyViolation internalExecute(JavaRuleContext context, PolicyRule.PolicyRuleArguments arguments) {
        log.info("Current identity:[{}]", arguments.getIdentity());
        log.info("Current policy:[{}]", arguments.getPolicy());
        log.info("Current constrain:[{}]", arguments.getConstraint());
        return null;
    }
}
