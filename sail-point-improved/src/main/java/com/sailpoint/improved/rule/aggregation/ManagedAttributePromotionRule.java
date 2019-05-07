package com.sailpoint.improved.rule.aggregation;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ManagedAttribute;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * For an application is run by an aggregation task or an identity refresh
 * task for which the “Promote managed attributes” option selected. This rule can set values on managed
 * attributes as they are promoted for the first time, and it only runs when a managed attribute is initially created
 * through promotion (i.e. on create, not on update). The rule directly modifies the ManagedAttribute passed to it,
 * so it does not have a return value.
 * <p>
 * This rule type was renamed in IdentityIQ version 6.2 to ManagedAttributePromotion. The new name is more
 * descriptive of when this rule is run and how it should be used.
 * <p>
 * The ManagedAttributeCustomization/Promotion Rule runs during a refresh process that occurs either in an
 * identity refresh task or at the end of an account aggregation task. Managed attributes are promoted after
 * identity attributes are refreshed (including manager correlation) but before any Refresh rule specified for the
 * task is run.
 * <p>
 * Outputs: None; the ManagedAttribute object passed as parameter to the rule should be edited directly by the
 * rule.
 */
@Slf4j
public abstract class ManagedAttributePromotionRule
        extends AbstractJavaRuleExecutor<Object, ManagedAttributePromotionRule.ManagedAttributePromotionRuleArguments> {

    /**
     * Name of attribute argument name
     */
    public static final String ARG_ATTRIBUTE_NAME = "attribute";
    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION_NAME = "application";
    /**
     * Name of state argument name
     */
    public static final String ARG_STATE_NAME = "state";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ManagedAttributePromotionRule.ARG_ATTRIBUTE_NAME,
            ManagedAttributePromotionRule.ARG_APPLICATION_NAME,
            ManagedAttributePromotionRule.ARG_STATE_NAME
    );

    /**
     * Default constructor
     */
    public ManagedAttributePromotionRule() {
        super(Rule.Type.ManagedAttributePromotion.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ManagedAttributePromotionRule.ManagedAttributePromotionRuleArguments buildContainerArguments(
            JavaRuleContext javaRuleContext) {
        return ManagedAttributePromotionRuleArguments
                .builder()
                .attribute((ManagedAttribute) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, ManagedAttributePromotionRule.ARG_ATTRIBUTE_NAME))
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, ManagedAttributePromotionRule.ARG_APPLICATION_NAME))
                .state((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, ManagedAttributePromotionRule.ARG_STATE_NAME))
                .build();
    }

    /**
     * Arguments container for {@link ManagedAttributePromotionRule}. Contains:
     * - attribute
     * - application
     * - state
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ManagedAttributePromotionRuleArguments {
        /**
         * A reference to the managed attribute being created
         */
        @Argument(name = ManagedAttributePromotionRule.ARG_ATTRIBUTE_NAME)
        private final ManagedAttribute attribute;
        /**
         * A reference to the application object to which this managed attribute belongs
         */
        @Argument(name = ManagedAttributePromotionRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * Map in which any data can be stored; available to the rule in subsequent rule executions within the same
         * task so expensive data (requiring time-intensive lookups) can be saved and shared between rule executions
         */
        @Argument(name = ManagedAttributePromotionRule.ARG_STATE_NAME)
        private final Map<String, Object> state;
    }
}
