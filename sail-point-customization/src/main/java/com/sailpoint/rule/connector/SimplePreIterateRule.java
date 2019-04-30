package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.connector.PreIterateRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

import java.io.InputStream;

/**
 * Simple implementation of {@link PreIterateRule} rule
 */
@Slf4j
@Rule(value = "Simple pre-iterate rule", type = sailpoint.object.Rule.Type.PreIterate)
public class SimplePreIterateRule extends PreIterateRule {

    /**
     * Null. Just print stats to log by INFO level
     */
    @Override
    @Argument(name = "result", type = ArgumentType.RETURNS, isReturnsType = true)
    protected InputStream internalExecute(SailPointContext context,
                                          PreIterateRuleArguments arguments) {
        log.debug("Try to get default map");
        log.info("Stats:[{}]", arguments.getStats());
        return null;
    }
}
