package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.certification.IdentityTriggerRule;
import lombok.extern.slf4j.Slf4j;

/**
 * Simple identity trigger rule for testing generation and execution of java rule
 */
@Slf4j
@Rule(value = "Simple IT rule", type = sailpoint.object.Rule.Type.IdentityTrigger)
public class SimpleIdentityTriggerRule extends IdentityTriggerRule {

    /**
     * Run for identity trigger rule
     */
    @Override
    @Argument(name = "result", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Boolean internalExecute(sailpoint.api.SailPointContext sailPointContext,
                                  IdentityTriggerRuleArguments arguments) {
        log.debug("Executing simple identity trigger rule");
        return false;
    }
}
