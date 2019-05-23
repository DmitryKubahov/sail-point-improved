package com.sailpoint.rule.aggregation;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.aggregation.RefreshRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

/**
 * Simple implementation of {@link RefreshRule} rule
 */
@Slf4j
@Rule(value = "Simple refresh rule", type = sailpoint.object.Rule.Type.Refresh)
public class SimpleRefreshRule extends RefreshRule {

    /**
     * Log identity display name by INFO log
     */
    @Override
    protected void internalExecuteNoneOutput(JavaRuleContext context, RefreshRuleArguments arguments) {
        log.info("Identity display name:[{}]", arguments.getIdentity().getDisplayName());
    }
}
