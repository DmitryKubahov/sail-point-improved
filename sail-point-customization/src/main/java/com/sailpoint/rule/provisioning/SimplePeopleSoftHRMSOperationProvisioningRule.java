package com.sailpoint.rule.provisioning;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.provisioning.PeopleSoftHRMSOperationProvisioningRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ProvisioningResult;

/**
 * Simple implementation of {@link PeopleSoftHRMSOperationProvisioningRule} rule
 */
@Slf4j
@Rule(value = "Simple people soft HRMS operation provisioning rule", type = sailpoint.object.Rule.Type.PeopleSoftHRMSOperationProvisioning)
public class SimplePeopleSoftHRMSOperationProvisioningRule extends PeopleSoftHRMSOperationProvisioningRule {

    /**
     * Log current plan and return new provisioning result
     */
    @Override
    @Argument(name = "result", type = ArgumentType.RETURNS, isReturnsType = true)
    protected ProvisioningResult internalExecute(JavaRuleContext context,
                                                 PeopleSoftHRMSOperationProvisioningRuleArguments arguments) {

        log.info("Provisioning plan:[{}]", arguments.getPlan());
        return new ProvisioningResult();
    }
}
