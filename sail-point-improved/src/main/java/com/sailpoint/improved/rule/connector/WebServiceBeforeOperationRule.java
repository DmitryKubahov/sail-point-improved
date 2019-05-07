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
 * The WebService connector supports interaction with (through both read and write operations) systems which
 * provide a REST API to allow other applications to connect to them. The appropriate REST calls can be configured
 * for each desired interaction type. For each interaction type, a “before” and “after” operation rule can be
 * specified, as described here and in the next rule type. This connector type, and therefore this rule, was added to
 * IdentityIQ in version 7.1.
 * <p>
 * NOTE: The WebService connector only supports JSON Responses from the API for both read and write
 * operations.
 */
@Slf4j
public abstract class WebServiceBeforeOperationRule
        extends AbstractJavaRuleExecutor<EndPoint, WebServiceBeforeOperationRule.WebServiceBeforeOperationRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION_NAME = "application";
    /**
     * Name of requestEndPoint argument name
     */
    public static final String ARG_REQUEST_END_POINT_NAME = "requestEndPoint";
    /**
     * Name of oldResponseMap argument name
     */
    public static final String ARG_OLD_RESPONSE_MAP_NAME = "oldResponseMap";
    /**
     * Name of restClient argument name
     */
    public static final String ARG_REST_CLIENT_NAME = "restClient";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            WebServiceBeforeOperationRule.ARG_APPLICATION_NAME,
            WebServiceBeforeOperationRule.ARG_REQUEST_END_POINT_NAME,
            WebServiceBeforeOperationRule.ARG_OLD_RESPONSE_MAP_NAME,
            WebServiceBeforeOperationRule.ARG_REST_CLIENT_NAME
    );

    /**
     * Default constructor
     */
    public WebServiceBeforeOperationRule() {
        super(Rule.Type.WebServiceBeforeOperationRule.name(), NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected WebServiceBeforeOperationRule.WebServiceBeforeOperationRuleArguments buildContainerArguments(
            JavaRuleContext javaRuleContext) {
        return WebServiceBeforeOperationRuleArguments
                .builder()
                .application((Application) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, WebServiceBeforeOperationRule.ARG_APPLICATION_NAME))
                .requestEndPoint((EndPoint) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext,
                                WebServiceBeforeOperationRule.ARG_REQUEST_END_POINT_NAME))
                .oldResponseMap((Map<String, Object>) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext,
                                WebServiceBeforeOperationRule.ARG_OLD_RESPONSE_MAP_NAME))
                .restClient((WebServicesClient) JavaRuleExecutorUtil.
                        getArgumentValueByName(javaRuleContext, WebServiceBeforeOperationRule.ARG_REST_CLIENT_NAME))
                .build();
    }

    /**
     * Arguments container for {@link WebServiceBeforeOperationRule}. Contains:
     * - application
     * - requestEndPoint
     * - oldResponseMap
     * - restClient
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class WebServiceBeforeOperationRuleArguments {
        /**
         * A reference to the Application object
         */
        @Argument(name = WebServiceBeforeOperationRule.ARG_APPLICATION_NAME)
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
        @Argument(name = WebServiceBeforeOperationRule.ARG_REQUEST_END_POINT_NAME)
        private final EndPoint requestEndPoint;
        /**
         * Earlier response object
         */
        @Argument(name = WebServiceBeforeOperationRule.ARG_OLD_RESPONSE_MAP_NAME)
        private final Map<String, Object> oldResponseMap;
        /**
         * REST client object
         */
        @Argument(name = WebServiceBeforeOperationRule.ARG_REST_CLIENT_NAME)
        private final WebServicesClient restClient;
    }
}
