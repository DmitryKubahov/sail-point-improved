package com.sailpoint.rule;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.IdentityTriggerRule;
import lombok.extern.slf4j.Slf4j;

/**
 * Simple identity trigger rule for testing generation and execution of java rule
 */
@Slf4j
@Rule(value = "Simple IT rule", type = sailpoint.object.Rule.Type.IdentityTrigger)
public class SimpleIdentityTriggerRule extends IdentityTriggerRule {

    /**
     * Singleton instance of current rule
     */
    private static final SimpleIdentityTriggerRule INSTANCE = new SimpleIdentityTriggerRule();

    /**
     * Return singleton instance of current rule
     *
     * @return instance of current rule;
     */
    public static SimpleIdentityTriggerRule getInstance() {
        return SimpleIdentityTriggerRule.INSTANCE;
    }

    /**
     * Run for identity trigger rule
     */
    @Override
    @Argument(name = "result", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Boolean executeIdentityTriggerRule(sailpoint.api.SailPointContext sailPointContext,
                                                 IdentityTriggerRuleArguments arguments) {
        log.debug("Executing simple identity trigger rule");
        return false;
    }
}
