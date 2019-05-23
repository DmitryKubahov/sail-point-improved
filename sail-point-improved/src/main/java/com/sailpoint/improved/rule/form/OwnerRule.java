package com.sailpoint.improved.rule.form;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Application;
import sailpoint.object.Bundle;
import sailpoint.object.Field;
import sailpoint.object.Form;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Template;

import java.util.Arrays;
import java.util.List;

/**
 * Used by role or application provisioning policies to determine the owner of the provisioning
 * policy or its policy fields. The owner of a field or policy is the Identity who will be asked to provide any input
 * values for the provisioning activity that could not be identified or calculated automatically by the system.
 * <p>
 * NOTE: Fields have an Owner field whether they belong to provisioning policies or forms. However, for forms,
 * the field owner value is ignored. Therefore an Owner rule is only useful for provisioning policy fields.
 * <p>
 * Output:
 * The rule returns an Identity name, or one of several special keywords that
 * can be used to identify the appropriate owner based on the role or application to which the provisioning policy is attached.
 * Those keywords are:
 * • IIQParentOwner: resolves to the owner of the application or role to which the policy belongs {@link Form#OWNER_PARENT}
 * • IIQRoleOwner: the owner of the role to which the policy belongs {@link Form#OWNER_ROLE}
 * • IIQApplicationOwner: the owner of the application to which the policy belongs {@link Form#OWNER_APPLICATION}
 */
@Slf4j
public abstract class OwnerRule
        extends AbstractJavaRuleExecutor<String, OwnerRule.OwnerRuleArguments> {

    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";
    /**
     * Name of role argument name
     */
    public static final String ARG_ROLE = "role";
    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of template argument name
     */
    public static final String ARG_TEMPLATE = "template";
    /**
     * Name of field argument name
     */
    public static final String ARG_FIELD = "field";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            OwnerRule.ARG_IDENTITY,
            OwnerRule.ARG_APPLICATION,
            OwnerRule.ARG_TEMPLATE
    );

    /**
     * Default constructor
     */
    public OwnerRule() {
        super(Rule.Type.Owner.name(), OwnerRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected OwnerRule.OwnerRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return OwnerRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, OwnerRule.ARG_IDENTITY))
                .role((Bundle) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, OwnerRule.ARG_ROLE))
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, OwnerRule.ARG_APPLICATION))
                .template((Template) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, OwnerRule.ARG_TEMPLATE))
                .field((Field) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, OwnerRule.ARG_FIELD))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     * - role
     * - application
     * - template
     * - field
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class OwnerRuleArguments {
        /**
         * Reference to the Identity being provisioned
         */
        @Argument(name = OwnerRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Reference to the role object involved in the provisioning process (if applicable)
         */
        @Argument(name = OwnerRule.ARG_ROLE)
        private final Bundle role;
        /**
         * Reference to the application object to which the provisioning will occur
         */
        @Argument(name = OwnerRule.ARG_APPLICATION)
        private final Application application;
        /**
         * Reference to the template object that defines the provisioning policy form
         */
        @Argument(name = OwnerRule.ARG_TEMPLATE)
        private final Template template;
        /**
         * Reference to the field object being assigned an owner (if any)
         */
        @Argument(name = OwnerRule.ARG_FIELD)
        private final Field field;
    }
}
