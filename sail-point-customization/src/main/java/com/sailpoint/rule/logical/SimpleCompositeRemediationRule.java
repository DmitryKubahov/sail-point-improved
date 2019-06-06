package com.sailpoint.rule.logical;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.logical.CompositeRemediationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ProvisioningPlan;

/**
 * Simple implementation of {@link CompositeRemediationRule} rule
 */
@Slf4j
@Rule(value = "Simple composite remediation rule", type = sailpoint.object.Rule.Type.CompositeRemediation)
public class SimpleCompositeRemediationRule extends CompositeRemediationRule {

    /**
     * Log current identity, provisioning plan and application and return current provisioning plan
     */
    @Override
    @Argument(name = "provisionPlan", type = ArgumentType.RETURNS, isReturnsType = true)
    protected ProvisioningPlan internalExecute(JavaRuleContext context,
                                               CompositeRemediationRuleArguments arguments) {
        log.info("Current identity name:[{}]", arguments.getIdentity().getName());
        log.info("Current provisioning plan:[{}]", arguments.getPlan());
        log.info("Current application name:[{}]", arguments.getApplication().getName());
        return arguments.getPlan();
    }
}
