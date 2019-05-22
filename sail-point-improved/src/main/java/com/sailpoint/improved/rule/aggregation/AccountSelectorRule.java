package com.sailpoint.improved.rule.aggregation;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Application;
import sailpoint.object.Bundle;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Link;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningProject;
import sailpoint.object.Rule;
import sailpoint.object.Source;

import java.util.Arrays;
import java.util.List;

/**
 * Was introduced in IdentityIQ version 6.3 to support provisioning of entitlements
 * through role assignments when a user holds more than one account on the target application. It provides the
 * logic for selecting a target account for provisioning entitlements for an IT role (or any role type with an
 * entitlement profile).
 * <p>
 * Account selector rules run during an identity refresh task with the Provision assignments option selected, when
 * a business role is assigned which has required IT roles that specify these rules. This rule must provide the logic
 * for choosing the account to which the entitlement should be provisioned. Account selector rules also run to
 * chose a target account when a role is requested through Lifecycle Manager; if it does not select a target
 * account, the LCM requester is prompted to select one from a list in the UI.
 * <p>
 * One or more account selector rules can be specified for each IT role; the system supports a global rule which
 * applies to all applications involved in the role profile as well as a rule per application
 * <p>
 * <p>
 * Output:
 * Can return any of these:
 * • one of the available Links (accounts) currently held by the the Identity
 * • a Link with a null nativeIdentity value – this tells the system to create a new Link (any values on the
 * returned Link are ignored and the Link is created based on the role and application provisioning policies)
 * • a null value – causes the system to prompt the requester for an account selection
 * • the string “prompt” – tells the system to prompt the requester for an account selection
 * <p>
 * The difference between null and “prompt” is that “prompt” forces the prompting to occur per IT role so that
 * if there are multiple IT roles involved in the role assignment which all target the same application, a
 * separate target account can be specified for each IT role.
 * <p>
 * Null causes the system to obey configuration settings as they have been specified in LCM and on the role itself, so
 * the prompting may be at the IT role level or at the business role level, depending on that configuration.
 *
 * <T> - return {@link Link} or {@link String} type
 */
@Slf4j
public abstract class AccountSelectorRule<T extends Object>
        extends AbstractJavaRuleExecutor<T, AccountSelectorRule.AccountSelectorRuleArguments> {

    /**
     * Name of source argument name
     */
    public static final String ARG_SOURCE = "source";
    /**
     * Name of role argument name
     */
    public static final String ARG_ROLE = "role";
    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";
    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of links argument name
     */
    public static final String ARG_LINKS = "links";
    /**
     * Name of isSecondary argument name
     */
    public static final String ARG_IS_SECONDARY = "isSecondary";
    /**
     * Name of project argument name
     */
    public static final String ARG_PROJECT = "project";
    /**
     * Name of accountRequest argument name
     */
    public static final String ARG_ACCOUNT_REQUEST = "accountRequest";
    /**
     * Name of allowCreate argument name
     */
    public static final String ARG_ALLOW_CREATE = "allowCreate";


    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            AccountSelectorRule.ARG_SOURCE,
            AccountSelectorRule.ARG_ROLE,
            AccountSelectorRule.ARG_IDENTITY,
            AccountSelectorRule.ARG_APPLICATION,
            AccountSelectorRule.ARG_IS_SECONDARY,
            AccountSelectorRule.ARG_PROJECT,
            AccountSelectorRule.ARG_ACCOUNT_REQUEST,
            AccountSelectorRule.ARG_ALLOW_CREATE
    );

    /**
     * Default constructor
     */
    public AccountSelectorRule() {
        super(Rule.Type.AccountSelector.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected AccountSelectorRule.AccountSelectorRuleArguments buildContainerArguments(
            JavaRuleContext javaRuleContext) {
        return AccountSelectorRuleArguments
                .builder()
                .source(Source.valueOf((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AccountSelectorRule.ARG_SOURCE)))
                .role((Bundle) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AccountSelectorRule.ARG_ROLE))
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AccountSelectorRule.ARG_IDENTITY))
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AccountSelectorRule.ARG_APPLICATION))
                .links((List<Link>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AccountSelectorRule.ARG_LINKS))
                .isSecondary((Boolean) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AccountSelectorRule.ARG_IS_SECONDARY))
                .project((ProvisioningProject) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AccountSelectorRule.ARG_PROJECT))
                .accountRequest((ProvisioningPlan.AccountRequest) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AccountSelectorRule.ARG_ACCOUNT_REQUEST))
                .allowCreate((Boolean) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AccountSelectorRule.ARG_ALLOW_CREATE))
                .build();
    }

    /**
     * Arguments container for {@link AccountSelectorRule}. Contains:
     * - source
     * - role
     * - identity
     * - application
     * - links
     * - isSecondary
     * - project
     * - accountRequest
     * - allowCreate
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class AccountSelectorRuleArguments {
        /**
         * Enum value defining the source of the request (UI, LCM, Task, etc.)
         */
        @Argument(name = AccountSelectorRule.ARG_SOURCE)
        private final Source source;
        /**
         * The IT role being provisioned
         */
        @Argument(name = AccountSelectorRule.ARG_ROLE)
        private final Bundle role;
        /**
         * The Identity to whom the role is being provisioned
         */
        @Argument(name = AccountSelectorRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * The Target application on which the entitlements will be provisioned
         */
        @Argument(name = AccountSelectorRule.ARG_APPLICATION)
        private final Application application;
        /**
         * List of all available links held by the Identity
         */
        @Argument(name = AccountSelectorRule.ARG_LINKS)
        private final List<Link> links;
        /**
         * True if this is not the first assignment of this role to this user
         */
        @Argument(name = AccountSelectorRule.ARG_IS_SECONDARY)
        private final Boolean isSecondary;
        /**
         * Provisioning project for the provisioning request
         */
        @Argument(name = AccountSelectorRule.ARG_PROJECT)
        private final ProvisioningProject project;
        /**
         * Account request containing details to be provisioned to the selected target account
         */
        @Argument(name = AccountSelectorRule.ARG_ACCOUNT_REQUEST)
        private final ProvisioningPlan.AccountRequest accountRequest;
        /**
         * True if account creation is allowed (i.e. if the system can accept and act upon the return from the rule of a new Link with no nativeIdentity)
         */
        @Argument(name = AccountSelectorRule.ARG_ALLOW_CREATE)
        private final Boolean allowCreate;
    }
}
