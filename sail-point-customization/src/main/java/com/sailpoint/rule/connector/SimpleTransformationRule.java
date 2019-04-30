package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.connector.TransformationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.connector.AbstractConnector;
import sailpoint.object.ResourceObject;

/**
 * Simple implementation of {@link TransformationRule} rule
 */
@Slf4j
@Rule(value = "Simple transformation rule", type = sailpoint.object.Rule.Type.Transformation)
public class SimpleTransformationRule extends TransformationRule {

    /**
     * Get default resource object, log it with INFO level and return it
     */
    @Override
    @Argument(name = "resourceObject", type = ArgumentType.RETURNS, isReturnsType = true)
    protected ResourceObject internalExecute(SailPointContext context,
                                             TransformationRuleArguments arguments) {
        log.debug("Try to get default resource object");
        ResourceObject resourceObject = AbstractConnector
                .defaultTransformObject(arguments.getSchema(), arguments.getObject());
        log.info("Default resource object:[{}]", resourceObject);
        return resourceObject;
    }
}
