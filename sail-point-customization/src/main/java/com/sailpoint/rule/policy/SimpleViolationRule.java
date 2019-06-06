package com.sailpoint.rule.policy;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.policy.ViolationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.PolicyViolation;

/**
 * Simple implementation of {@link ViolationRule} rule
 */
@Slf4j
@Rule(value = "Simple violation rule", type = sailpoint.object.Rule.Type.Violation)
public class SimpleViolationRule extends ViolationRule {

    /**
     * Log current policy, identity and constraint and return the same policy violation
     */
    @Override
    @Argument(name = "violation", type = ArgumentType.RETURNS, isReturnsType = true)
    protected PolicyViolation internalExecute(JavaRuleContext context, ViolationRuleArguments arguments) {
        log.info("Current identity:[{}]", arguments.getIdentity());
        log.info("Current policy:[{}]", arguments.getViolation());
        log.info("Current constrain:[{}]", arguments.getConstraint());
        return arguments.getViolation();
    }
}
