package com.sailpoint.improved.rule.aggregation;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ResourceObject;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * If the correlation rule cannot find an Identity that corresponds to the account, one must be created. By default,
 * the Identity Name is set to the display attribute from the resource object (or the identity attribute if display
 * attribute is null) and the Manager attribute is set to false. An IdentityCreation rule specifies any other Identity
 * attribute population, or any change to these two attribute values, based on the account data. It can also be used
 * to set values like a default IdentityIQ password for the Identity.
 * <p>
 * If the application is not an authoritative application, any Identities created for its accounts must later be
 * manually correlated to an authoritative Identity or the accounts will have to be recorrelated through an
 * automated process to connect them to the correct authoritative Identities.
 * <p>
 * IdentityCreation rules are most commonly specified for authoritative applications, since new Identities created
 * from those accounts are real, permanent Identities. However, they can also be used for non-authoritative
 * application accounts to set attributes that can make manual correlation easier.
 * <p>
 * Outputs:
 * None. The identity object passed as parameter to the rule should be edited directly by the rule.
 */
@Slf4j
public abstract class IdentityCreationRule
        extends AbstractJavaRuleExecutor<Object, IdentityCreationRule.IdentityCreationRuleArguments> {

    /**
     * Name of environment argument name
     */
    public static final String ARG_ENVIRONMENT = "environment";
    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of account argument name
     */
    public static final String ARG_ACCOUNT = "account";
    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            IdentityCreationRule.ARG_ENVIRONMENT,
            IdentityCreationRule.ARG_APPLICATION,
            IdentityCreationRule.ARG_ACCOUNT,
            IdentityCreationRule.ARG_IDENTITY
    );

    /**
     * Default constructor
     */
    public IdentityCreationRule() {
        super(Rule.Type.IdentityCreation.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected IdentityCreationRule.IdentityCreationRuleArguments buildContainerArguments(
            JavaRuleContext javaRuleContext) {
        return IdentityCreationRuleArguments
                .builder()
                .environment((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, IdentityCreationRule.ARG_ENVIRONMENT))
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, IdentityCreationRule.ARG_APPLICATION))
                .account((ResourceObject) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, IdentityCreationRule.ARG_ACCOUNT))
                .identity((Identity) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, IdentityCreationRule.ARG_IDENTITY))
                .build();
    }

    /**
     * Arguments container for {@link IdentityCreationRule}. Contains:
     * - environment
     * - application
     * - account
     * - identity
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class IdentityCreationRuleArguments {
        /**
         * Map of arguments passed to the aggregation task
         */
        @Argument(name = IdentityCreationRule.ARG_ENVIRONMENT)
        private final Map<String, Object> environment;
        /**
         * Reference to the application object from which the account was read
         */
        @Argument(name = IdentityCreationRule.ARG_APPLICATION)
        private final Application application;
        /**
         * Reference to the ResourceObject representing the account
         */
        @Argument(name = IdentityCreationRule.ARG_ACCOUNT)
        private final ResourceObject account;
        /**
         * Reference to the Identity being created
         */
        @Argument(name = IdentityCreationRule.ARG_IDENTITY)
        private final Identity identity;
    }
}
