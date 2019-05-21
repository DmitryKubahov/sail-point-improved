package com.sailpoint.improved.rule.provisioning;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
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
 * Only specified for an application that uses the JDBC connector and
 * supports provisioning. It contains application- and operation-specific provisioning logic for the application. The
 * JDBC connector is a generic connector that cannot know how to provision to the specific database except as
 * instructed in custom-written logic provided a provisioning rule.
 * <p>
 * Separate JDBCOperationProvisioning rules are created for account enabling, account disabling, account deletion,
 * account unlocking, account creation, and account modification. This rule type was introduced in IdentityIQ
 * version 6.1 as an alternative to specifying a single JDBCProvision rule which performs all of these operations for
 * the application.
 * <p>
 * Output:
 * ProvisioningResult object containing the status (success, failure, retry, etc.) of the provisioning request
 */
@Slf4j
public abstract class JDBCOperationProvisioningRule
        extends AbstractJavaRuleExecutor<ProvisioningResult, JDBCOperationProvisioningRule.JDBCOperationProvisioningRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION_NAME = "application";
    /**
     * Name of schema argument name
     */
    public static final String ARG_SCHEMA_NAME = "schema";
    /**
     * Name of connection argument name
     */
    public static final String ARG_CONNECTION_NAME = "connection";
    /**
     * Name of plan argument name
     */
    public static final String ARG_PLAN_NAME = "plan";
    /**
     * Name of request argument name
     */
    public static final String ARG_REQUEST_NAME = "request";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            JDBCOperationProvisioningRule.ARG_APPLICATION_NAME,
            JDBCOperationProvisioningRule.ARG_SCHEMA_NAME,
            JDBCOperationProvisioningRule.ARG_CONNECTION_NAME,
            JDBCOperationProvisioningRule.ARG_PLAN_NAME,
            JDBCOperationProvisioningRule.ARG_REQUEST_NAME
    );

    /**
     * Default constructor
     */
    public JDBCOperationProvisioningRule() {
        super(Rule.Type.JDBCOperationProvisioning.name(), JDBCOperationProvisioningRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected JDBCOperationProvisioningRule.JDBCOperationProvisioningRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return JDBCOperationProvisioningRuleArguments.builder()
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, JDBCOperationProvisioningRule.ARG_APPLICATION_NAME))
                .schema((Schema) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, JDBCOperationProvisioningRule.ARG_SCHEMA_NAME))
                .connection((Connection) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, JDBCOperationProvisioningRule.ARG_CONNECTION_NAME))
                .plan((ProvisioningPlan) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, JDBCOperationProvisioningRule.ARG_PLAN_NAME))
                .request((ProvisioningPlan.AbstractRequest) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, JDBCOperationProvisioningRule.ARG_REQUEST_NAME))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - application
     * - schema
     * - connection
     * - plan
     * - request
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class JDBCOperationProvisioningRuleArguments {
        /**
         * Reference to the application object
         */
        @Argument(name = JDBCOperationProvisioningRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * Reference to the application schema
         */
        @Argument(name = JDBCOperationProvisioningRule.ARG_SCHEMA_NAME)
        private final Schema schema;
        /**
         * Connection object to connect to the JDBC database
         */
        @Argument(name = JDBCOperationProvisioningRule.ARG_CONNECTION_NAME)
        private final Connection connection;
        /**
         * Provisioning plan containing the provisioning request(s)
         */
        @Argument(name = JDBCOperationProvisioningRule.ARG_PLAN_NAME)
        private final ProvisioningPlan plan;
        /**
         * AbstractRequest object containing the account request (or object request, in the case of group provisioning) to be processed
         */
        @Argument(name = JDBCOperationProvisioningRule.ARG_REQUEST_NAME)
        private final ProvisioningPlan.AbstractRequest request;

    }
}
