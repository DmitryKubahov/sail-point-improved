package com.sailpoint.improved.rule.provisioning;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import com.sap.conn.jco.JCoDestination;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.connector.SAPHRConnector;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningResult;
import sailpoint.object.Rule;
import sailpoint.object.Schema;

import java.util.Arrays;
import java.util.List;

/**
 * The SAP HR/HCM connector was introduced in version 7.0, so this rule applies only to versions 7.0+.
 * Two options are available for configuring provisioning to SAP HR/HCM applications:
 * 1. A single rule (of type SapHrProvision) which contains all supported provisioning operations, or
 * 2. A collection of operation-specific rules (of type SapHrOperationProvisioning, discussed below), one per
 * supported provisioning operation
 * An SAP HR Provision rule is specified for an application that uses the SAP HR/HCM connector if it will support
 * provisioning. It contains the installation-specific provisioning logic for provisioning to the SAP HR application.
 * <p>
 * Output:
 * ProvisioningResult object containing the status (success, failure, retry, etc.) of the provisioning request
 */
@Slf4j
public abstract class SapHrProvisionRule
        extends AbstractJavaRuleExecutor<ProvisioningResult, SapHrProvisionRule.SapHrProvisionRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of schema argument name
     */
    public static final String ARG_SCHEMA = "schema";
    /**
     * Name of destination argument name
     */
    public static final String ARG_DESTINATION = "destination";
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
            SapHrProvisionRule.ARG_APPLICATION,
            SapHrProvisionRule.ARG_SCHEMA,
            SapHrProvisionRule.ARG_DESTINATION,
            SapHrProvisionRule.ARG_PLAN,
            SapHrProvisionRule.ARG_CONNECTOR
    );

    /**
     * Default constructor
     */
    public SapHrProvisionRule() {
        super(Rule.Type.SapHrProvision.name(), SapHrProvisionRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected SapHrProvisionRule.SapHrProvisionRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return SapHrProvisionRuleArguments.builder()
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SapHrProvisionRule.ARG_APPLICATION))
                .schema((Schema) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SapHrProvisionRule.ARG_SCHEMA))
                .destination((JCoDestination) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SapHrProvisionRule.ARG_DESTINATION))
                .plan((ProvisioningPlan) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SapHrProvisionRule.ARG_PLAN))
                .request((ProvisioningPlan.AbstractRequest) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SapHrProvisionRule.ARG_REQUEST))
                .connector((SAPHRConnector) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SapHrProvisionRule.ARG_CONNECTOR))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - application
     * - schema
     * - destination
     * - plan
     * - request
     * - connector
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class SapHrProvisionRuleArguments {
        /**
         * Reference to the application object
         */
        @Argument(name = SapHrProvisionRule.ARG_APPLICATION)
        private final Application application;
        /**
         * Reference to the application schema
         */
        @Argument(name = SapHrProvisionRule.ARG_SCHEMA)
        private final Schema schema;
        /**
         * A connected and ready to use SAP destination object that can be used to call BAPI function modules and call to SAP tables
         */
        @Argument(name = SapHrProvisionRule.ARG_DESTINATION)
        private final JCoDestination destination;
        /**
         * Provisioning plan containing the provisioning request(s)
         */
        @Argument(name = SapHrProvisionRule.ARG_PLAN)
        private final ProvisioningPlan plan;
        /**
         * AccountRequest being processed; always null for this global rule; only set for {@link SapHrOperationProvisioningRule}
         */
        @Argument(name = SapHrProvisionRule.ARG_REQUEST)
        private final ProvisioningPlan.AbstractRequest request;
        /**
         * Application connector being used for the operation
         */
        @Argument(name = SapHrProvisionRule.ARG_CONNECTOR)
        private final SAPHRConnector connector;

    }
}
