package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.connector.MergeMapsRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

import java.util.Map;

/**
 * Simple implementation of {@link MergeMapsRule} rule
 */
@Slf4j
@Rule(value = "Simple merge maps rule", type = sailpoint.object.Rule.Type.MergeMaps)
public class SimpleMergeMapsRule extends MergeMapsRule {

    /**
     * Log current map and merged attributes with INFO level
     */
    @Override
    @Argument(name = "merged", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(SailPointContext context, MergeMapsRuleArguments arguments) {
        log.info("Current:[{}]", arguments.getCurrent());
        log.info("Merged attributes:[{}]", arguments.getMergeAttrs());
        return arguments.getCurrent();
    }
}
