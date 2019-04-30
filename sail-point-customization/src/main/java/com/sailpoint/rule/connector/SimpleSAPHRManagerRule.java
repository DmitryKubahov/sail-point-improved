package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.connector.SAPHRManagerRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

/**
 * Simple implementation of {@link SAPHRManagerRule} rule
 */
@Slf4j
@Rule(value = "Simple SAP hr manager rule", type = sailpoint.object.Rule.Type.SAPHRManagerRule)
public class SimpleSAPHRManagerRule extends SAPHRManagerRule {

    /**
     * return null as id of the manager’s identity
     */
    @Override
    @Argument(name = "supervisor", type = ArgumentType.RETURNS, isReturnsType = true)
    protected String internalExecute(SailPointContext context, SAPHRManagerRuleArguments arguments) {
        return null;
    }
}
