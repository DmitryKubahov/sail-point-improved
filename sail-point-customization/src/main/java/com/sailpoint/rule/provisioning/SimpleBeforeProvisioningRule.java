package com.sailpoint.rule.provisioning;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.provisioning.BeforeProvisioningRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

/**
 * Simple implementation of {@link BeforeProvisioningRule} rule
 */
@Slf4j
@Rule(value = "Simple before provisioning rule", type = sailpoint.object.Rule.Type.BeforeProvisioning)
public class SimpleBeforeProvisioningRule extends BeforeProvisioningRule {

    /**
     * Log current plan and return it
     */
    @Override
    protected void internalExecuteNoneOutput(JavaRuleContext context,
                                             BeforeProvisioningRule.BeforeProvisioningRuleArguments arguments) {
        log.info("Provisioning plan:[{}]", arguments.getPlan());
    }
}
