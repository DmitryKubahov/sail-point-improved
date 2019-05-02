package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.connector.RACFPermissionCustomizationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.Permission;

/**
 * Simple implementation of {@link RACFPermissionCustomizationRule} rule
 */
@Slf4j
@Rule(value = "Simple racf permission customization rule", type = sailpoint.object.Rule.Type.RACFPermissionCustomization)
public class SimpleRACFPermissionCustomizationRule extends RACFPermissionCustomizationRule {

    /**
     * Just log current permission by INFO and return it
     */
    @Override
    @Argument(name = "permission", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Permission internalExecute(SailPointContext context,
                                         RACFPermissionCustomizationRuleArguments arguments) {
        log.info("Current permission:[{}]", arguments.getPermission());
        return arguments.getPermission();
    }
}
