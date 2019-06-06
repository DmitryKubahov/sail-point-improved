package com.sailpoint.improved.rule.login;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Provides the logic for mapping the assertion details provided by the identity provider
 * to an Identity for sign-on to IdentityIQ. SAML SSO was introduced in version 6.3 of IdentityIQ, so this rule
 * applies to 6.3+ versions.
 * <p>
 * Output:
 * Specifies the Identity or the Link matched to the information passed in the assertionAttributes
 * <p>
 * NOTE: As with the SSOAuthentication rule, the rule must return a Link (account) instead of an Identity when
 * implementing Electronic Signatures with SSO authentication because this rule is used to validate the user for
 * recording their electronic signature as well as for initial sign-on.
 *
 * @param <T> {@link sailpoint.object.Identity} or {@link sailpoint.object.Link}
 */
@Slf4j
public abstract class SAMLCorrelationRule<T>
        extends AbstractJavaRuleExecutor<T, SAMLCorrelationRule.SAMLCorrelationRuleArguments> {

    /**
     * Name of assertionAttributes argument name
     */
    public static final String ARG_ASSERTION_ATTRIBUTES = "assertionAttributes";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            SAMLCorrelationRule.ARG_ASSERTION_ATTRIBUTES
    );

    /**
     * Default constructor
     */
    public SAMLCorrelationRule() {
        super(Rule.Type.SAMLCorrelation.name(), SAMLCorrelationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected SAMLCorrelationRule.SAMLCorrelationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return SAMLCorrelationRuleArguments.builder()
                .assertionAttributes((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SAMLCorrelationRule.ARG_ASSERTION_ATTRIBUTES))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - assertionAttributes
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class SAMLCorrelationRuleArguments {
        /**
         * A map of (string) key-value pairs provided by the Identity Provider;
         * will always contain a key NameId (value is the name Id sent by the Identity Provider)
         * and any other SAML assertion attributes
         */
        @Argument(name = SAMLCorrelationRule.ARG_ASSERTION_ATTRIBUTES)
        private final Map<String, Object> assertionAttributes;
    }
}
