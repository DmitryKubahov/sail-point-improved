package com.sailpoint.improved.rule;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.JavaRuleExecutor;

import java.util.Optional;

/**
 * Abstract class for java rule executor,
 */
@Slf4j
public abstract class AbstractJavaRuleExecutor implements JavaRuleExecutor {

    /**
     * Get attribute value by name
     *
     * @param javaRuleContext - java rule context
     * @param attributeName   - attribute name
     * @return attribute value. Can be null.
     */
    protected Object getAttributeByName(@NonNull JavaRuleContext javaRuleContext, @NonNull String attributeName) {
        log.debug("Start getting attribute:[{}] value from java rule context", attributeName);
        return Optional.ofNullable(javaRuleContext.getArguments()).map(attribute -> attribute.get(attributeName))
                .orElse(null);
    }
}
