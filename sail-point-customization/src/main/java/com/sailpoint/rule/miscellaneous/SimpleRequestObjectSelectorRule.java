package com.sailpoint.rule.miscellaneous;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.miscellaneous.RequestObjectSelectorRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.QueryInfo;

/**
 * Simple implementation of {@link RequestObjectSelectorRule} rule
 */
@Slf4j
@Rule(value = "Simple request object selector rule", type = sailpoint.object.Rule.Type.RequestObjectSelector)
public class SimpleRequestObjectSelectorRule extends RequestObjectSelectorRule {

    /**
     * Log current requestor and requestee. Return null.
     */
    @Override
    @Argument(name = "filter", type = ArgumentType.RETURNS, isReturnsType = true)
    protected QueryInfo internalExecute(JavaRuleContext context,
                                        RequestObjectSelectorRuleArguments arguments) {
        log.info("Current requestor:[{}]", arguments.getRequestor().getName());
        log.info("Current requestee:[{}]", arguments.getRequestee().getName());
        return null;
    }
}
