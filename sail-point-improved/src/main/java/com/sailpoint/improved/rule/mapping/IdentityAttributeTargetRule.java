package com.sailpoint.improved.rule.mapping;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.AttributeTarget;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ObjectAttribute;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningProject;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Defined when attribute changes are to be propagated to accounts on other
 * applications. If any manipulation or transformation is required on the attribute value before it can be written to
 * the target application, an IdentityAttributeTarget rule is used to perform that action.
 * <p>
 * Output:
 * Transformed value that will be pushed to the target
 */
@Slf4j
public abstract class IdentityAttributeTargetRule
        extends AbstractJavaRuleExecutor<Object, IdentityAttributeTargetRule.IdentityAttributeTargetRuleArguments> {

    /**
     * Name of value argument name
     */
    public static final String ARG_VALUE = "value";
    /**
     * Name of sourceIdentityAttribute argument name
     */
    public static final String ARG_SOURCE_IDENTITY_ATTRIBUTE = "sourceIdentityAttribute";
    /**
     * Name of sourceIdentityAttributeName argument name
     */
    public static final String ARG_SOURCE_IDENTITY_ATTRIBUTE_NAME = "sourceIdentityAttributeName";
    /**
     * Name of sourceAttributeRequest argument name
     */
    public static final String ARG_SOURCE_ATTRIBUTE_REQUEST = "sourceAttributeRequest";
    /**
     * Name of target argument name
     */
    public static final String ARG_TARGET = "target";
    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";
    /**
     * Name of project argument name
     */
    public static final String ARG_PROJECT = "project";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            IdentityAttributeTargetRule.ARG_SOURCE_IDENTITY_ATTRIBUTE,
            IdentityAttributeTargetRule.ARG_SOURCE_IDENTITY_ATTRIBUTE_NAME,
            IdentityAttributeTargetRule.ARG_SOURCE_ATTRIBUTE_REQUEST,
            IdentityAttributeTargetRule.ARG_TARGET,
            IdentityAttributeTargetRule.ARG_IDENTITY,
            IdentityAttributeTargetRule.ARG_PROJECT
    );

    /**
     * Default constructor
     */
    public IdentityAttributeTargetRule() {
        super(Rule.Type.IdentityAttributeTarget.name(), IdentityAttributeTargetRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected IdentityAttributeTargetRule.IdentityAttributeTargetRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return IdentityAttributeTargetRuleArguments.builder()
                .value(JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IdentityAttributeTargetRule.ARG_VALUE))
                .sourceIdentityAttribute((ObjectAttribute) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                IdentityAttributeTargetRule.ARG_SOURCE_IDENTITY_ATTRIBUTE))
                .sourceIdentityAttributeName((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                IdentityAttributeTargetRule.ARG_SOURCE_IDENTITY_ATTRIBUTE_NAME))
                .sourceAttributeRequest((ProvisioningPlan.AttributeRequest) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                IdentityAttributeTargetRule.ARG_SOURCE_ATTRIBUTE_REQUEST))
                .target((AttributeTarget) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IdentityAttributeTargetRule.ARG_TARGET))
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IdentityAttributeTargetRule.ARG_IDENTITY))
                .project((ProvisioningProject) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, IdentityAttributeTargetRule.ARG_PROJECT))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - value
     * - sourceIdentityAttribute
     * - sourceIdentityAttributeName
     * - sourceAttributeRequest
     * - target
     * - identity
     * - project
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class IdentityAttributeTargetRuleArguments {
        /**
         * Value of the Identity attribute (can be single value or list)
         */
        @Argument(name = IdentityAttributeTargetRule.ARG_VALUE)
        private final Object value;
        /**
         * Reference to the source objectAttribute for this target
         */
        @Argument(name = IdentityAttributeTargetRule.ARG_SOURCE_IDENTITY_ATTRIBUTE)
        private final ObjectAttribute sourceIdentityAttribute;
        /**
         * Name of the identity attribute for this target
         */
        @Argument(name = IdentityAttributeTargetRule.ARG_SOURCE_IDENTITY_ATTRIBUTE_NAME)
        private final String sourceIdentityAttributeName;
        /**
         * Reference to the ProvisioningPlan AttributeRequest that is setting the attribute on the identity
         */
        @Argument(name = IdentityAttributeTargetRule.ARG_SOURCE_ATTRIBUTE_REQUEST)
        private final ProvisioningPlan.AttributeRequest sourceAttributeRequest;
        /**
         * Reference to the AttributeTarget that is being processed
         */
        @Argument(name = IdentityAttributeTargetRule.ARG_TARGET)
        private final AttributeTarget target;
        /**
         * Reference to the Identity being processed
         */
        @Argument(name = IdentityAttributeTargetRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Reference to the ProvisioningProject that contains the changes being requested
         */
        @Argument(name = IdentityAttributeTargetRule.ARG_PROJECT)
        private final ProvisioningProject project;
    }
}
