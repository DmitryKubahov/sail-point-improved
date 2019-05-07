package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.connector.PostIterateRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

/**
 * Simple implementation of {@link PostIterateRule} rule
 */
@Slf4j
@Rule(value = "Simple post-iterate rule", type = sailpoint.object.Rule.Type.PostIterate)
public class SimplePostIterateRule extends PostIterateRule {

    /**
     * Null. Just print stats to log by INFO level
     */
    @Override
    protected Object internalExecute(SailPointContext context,
                                     PostIterateRuleArguments arguments) {
        log.debug("Try to get default map");
        log.info("Stats:[{}]", arguments.getStats());
        return null;
    }
}
