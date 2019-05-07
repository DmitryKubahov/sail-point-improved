package com.sailpoint.rule.aggregation;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.aggregation.ResourceObjectCustomizationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.ResourceObject;

/**
 * Simple implementation of {@link ResourceObjectCustomizationRule} rule
 */
@Slf4j
@Rule(value = "Simple resource object customization rule", type = sailpoint.object.Rule.Type.ResourceObjectCustomization)
public class SimpleResourceObjectCustomizationRule extends ResourceObjectCustomizationRule {

    /**
     * Log default object with INFO level and return it
     */
    @Override
    @Argument(name = "object", type = ArgumentType.RETURNS, isReturnsType = true)
    protected ResourceObject internalExecute(SailPointContext context,
                                             ResourceObjectCustomizationRuleArguments arguments) {
        log.debug("Get default object");
        ResourceObject object = arguments.getObject();
        log.info("Default object:[{}]", object);
        return object;
    }
}
