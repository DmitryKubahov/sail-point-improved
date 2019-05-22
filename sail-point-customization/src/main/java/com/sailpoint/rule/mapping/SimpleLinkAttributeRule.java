package com.sailpoint.rule.mapping;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.mapping.LinkAttributeRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

import java.util.UUID;

/**
 * Simple implementation of {@link LinkAttributeRule} rule
 */
@Slf4j
@Rule(value = "Simple link attribute rule", type = sailpoint.object.Rule.Type.LinkAttribute)
public class SimpleLinkAttributeRule extends LinkAttributeRule {

    /**
     * Log current link and return random string
     */
    @Override
    @Argument(name = "value", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Object internalExecute(SailPointContext sailPointContext,
                                     LinkAttributeRule.LinkAttributeRuleArguments arguments) {
        log.info("Current link:[{}]", arguments.getLink());
        return UUID.randomUUID().toString();
    }
}
