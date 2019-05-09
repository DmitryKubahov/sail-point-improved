package com.sailpoint.improved.rule.provisioning;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.connector.PeopleSoftHRMSConnector;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningResult;
import sailpoint.object.Rule;
import sailpoint.object.Schema;

import java.util.Arrays;
import java.util.List;

/**
 * The PeopleSoft HCM Database connector was introduced in version 7.0, so this rule applies only to versions 7.0+.
 * A PeopleSoft HRMS Operation Provisioning rule is an alternative to the PeopleSoft HRMS Provision rule when
 * the installation wants to partition their provisioning logic into individual rules per operation. It contains the
 * installation-specific provisioning logic for each specific provisioning operation for the PeopleSoft HCM Database
 * application.
 * <p>
 * Separate PeopleSoftHRMSOperationProvisioning rules can be created for account enabling, account disabling,
 * account deletion, account unlocking, account creation, and account modification.
 * <p>
 * Output:
 * ProvisioningResult object containing the status (success, failure, retry, etc.) of the provisioning request
 */
@Slf4j
public abstract class PeopleSoftHRMSOperationProvisioningRule
        extends AbstractJavaRuleExecutor<ProvisioningResult, PeopleSoftHRMSOperationProvisioningRule.PeopleSoftHRMSOperationProvisioningRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION_NAME = "application";
    /**
     * Name of session argument name
     */
    public static final String ARG_SESSION_NAME = "session";
    /**
     * Name of schema argument name
     */
    public static final String ARG_SCHEMA_NAME = "schema";
    /**
     * Name of plan argument name
     */
    public static final String ARG_PLAN_NAME = "plan";
    /**
     * Name of request argument name
     */
    public static final String ARG_REQUEST_NAME = "request";
    /**
     * Name of connector argument name
     */
    public static final String ARG_CONNECTOR_NAME = "connector";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            PeopleSoftHRMSOperationProvisioningRule.ARG_APPLICATION_NAME,
            PeopleSoftHRMSOperationProvisioningRule.ARG_SESSION_NAME,
            PeopleSoftHRMSOperationProvisioningRule.ARG_SCHEMA_NAME,
            PeopleSoftHRMSOperationProvisioningRule.ARG_PLAN_NAME,
            PeopleSoftHRMSOperationProvisioningRule.ARG_REQUEST_NAME,
            PeopleSoftHRMSOperationProvisioningRule.ARG_CONNECTOR_NAME
    );

    /**
     * Default constructor
     */
    public PeopleSoftHRMSOperationProvisioningRule() {
        super(Rule.Type.PeopleSoftHRMSOperationProvisioning.name(),
                PeopleSoftHRMSOperationProvisioningRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected PeopleSoftHRMSOperationProvisioningRule.PeopleSoftHRMSOperationProvisioningRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return PeopleSoftHRMSOperationProvisioningRuleArguments.builder()
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                PeopleSoftHRMSOperationProvisioningRule.ARG_APPLICATION_NAME))
                .session(JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        PeopleSoftHRMSOperationProvisioningRule.ARG_SESSION_NAME))
                .schema((Schema) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                PeopleSoftHRMSOperationProvisioningRule.ARG_SCHEMA_NAME))
                .plan((ProvisioningPlan) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PeopleSoftHRMSOperationProvisioningRule.ARG_PLAN_NAME))
                .request((ProvisioningPlan.AbstractRequest) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                PeopleSoftHRMSOperationProvisioningRule.ARG_REQUEST_NAME))
                .connector((PeopleSoftHRMSConnector) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                PeopleSoftHRMSOperationProvisioningRule.ARG_CONNECTOR_NAME))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - application
     * - session
     * - schema
     * - plan
     * - request
     * - connector
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class PeopleSoftHRMSOperationProvisioningRuleArguments {
        /**
         * Reference to the application object
         */
        @Argument(name = PeopleSoftHRMSOperationProvisioningRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * Session connection to PeopleSoft server
         */
        @Argument(name = PeopleSoftHRMSOperationProvisioningRule.ARG_SESSION_NAME)
        private final Object session;
        /**
         * Reference to the application schema
         */
        @Argument(name = PeopleSoftHRMSOperationProvisioningRule.ARG_SCHEMA_NAME)
        private final Schema schema;
        /**
         * Provisioning plan containing the provisioning request(s)
         */
        @Argument(name = PeopleSoftHRMSOperationProvisioningRule.ARG_PLAN_NAME)
        private final ProvisioningPlan plan;
        /**
         * AbstractRequest object containing the account request (or object request, in the case of group provisioning) to be processed
         */
        @Argument(name = PeopleSoftHRMSOperationProvisioningRule.ARG_REQUEST_NAME)
        private final ProvisioningPlan.AbstractRequest request;
        /**
         * Application connector being used for the operation
         */
        @Argument(name = PeopleSoftHRMSOperationProvisioningRule.ARG_CONNECTOR_NAME)
        private final PeopleSoftHRMSConnector connector;

    }
}
