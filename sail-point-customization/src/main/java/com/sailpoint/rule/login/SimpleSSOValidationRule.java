package com.sailpoint.rule.login;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.login.SSOValidationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.GeneralException;

/**
 * Simple implementation of {@link SSOValidationRule} rule
 */
@Slf4j
@Rule(value = "Simple SSO validation rule", type = sailpoint.object.Rule.Type.SSOValidation)
public class SimpleSSOValidationRule extends SSOValidationRule {

    /**
     * Log current http request and return nmull
     */
    @Override
    @Argument(name = "resultMessage", type = ArgumentType.RETURNS, isReturnsType = true)
    protected String internalExecute(JavaRuleContext context,
                                     SSOValidationRuleArguments arguments) throws GeneralException {
        log.info("Current http request:[{}]", arguments.getHttpRequest());
        return null;
    }
}
