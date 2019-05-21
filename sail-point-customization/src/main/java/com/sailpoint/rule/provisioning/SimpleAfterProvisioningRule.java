package com.sailpoint.rule.provisioning;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.provisioning.AfterProvisioningRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

/**
 * Simple implementation of {@link AfterProvisioningRule} rule
 */
@Slf4j
@Rule(value = "Simple after provisioning rule", type = sailpoint.object.Rule.Type.AfterProvisioning)
public class SimpleAfterProvisioningRule extends AfterProvisioningRule {

    /**
     * Log current result and return it
     */
    @Override
    protected Object internalExecute(SailPointContext context, AfterProvisioningRuleArguments arguments) {
        log.info("Provisioning result:[{}]", arguments.getResult());
        return arguments.getResult();
    }
}
