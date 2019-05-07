package com.sailpoint.rule.aggregation;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.aggregation.CorrelationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.Identity;
import sailpoint.object.ResourceObject;
import sailpoint.workflow.IdentityLibrary;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation of {@link CorrelationRule} rule
 */
@Slf4j
@Rule(value = "Simple correlation rule", type = sailpoint.object.Rule.Type.Correlation)
public class SimpleCorrelationRule extends CorrelationRule {

    /**
     * Pattern for concatenation lastName and firstName of identity
     */
    private static final String IDENTITY_NAME_PATTERN = "{0}.{1}";

    /**
     * Concatenates a {@link Identity#ATT_LASTNAME} and {@link Identity#ATT_FIRSTNAME}
     * field from the account (resourceObject) to build an Identity name for matching to an existing Identity
     */
    @Override
    @Argument(name = "map", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(SailPointContext context,
                                                  CorrelationRuleArguments arguments) {
        Map<String, Object> returnMap = new HashMap<>();
        ResourceObject account = arguments.getAccount();
        String firstName = account.getStringAttribute(Identity.ATT_FIRSTNAME);
        String lastName = account.getStringAttribute(Identity.ATT_LASTNAME);
        if ((firstName != null) && (lastName != null)) {
            returnMap.put(IdentityLibrary.ARG_IDENTITY_NAME,
                    MessageFormat.format(IDENTITY_NAME_PATTERN, firstName, lastName));
        }
        return returnMap;
    }
}
