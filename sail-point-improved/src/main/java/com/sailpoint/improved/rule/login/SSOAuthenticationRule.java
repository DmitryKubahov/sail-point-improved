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

/**
 * Specifies how the user is authenticated and matched to an Identity for sign-on to
 * IdentityIQ. Writing this rule is the only action required to implement rule-based single sign-on with IdentityIQ.
 * Version 6.1 introduced the option of returning a Link (account) from this rule instead of an Identity; this option
 * must be used when implementing Electronic Signatures with SSO authentication because this rule is used to
 * validate the user for recording their electronic signature as well as for initial sign-on.
 * <p>
 * Output:
 * Specifies the Identity or the Link matched to the information passed in the header
 *
 * @param <T> {@link sailpoint.object.Identity} or {@link sailpoint.object.Link}
 */
@Slf4j
public abstract class SSOAuthenticationRule<T>
        extends AbstractJavaRuleExecutor<T, SSOAuthenticationRule.SSOAuthenticationRuleArguments> {

    /**
     * Name of httpRequest argument name
     */
    public static final String ARG_HTTP_REQUEST = "httpRequest";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            SSOAuthenticationRule.ARG_HTTP_REQUEST
    );

    /**
     * Default constructor
     */
    public SSOAuthenticationRule() {
        super(Rule.Type.SSOAuthentication.name(), SSOAuthenticationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected SSOAuthenticationRule.SSOAuthenticationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return SSOAuthenticationRuleArguments.builder()
                .httpRequest((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SSOAuthenticationRule.ARG_HTTP_REQUEST))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - httpRequest
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class SSOAuthenticationRuleArguments {
        /**
         * Contains header information, including the user’s token from the SSO system
         */
        @Argument(name = SSOAuthenticationRule.ARG_HTTP_REQUEST)
        private final String httpRequest;
    }
}
