package com.sailpoint.rule.aggregation;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.aggregation.AccountSelectorRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.tools.Util;

/**
 * Simple implementation of {@link AccountSelectorRule} rule
 */
@Slf4j
@Rule(value = "Simple account selector rule", type = sailpoint.object.Rule.Type.AccountSelector)
public class SimpleAccountSelectorRule extends AccountSelectorRule<Object> {

    /**
     * app2_privileged argument name
     */
    private static final String APP2_PRIVILEGED = "app2_privileged";

    /**
     * For requests with a source of UI or LCM, it returns a null so the user will be prompted to select an account in the UI.
     * Otherwise, it look for and returns the first non-privileged account Link found, if one exists for the user. (The
     * app2_privileged attribute on the target application designates whether the account is privileged or not.)
     */
    @Override
    @Argument(name = "selection", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Object internalExecute(SailPointContext context,
                                     AccountSelectorRuleArguments arguments) {

        return Util.isEmpty(arguments.getLinks()) ? null :
                arguments.getLinks().stream()
                        .filter(link -> Boolean.FALSE.toString().equals(link.getAttribute(APP2_PRIVILEGED))).findFirst()
                        .orElse(null);
    }
}
