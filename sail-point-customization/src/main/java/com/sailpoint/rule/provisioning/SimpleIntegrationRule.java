package com.sailpoint.rule.provisioning;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.provisioning.IntegrationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.ProvisioningResult;

/**
 * Simple implementation of {@link IntegrationRule} rule
 */
@Slf4j
@Rule(value = "Simple integration rule", type = sailpoint.object.Rule.Type.Integration)
public class SimpleIntegrationRule extends IntegrationRule {

    /**
     * Log current plan and return new provisioning result
     */
    @Override
    @Argument(name = "result", type = ArgumentType.RETURNS, isReturnsType = true)
    protected ProvisioningResult internalExecute(SailPointContext context, IntegrationRuleArguments arguments) {
        log.info("Provisioning plan:[{}]", arguments.getPlan());
        return new ProvisioningResult();
    }
}
