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
 * The PeopleSoft HCM Database connector was introduced in version 7.0, so this rule applies only to versions
 * 7.0+.
 * Two options are available for configuring provisioning to PeopleSoft HCM Database applications:
 * 1. A single rule (of type PeopleSoftHRMSProvision) which contains all supported provisioning operations,
 * 2. A collection of operation-specific rules (of type PeopleSoftHRMSOperationProvisioning, discussed
 * below), one per supported provisioning operation
 * <p>
 * A PeopleSoft HRMS Provision rule is specified for an application that uses the PeopleSoft HCM Database
 * connector if it will support provisioning. It contains the installation-specific provisioning logic for provisioning to
 * the PeopleSoft HCM database application.
 * <p>
 * Output:
 * ProvisioningResult object containing the status (success, failure, retry, etc.) of the provisioning request
 */
@Slf4j
public abstract class PeopleSoftHRMSProvisionRule
        extends AbstractJavaRuleExecutor<ProvisioningResult, PeopleSoftHRMSProvisionRule.PeopleSoftHRMSProvisionRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of schema argument name
     */
    public static final String ARG_SCHEMA = "schema";
    /**
     * Name of plan argument name
     */
    public static final String ARG_PLAN = "plan";
    /**
     * Name of request argument name
     */
    public static final String ARG_REQUEST = "request";
    /**
     * Name of connector argument name
     */
    public static final String ARG_CONNECTOR = "connector";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            PeopleSoftHRMSProvisionRule.ARG_APPLICATION,
            PeopleSoftHRMSProvisionRule.ARG_SCHEMA,
            PeopleSoftHRMSProvisionRule.ARG_PLAN,
            PeopleSoftHRMSProvisionRule.ARG_CONNECTOR
    );

    /**
     * Default constructor
     */
    public PeopleSoftHRMSProvisionRule() {
        super(Rule.Type.PeopleSoftHRMSProvision.name(), PeopleSoftHRMSProvisionRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected PeopleSoftHRMSProvisionRule.PeopleSoftHRMSProvisionRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return PeopleSoftHRMSProvisionRuleArguments.builder()
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PeopleSoftHRMSProvisionRule.ARG_APPLICATION))
                .schema((Schema) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PeopleSoftHRMSProvisionRule.ARG_SCHEMA))
                .plan((ProvisioningPlan) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PeopleSoftHRMSProvisionRule.ARG_PLAN))
                .request((ProvisioningPlan.AbstractRequest) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PeopleSoftHRMSProvisionRule.ARG_REQUEST))
                .connector((PeopleSoftHRMSConnector) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, PeopleSoftHRMSProvisionRule.ARG_CONNECTOR))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - application
     * - schema
     * - plan
     * - request
     * - connector
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class PeopleSoftHRMSProvisionRuleArguments {
        /**
         * Reference to the application object
         */
        @Argument(name = PeopleSoftHRMSProvisionRule.ARG_APPLICATION)
        private final Application application;
        /**
         * Reference to the application schema
         */
        @Argument(name = PeopleSoftHRMSProvisionRule.ARG_SCHEMA)
        private final Schema schema;
        /**
         * Provisioning plan containing the provisioning request(s)
         */
        @Argument(name = PeopleSoftHRMSProvisionRule.ARG_PLAN)
        private final ProvisioningPlan plan;
        /**
         * AccountRequest being processed; always null for this global rule; only set for {@link PeopleSoftHRMSOperationProvisioningRule}
         */
        @Argument(name = PeopleSoftHRMSProvisionRule.ARG_REQUEST)
        private final ProvisioningPlan.AbstractRequest request;
        /**
         * Application connector being used for the operation
         */
        @Argument(name = PeopleSoftHRMSProvisionRule.ARG_CONNECTOR)
        private final PeopleSoftHRMSConnector connector;

    }
}
