package com.sailpoint.rule.connector;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.connector.WebServiceBeforeOperationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.connector.webservices.EndPoint;

/**
 * Simple implementation of {@link WebServiceBeforeOperationRule} rule
 */
@Slf4j
@Rule(value = "Simple web service before operation rule", type = sailpoint.object.Rule.Type.WebServiceBeforeOperationRule)
public class SimpleWebServiceBeforeOperationRule extends WebServiceBeforeOperationRule {

    /**
     * Just log full request endpoint path with INFO
     */
    @Override
    @Argument(name = "endpoint", type = ArgumentType.RETURNS, isReturnsType = true)
    protected EndPoint internalExecute(SailPointContext context,
                                       WebServiceBeforeOperationRuleArguments arguments) {
        log.info("EndPoint full path:[{}]", arguments.getRequestEndPoint().getFullUrl());
        return arguments.getRequestEndPoint();
    }
}
