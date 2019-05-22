package com.sailpoint.rule.aggregation;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.aggregation.IdentityCreationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.Identity;

/**
 * Simple implementation of {@link IdentityCreationRule} rule
 */
@Slf4j
@Rule(value = "Simple identity creation rule", type = sailpoint.object.Rule.Type.IdentityCreation)
public class SimpleIdentityCreationRule extends IdentityCreationRule {

    /**
     * Log {@link Identity#ATT_LASTNAME} and {@link Identity#ATT_FIRSTNAME} by INFO level
     */
    @Override
    protected void internalExecuteNoneOutput(SailPointContext context,
                                             IdentityCreationRuleArguments arguments) {
        log.info("First name:[{}]", arguments.getIdentity().getFirstname());
        log.info("Last name:[{}]", arguments.getIdentity().getLastname());
    }
}
