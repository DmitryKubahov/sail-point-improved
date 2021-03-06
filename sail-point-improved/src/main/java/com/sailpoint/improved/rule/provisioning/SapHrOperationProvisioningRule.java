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
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningResult;
import sailpoint.object.Rule;
import sailpoint.object.Schema;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

/**
 * The SAP HR/HCM connector was introduced in version 7.0, so this rule applies only to versions 7.0+.
 * An SAP HR Operation Provisioning rule is an alternative to the SAP HR Provision rule when the installation wants
 * to partition their provisioning logic into individual rules per operation. It contains the installation-specific
 * provisioning logic for each specific provisioning operation for the SAP HR application.
 * Separate SapHrOperationProvisioning rules can be created for account enabling, account disabling, account
 * deletion, account unlocking, account creation, and account modification.
 * <p>
 * Output:
 * ProvisioningResult object containing the status (success, failure, retry, etc.) of the provisioning request
 */
@Slf4j
public abstract class SapHrOperationProvisioningRule
        extends AbstractJavaRuleExecutor<ProvisioningResult, SapHrOperationProvisioningRule.SapHrOperationProvisioningRuleArguments> {

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
     * Name of connection argument name
     */
    public static final String ARG_CONNECTION = "connection";
    /**
     * Name of plan argument name
     */
    public static final String ARG_PLAN = "plan";
    /**
     * Name of request argument name
     */
    public static final String ARG_REQUEST = "request";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            SapHrOperationProvisioningRule.ARG_APPLICATION,
            SapHrOperationProvisioningRule.ARG_SCHEMA,
            SapHrOperationProvisioningRule.ARG_DESTINATION,
            SapHrOperationProvisioningRule.ARG_CONNECTION,
            SapHrOperationProvisioningRule.ARG_PLAN,
            SapHrOperationProvisioningRule.ARG_REQUEST
    );

    /**
     * Default constructor
     */
    public SapHrOperationProvisioningRule() {
        super(Rule.Type.SapHrOperationProvisioning.name(), SapHrOperationProvisioningRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected SapHrOperationProvisioningRule.SapHrOperationProvisioningRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return SapHrOperationProvisioningRuleArguments.builder()
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SapHrOperationProvisioningRule.ARG_APPLICATION))
                .schema((Schema) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SapHrOperationProvisioningRule.ARG_SCHEMA))
                .destination((JCoDestination) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SapHrOperationProvisioningRule.ARG_DESTINATION))
                .connection((Connection) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SapHrOperationProvisioningRule.ARG_CONNECTION))
                .plan((ProvisioningPlan) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SapHrOperationProvisioningRule.ARG_PLAN))
                .request((ProvisioningPlan.AbstractRequest) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SapHrOperationProvisioningRule.ARG_REQUEST))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - application
     * - schema
     * - destination
     * - connection
     * - plan
     * - request
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class SapHrOperationProvisioningRuleArguments {
        /**
         * Reference to the application object
         */
        @Argument(name = SapHrOperationProvisioningRule.ARG_APPLICATION)
        private final Application application;
        /**
         * Reference to the application schema
         */
        @Argument(name = SapHrOperationProvisioningRule.ARG_SCHEMA)
        private final Schema schema;
        /**
         * A connected and ready to use SAP destination object that can be used to call BAPI function modules and call to SAP tables
         */
        @Argument(name = SapHrOperationProvisioningRule.ARG_DESTINATION)
        private final JCoDestination destination;
        /**
         * Connection object to connect to the JDBC database
         */
        @Argument(name = SapHrOperationProvisioningRule.ARG_CONNECTION)
        private final Connection connection;
        /**
         * Provisioning plan containing the provisioning request(s)
         */
        @Argument(name = SapHrOperationProvisioningRule.ARG_PLAN)
        private final ProvisioningPlan plan;
        /**
         * AbstractRequest object containing the account request (or object request, in the case of group provisioning) to be processed
         */
        @Argument(name = SapHrOperationProvisioningRule.ARG_REQUEST)
        private final ProvisioningPlan.AbstractRequest request;
    }
}
