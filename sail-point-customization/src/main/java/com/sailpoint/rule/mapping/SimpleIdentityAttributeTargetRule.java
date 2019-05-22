package com.sailpoint.rule.mapping;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.mapping.IdentityAttributeTargetRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

import java.util.UUID;

/**
 * Simple implementation of {@link IdentityAttributeTargetRule} rule
 */
@Slf4j
@Rule(value = "Simple identity attribute target rule", type = sailpoint.object.Rule.Type.IdentityAttributeTarget)
public class SimpleIdentityAttributeTargetRule extends IdentityAttributeTargetRule {

    /**
     * Log current attribute value and return a random one
     */
    @Override
    @Argument(name = "attributeValue", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Object internalExecute(SailPointContext sailPointContext,
                                     IdentityAttributeTargetRule.IdentityAttributeTargetRuleArguments arguments) {
        log.info("Current attribute value:[{}]", arguments.getValue());
        return UUID.randomUUID();
    }
}
