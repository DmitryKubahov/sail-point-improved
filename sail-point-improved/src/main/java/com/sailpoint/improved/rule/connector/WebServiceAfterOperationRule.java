package com.sailpoint.improved.rule.connector;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.connector.webservices.EndPoint;
import sailpoint.connector.webservices.WebServicesClient;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The WebService connector allows IdentityIQ to connect to applications through the applications’ REST API
 * methods, for both read and write operations. The appropriate REST calls can be configured for each desired
 * operation type. For each operation type, a “before” and “after” operation rule can be specified, as described
 * here and in the rule type above. This connector type, and therefore this rule, was added to IdentityIQ in version 7.1.
 * The WebServiceAfterOperationRule is run immediately after running the associated REST call to the target system.
 */
@Slf4j
public abstract class WebServiceAfterOperationRule
        extends AbstractJavaRuleExecutor<Map<String, Object>, WebServiceAfterOperationRule.WebServiceAfterOperationRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of requestEndPoint argument name
     */
    public static final String ARG_REQUEST_END_POINT = "requestEndPoint";
    /**
     * Name of processedResponseObject argument name
     */
    public static final String ARG_PROCESSED_RESPONSE_OBJECT = "processedResponseObject";
    /**
     * Name of rawResponseObject argument name
     */
    public static final String ARG_RAW_RESPONSE_OBJECT = "rawResponseObject";
    /**
     * Name of restClient argument name
     */
    public static final String ARG_REST_CLIENT = "restClient";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            WebServiceAfterOperationRule.ARG_APPLICATION,
            WebServiceAfterOperationRule.ARG_REQUEST_END_POINT,
            WebServiceAfterOperationRule.ARG_PROCESSED_RESPONSE_OBJECT,
            WebServiceAfterOperationRule.ARG_RAW_RESPONSE_OBJECT,
            WebServiceAfterOperationRule.ARG_REST_CLIENT
    );

    /**
     * Default constructor
     */
    public WebServiceAfterOperationRule() {
        super(Rule.Type.WebServiceAfterOperationRule.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected WebServiceAfterOperationRule.WebServiceAfterOperationRuleArguments buildContainerArguments(
            JavaRuleContext javaRuleContext) {
        return WebServiceAfterOperationRuleArguments
                .builder()
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, WebServiceAfterOperationRule.ARG_APPLICATION))
                .requestEndPoint((EndPoint) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext,
                                WebServiceAfterOperationRule.ARG_REQUEST_END_POINT))
                .processedResponseObject((List<Map<String, Object>>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext,
                                WebServiceAfterOperationRule.ARG_PROCESSED_RESPONSE_OBJECT))
                .rawResponseObject((String) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        WebServiceAfterOperationRule.ARG_RAW_RESPONSE_OBJECT))
                .restClient((WebServicesClient) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, WebServiceAfterOperationRule.ARG_REST_CLIENT))
                .build();
    }

    /**
     * Arguments container for {@link WebServiceAfterOperationRule}. Contains:
     * - application
     * - requestEndPoint
     * - processedResponseObject
     * - rawResponseObject
     * - restClient
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class WebServiceAfterOperationRuleArguments {
        /**
         * A reference to the Application object
         */
        @Argument(name = WebServiceAfterOperationRule.ARG_APPLICATION)
        private final Application application;
        /**
         * Current request information; contains:
         * - header
         * - body
         * - context url
         * - method type
         * - response attribute map
         * - successful response code
         */
        @Argument(name = WebServiceAfterOperationRule.ARG_REQUEST_END_POINT)
        private final EndPoint requestEndPoint;
        /**
         * Response Object processed by the Web Services connector
         */
        @Argument(name = WebServiceAfterOperationRule.ARG_PROCESSED_RESPONSE_OBJECT)
        private final List<Map<String, Object>> processedResponseObject;
        /**
         * Response Object returned from the end system
         */
        @Argument(name = WebServiceAfterOperationRule.ARG_RAW_RESPONSE_OBJECT)
        private final String rawResponseObject;
        /**
         * REST client object
         */
        @Argument(name = WebServiceAfterOperationRule.ARG_REST_CLIENT)
        private final WebServicesClient restClient;
    }
}
