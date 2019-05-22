package com.sailpoint.rule.mapping;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.mapping.ListenerRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

/**
 * Simple implementation of {@link ListenerRule} rule
 */
@Slf4j
@Rule(value = "Simple listener rule", type = sailpoint.object.Rule.Type.Listener)
public class SimpleListenerRule extends ListenerRule {

    /**
     * Log new and old value
     */
    @Override
    protected void internalExecuteNoneOutput(SailPointContext sailPointContext,
                                             ListenerRuleArguments arguments) {
        log.info("Old value:[{}]", arguments.getOldValue());
        log.info("New value:[{}]", arguments.getNewValue());
    }
}
