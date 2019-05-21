package com.sailpoint.improved.rule.report;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Attributes;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.QueryOptions;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Used to specify any custom filter for the report and add it into the queryOptions
 * object that is used in the datasource filter. This is an alternative to a QueryScript (embedded within the report
 * definition). Parameters using a QueryRule do not need to specify a property because the queryRule overrides
 * any property on the parameter; the argument specified on the parameter can be accessed within the rule
 * through the “value” variable.
 * <p>
 * Output:
 * Updated QueryOptions object to use as report filter
 */
@Slf4j
public abstract class ReportParameterQueryRule
        extends AbstractJavaRuleExecutor<QueryOptions, ReportParameterQueryRule.ReportParameterQueryRuleArguments> {

    /**
     * Name of value argument name
     */
    public static final String ARG_VALUE_NAME = "value";
    /**
     * Name of arguments argument name
     */
    public static final String ARG_ARGUMENTS_NAME = "arguments";
    /**
     * Name of queryOptions argument name
     */
    public static final String ARG_QUERY_OPTIONS_NAME = "queryOptions";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ReportParameterQueryRule.ARG_VALUE_NAME,
            ReportParameterQueryRule.ARG_ARGUMENTS_NAME,
            ReportParameterQueryRule.ARG_QUERY_OPTIONS_NAME
    );

    /**
     * Default constructor
     */
    public ReportParameterQueryRule() {
        super(Rule.Type.ReportParameterQuery.name(), ReportParameterQueryRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ReportParameterQueryRule.ReportParameterQueryRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ReportParameterQueryRuleArguments.builder()
                .value(JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ReportParameterQueryRule.ARG_VALUE_NAME))
                .arguments((Attributes) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ReportParameterQueryRule.ARG_ARGUMENTS_NAME))
                .queryOptions((QueryOptions) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ReportParameterQueryRule.ARG_QUERY_OPTIONS_NAME))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - value
     * - arguments
     * - queryOptions
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ReportParameterQueryRuleArguments {
        /**
         * Parameter value, as specified in report form
         */
        @Argument(name = ReportParameterQueryRule.ARG_VALUE_NAME)
        private final Object value;
        /**
         * Report arguments map
         */
        @Argument(name = ReportParameterQueryRule.ARG_ARGUMENTS_NAME)
        private final Attributes arguments;
        /**
         * Updated QueryOptions object to use as report filter
         */
        @Argument(name = ReportParameterQueryRule.ARG_QUERY_OPTIONS_NAME)
        private final QueryOptions queryOptions;
    }
}
