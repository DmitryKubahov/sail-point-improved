package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.connector.FileParsingRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

import java.util.Collections;
import java.util.Map;

/**
 * Simple implementation of {@link FileParsingRule} rule
 */
@Slf4j
@Rule(value = "Simple file parsing rule", type = sailpoint.object.Rule.Type.FileParsingRule)
public class SimpleFileParsingRule extends FileParsingRule {

    /**
     * return empty map
     */
    @Override
    @Argument(name = "map", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(JavaRuleContext context, FileParsingRuleArguments arguments) {
        return Collections.emptyMap();
    }
}
