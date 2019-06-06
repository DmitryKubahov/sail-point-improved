package com.sailpoint.improved.rule.miscellaneous;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.ScopeService;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.QueryInfo;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Specifies a filter that is used in determining the objects that a given user can
 * request for the population of users over which he has request authority in the Lifecycle Manager component of
 * IdentityIQ. These are specified as the “Object Request Authority” rules that determine the list of Roles,
 * Applications, and Managed Entitlements visible to a user in the LCM access request windows.
 * The scopeService class offers convenience methods for creating QueryOptions objects that filter the object lists
 * by matching an Identity’s assigned scope or controlled scopes. These are accessible by to rules that import the
 * {@link ScopeService} class. The methods are:
 * - {@link ScopeService#getAssignedScopeQueryInfo(Identity)}
 * - {@link ScopeService#getControlledScopesQueryInfo(Identity)}
 * <p>
 * Output:
 * The QueryInfo object containing the filter to be applied to the object list
 */
@Slf4j
public abstract class RequestObjectSelectorRule
        extends AbstractJavaRuleExecutor<QueryInfo, RequestObjectSelectorRule.RequestObjectSelectorRuleArguments> {

    /**
     * Name of requestor argument name
     */
    public static final String ARG_REQUESTOR = "requestor";
    /**
     * Name of requestee argument name
     */
    public static final String ARG_REQUESTEE = "requestee";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            RequestObjectSelectorRule.ARG_REQUESTOR,
            RequestObjectSelectorRule.ARG_REQUESTEE
    );

    /**
     * Default constructor
     */
    public RequestObjectSelectorRule() {
        super(Rule.Type.RequestObjectSelector.name(), RequestObjectSelectorRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected RequestObjectSelectorRule.RequestObjectSelectorRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return RequestObjectSelectorRuleArguments.builder()
                .requestor((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, RequestObjectSelectorRule.ARG_REQUESTOR))
                .requestee((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, RequestObjectSelectorRule.ARG_REQUESTEE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - requestor
     * - requestee
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class RequestObjectSelectorRuleArguments {
        /**
         * Identity object for the user who is making LCM access requests
         */
        @Argument(name = RequestObjectSelectorRule.ARG_REQUESTOR)
        private final Identity requestor;
        /**
         * Identity object representing the user on whose behalf the request is being made (the person to
         * whom the requested access will be granted); only applicable when the request is being processed for a
         * single user; null when multiple users are specified
         */
        @Argument(name = RequestObjectSelectorRule.ARG_REQUESTEE)
        private final Identity requestee;
    }
}
