package com.sailpoint.improved.rule.connector;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Permission;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * The RACFPermissionCustomization rule is run during aggregation from the newer (version 7.1+)
 * RACF direct connector. It is used to customize the permissions map.
 */
@Slf4j
public abstract class RACFPermissionCustomizationRule
        extends AbstractJavaRuleExecutor<Permission, RACFPermissionCustomizationRule.RACFPermissionCustomizationRuleArguments> {

    /**
     * Name of permission argument name
     */
    public static final String ARG_PERMISSION_NAME = "permission";
    /**
     * Name of line argument name
     */
    public static final String ARG_LINE_NAME = "line";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            RACFPermissionCustomizationRule.ARG_PERMISSION_NAME,
            RACFPermissionCustomizationRule.ARG_LINE_NAME
    );

    /**
     * Default constructor
     */
    public RACFPermissionCustomizationRule() {
        super(Rule.Type.RACFPermissionCustomization.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected RACFPermissionCustomizationRule.RACFPermissionCustomizationRuleArguments buildContainerArguments(
            JavaRuleContext javaRuleContext) {
        return RACFPermissionCustomizationRuleArguments
                .builder()
                .permission((Permission) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, RACFPermissionCustomizationRule.ARG_PERMISSION_NAME))
                .line((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, RACFPermissionCustomizationRule.ARG_LINE_NAME))
                .build();
    }

    /**
     * Arguments container for {@link RACFPermissionCustomizationRule}. Contains:
     * - permission
     * - line
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class RACFPermissionCustomizationRuleArguments {
        /**
         * Permission object (map), as built from RACF record (line) directly
         */
        @Argument(name = RACFPermissionCustomizationRule.ARG_PERMISSION_NAME)
        private final Permission permission;
        /**
         * Individual record read from RACF
         */
        @Argument(name = RACFPermissionCustomizationRule.ARG_LINE_NAME)
        private final String line;
    }
}
