package com.sailpoint.rule.mapping;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.mapping.IdentityAttributeRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

import java.util.UUID;

/**
 * Simple implementation of {@link IdentityAttributeRule} rule
 */
@Slf4j
@Rule(value = "Simple identity attribute rule", type = sailpoint.object.Rule.Type.IdentityAttribute)
public class SimpleIdentityAttributeRule extends IdentityAttributeRule {

    /**
     * Log current attribute definition and source. Return random string
     */
    @Override
    @Argument(name = "attributeValue", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Object internalExecute(SailPointContext sailPointContext,
                                     IdentityAttributeRule.IdentityAttributeRuleArguments arguments) {
        log.info("Current attribute definition:[{}]", arguments.getAttributeDefinition());
        log.info("Current attribute source:[{}]", arguments.getAttributeSource());
        return UUID.randomUUID().toString();
    }
}
