package com.sailpoint.improved.rule.connector;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import com.sap.conn.jco.JCoDestination;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.connector.SAPInternalConnector;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.Schema;

import java.util.Arrays;
import java.util.List;

/**
 * Contains HR data and therefore often contains the manager-employee relationship data for the
 * organization. There are a couple of different predefined manager relationship models which are supported in
 * Rules in the application, but some customers may have a custom manager relationship model implemented. This rule
 * allows those customers to apply their model to the data read from the SAP HR/HCM application to calculate the
 * appropriate manager user for each identity.
 */
@Slf4j
public abstract class SAPHRManagerRule
        extends AbstractJavaRuleExecutor<String, SAPHRManagerRule.SAPHRManagerRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION_NAME = "application";
    /**
     * Name of schema argument name
     */
    public static final String ARG_SCHEMA_NAME = "schema";
    /**
     * Name of destination argument name
     */
    public static final String ARG_DESTINATION_NAME = "destination";
    /**
     * Name of connector argument name
     */
    public static final String ARG_CONNECTOR_NAME = "connector";
    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            SAPHRManagerRule.ARG_APPLICATION_NAME,
            SAPHRManagerRule.ARG_SCHEMA_NAME,
            SAPHRManagerRule.ARG_DESTINATION_NAME,
            SAPHRManagerRule.ARG_CONNECTOR_NAME
    );

    /**
     * Default constructor
     */
    public SAPHRManagerRule() {
        super(Rule.Type.SAPHRManagerRule.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected SAPHRManagerRule.SAPHRManagerRuleArguments buildContainerArguments(JavaRuleContext javaRuleContext) {
        return SAPHRManagerRuleArguments
                .builder()
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, SAPHRManagerRule.ARG_APPLICATION_NAME))
                .schema((Schema) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, SAPHRManagerRule.ARG_SCHEMA_NAME))
                .destination((JCoDestination) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, SAPHRManagerRule.ARG_DESTINATION_NAME))
                .connector((SAPInternalConnector) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, SAPHRManagerRule.ARG_CONNECTOR_NAME))
                .build();
    }

    /**
     * Arguments container for {@link SAPHRManagerRule}. Contains:
     * - application
     * - schema
     * - destination
     * - connector
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class SAPHRManagerRuleArguments {
        /**
         * A reference to the Application object
         */
        @Argument(name = SAPHRManagerRule.ARG_APPLICATION_NAME)
        private final Application application;
        /**
         * A reference to the Schema object that represents the object we are building
         */
        @Argument(name = SAPHRManagerRule.ARG_SCHEMA_NAME)
        private final Schema schema;
        /**
         * A connected and ready to use SAP destination object that can be used to call BAPI function modules and call to SAP tables
         */
        @Argument(name = SAPHRManagerRule.ARG_DESTINATION_NAME)
        private final JCoDestination destination;
        /**
         * A reference to the current SAP Connector
         */
        @Argument(name = SAPHRManagerRule.ARG_CONNECTOR_NAME)
        private final SAPInternalConnector connector;
    }
}
