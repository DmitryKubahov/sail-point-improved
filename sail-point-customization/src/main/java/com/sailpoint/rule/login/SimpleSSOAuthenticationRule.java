package com.sailpoint.rule.login;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.login.SSOAuthenticationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.GeneralException;

/**
 * Simple implementation of {@link SSOAuthenticationRule} rule
 */
@Slf4j
@Rule(value = "Simple SSO authentication rule", type = sailpoint.object.Rule.Type.SSOAuthentication)
public class SimpleSSOAuthenticationRule extends SSOAuthenticationRule<Identity> {

    /**
     * Log current http request and return identity by 'spadmin' name
     */
    @Override
    @Argument(name = "identity", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Identity internalExecute(JavaRuleContext context,
                                       SSOAuthenticationRuleArguments arguments) throws GeneralException {
        log.info("Current http request:[{}]", arguments.getHttpRequest());
        return context.getContext().getObjectByName(Identity.class, "spadmin");
    }
}
