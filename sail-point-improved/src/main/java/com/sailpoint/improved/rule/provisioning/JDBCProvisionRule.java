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
 * Rule is only specified for an application that uses the JDBC connector and supports provisioning.
 * It contains the application-specific provisioning logic for applications which use that connector. The JDBC
 * connector is a generic connector that cannot know how to provision to the specific database except as
 * instructed in custom-written logic provided a provisioning rule.
 * <p>
 * Output:
 * ProvisioningResult object containing the status (success, failure, retry, etc.) of the provisioning request
 */
@Slf4j
public abstract class JDBCProvisionRule
        extends AbstractJavaRuleExecutor<ProvisioningResult, JDBCProvisionRule.JDBCProvisionRuleArguments> {

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
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            JDBCProvisionRule.ARG_PLAN_NAME,
            JDBCProvisionRule.ARG_APPLICATION_NAME
    );

    /**
     * Default constructor
     */
    public JDBCProvisionRule() {
        super(Rule.Type.JDBCProvision.name(), JDBCProvisionRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected JDBCProvisionRule.JDBCProvisionRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return JDBCProvisionRuleArguments.builder()
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, JDBCProvisionRule.ARG_APPLICATION_NAME))
                .schema((Schema) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, JDBCProvisionRule.ARG_SCHEMA_NAME))
                .connection((Connection) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, JDBCProvisionRule.ARG_CONNECTION_NAME))
                .plan((ProvisioningPlan) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, JDBCProvisionRule.ARG_PLAN_NAME))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - application
     * - schema
     * - connection
     * - plan
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class JDBCProvisionRuleArguments {
        /**
         * Reference to the application object
         */
        @Argument(name = JDBCProvisionRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * Reference to the application schema
         */
        @Argument(name = JDBCProvisionRule.ARG_SCHEMA_NAME)
        private final Schema schema;
        /**
         * Connection object to connect to the JDBC database
         */
        @Argument(name = JDBCProvisionRule.ARG_CONNECTION_NAME)
        private final Connection connection;
        /**
         * Provisioning plan containing the provisioning request(s)
         */
        @Argument(name = JDBCProvisionRule.ARG_PLAN_NAME)
        private final ProvisioningPlan plan;

    }
}
