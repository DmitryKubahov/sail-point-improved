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
 * Runs on every page change as a user navigates through IdentityIQ; it validates
 * the session with the SSO provider by examining the headers through the httpRequest. This frequent validation
 * provides an added measure of verification for those clients with extraordinary concerns for security, but it can
 * impact system performance. If the SSO Validation Rule cannot verify a valid SSO session it logouts the user and
 * displays an error message (as specified by the rule creator).
 * <p>
 * Output:
 * Returns the error message to display, indicating that the validation failed; returns null if validation was successful
 */
@Slf4j
public abstract class SSOValidationRule
        extends AbstractJavaRuleExecutor<String, SSOValidationRule.SSOValidationRuleArguments> {

    /**
     * Name of httpRequest argument name
     */
    public static final String ARG_HTTP_REQUEST = "httpRequest";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            SSOValidationRule.ARG_HTTP_REQUEST
    );

    /**
     * Default constructor
     */
    public SSOValidationRule() {
        super(Rule.Type.SSOValidation.name(), SSOValidationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected SSOValidationRule.SSOValidationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return SSOValidationRuleArguments.builder()
                .httpRequest((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, SSOValidationRule.ARG_HTTP_REQUEST))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - httpRequest
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class SSOValidationRuleArguments {
        /**
         * Contains header information, including the user’s token from the SSO system
         */
        @Argument(name = SSOValidationRule.ARG_HTTP_REQUEST)
        private final String httpRequest;
    }
}
