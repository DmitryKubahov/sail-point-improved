package com.sailpoint.rule.provisioning;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.provisioning.JDBCProvisionRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.ProvisioningResult;

/**
 * Simple implementation of {@link JDBCProvisionRule} rule
 */
@Slf4j
@Rule(value = "Simple JDBC provisioning rule", type = sailpoint.object.Rule.Type.JDBCProvision)
public class SimpleJDBCProvisionRule extends JDBCProvisionRule {

    /**
     * Log current plan and return new provisioning result
     */
    @Override
    @Argument(name = "result", type = ArgumentType.RETURNS, isReturnsType = true)
    protected ProvisioningResult internalExecute(SailPointContext context,
                                                 JDBCProvisionRuleArguments arguments) {

        log.info("Provisioning plan:[{}]", arguments.getPlan());
        return new ProvisioningResult();
    }
}
