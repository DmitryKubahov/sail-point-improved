package com.sailpoint.rule.logical;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.logical.CompositeAccountRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;

import java.util.Collections;
import java.util.List;

/**
 * Simple implementation of {@link CompositeAccountRule} rule
 */
@Slf4j
@Rule(value = "Simple composite account rule", type = sailpoint.object.Rule.Type.CompositeAccount)
public class SimpleCompositeAccountRule extends CompositeAccountRule {

    /**
     * Log current identity and application and return empty list
     */
    @Override
    @Argument(name = "newOwner", type = ArgumentType.RETURNS, isReturnsType = true)
    protected List<Link> internalExecute(JavaRuleContext context,
                                         CompositeAccountRule.CompositeAccountRuleArguments arguments) {
        log.info("Current identity name:[{}]", arguments.getIdentity().getName());
        log.info("Current application name:[{}]", arguments.getApplication().getName());
        return Collections.emptyList();
    }
}
