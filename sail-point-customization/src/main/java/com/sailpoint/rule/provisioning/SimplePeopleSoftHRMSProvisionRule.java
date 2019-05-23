package com.sailpoint.rule.provisioning;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.provisioning.PeopleSoftHRMSProvisionRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ProvisioningResult;

/**
 * Simple implementation of {@link PeopleSoftHRMSProvisionRule} rule
 */
@Slf4j
@Rule(value = "Simple people soft HRMS provision rule", type = sailpoint.object.Rule.Type.PeopleSoftHRMSProvision)
public class SimplePeopleSoftHRMSProvisionRule extends PeopleSoftHRMSProvisionRule {

    /**
     * Log current plan and return new provisioning result
     */
    @Override
    @Argument(name = "result", type = ArgumentType.RETURNS, isReturnsType = true)
    protected ProvisioningResult internalExecute(JavaRuleContext context,
                                                 PeopleSoftHRMSProvisionRuleArguments arguments) {

        log.info("Provisioning plan:[{}]", arguments.getPlan());
        return new ProvisioningResult();
    }
}
