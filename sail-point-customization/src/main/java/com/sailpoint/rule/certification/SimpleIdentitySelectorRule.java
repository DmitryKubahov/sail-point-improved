package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.certification.IdentitySelectorRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

/**
 * Simple implementation of {@link IdentitySelectorRule} rule
 */
@Slf4j
@Rule(value = "Simple identity selector rule", type = sailpoint.object.Rule.Type.IdentitySelector)
public class SimpleIdentitySelectorRule extends IdentitySelectorRule {

    /**
     * Log current identity and return true
     */
    @Override
    @Argument(name = "success", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Boolean internalExecute(JavaRuleContext context,
                                      IdentitySelectorRuleArguments arguments) {

        log.info("Identity:[{}]", arguments.getIdentity());
        return Boolean.TRUE;
    }
}
