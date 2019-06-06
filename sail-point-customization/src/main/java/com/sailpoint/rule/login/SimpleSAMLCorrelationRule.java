package com.sailpoint.rule.login;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.login.SAMLCorrelationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.GeneralException;

/**
 * Simple implementation of {@link SAMLCorrelationRule} rule
 */
@Slf4j
@Rule(value = "Simple SAML correlation rule", type = sailpoint.object.Rule.Type.SAMLCorrelation)
public class SimpleSAMLCorrelationRule extends SAMLCorrelationRule<Identity> {

    /**
     * Log current assertion attributes and return identity by 'spadmin' name
     */
    @Override
    @Argument(name = "identity", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Identity internalExecute(JavaRuleContext context,
                                       SAMLCorrelationRuleArguments arguments) throws GeneralException {
        log.info("Current assertion attributes:[{}]", arguments.getAssertionAttributes());
        return context.getContext().getObjectByName(Identity.class, "spadmin");
    }
}
