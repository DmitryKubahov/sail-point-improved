package com.sailpoint.rule.logical;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.logical.CompositeTierCorrelationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;

import java.util.Collections;
import java.util.List;

/**
 * Simple implementation of {@link CompositeTierCorrelationRule} rule
 */
@Slf4j
@Rule(value = "Simple composite tier correlation rule", type = sailpoint.object.Rule.Type.CompositeTierCorrelation)
public class SimpleCompositeTierCorrelationRule extends CompositeTierCorrelationRule {

    /**
     * Log current identity, tier application and primary link. Return empty list
     */
    @Override
    @Argument(name = "links", type = ArgumentType.RETURNS, isReturnsType = true)
    protected List<Link> internalExecute(JavaRuleContext context,
                                         CompositeTierCorrelationRuleArguments arguments) {
        log.info("Current identity name:[{}]", arguments.getIdentity().getName());
        log.info("Current tier application name:[{}]", arguments.getTierApplication().getName());
        log.info("Current private link:[{}]", arguments.getPrimaryLink().getName());
        return Collections.emptyList();
    }
}
