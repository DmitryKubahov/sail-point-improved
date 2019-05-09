package com.sailpoint.rule.provisioning;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.provisioning.SapHrProvisionRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.ProvisioningResult;

/**
 * Simple implementation of {@link SapHrProvisionRule} rule
 */
@Slf4j
@Rule(value = "Simple SAP HR provision rule", type = sailpoint.object.Rule.Type.SapHrProvision)
public class SimpleSapHrProvisionRule extends SapHrProvisionRule {

    /**
     * Log current plan and return new provisioning result
     */
    @Override
    @Argument(name = "result", type = ArgumentType.RETURNS, isReturnsType = true)
    protected ProvisioningResult internalExecute(SailPointContext context,
                                                 SapHrProvisionRuleArguments arguments) {

        log.info("Provisioning plan:[{}]", arguments.getPlan());
        return new ProvisioningResult();
    }
}
