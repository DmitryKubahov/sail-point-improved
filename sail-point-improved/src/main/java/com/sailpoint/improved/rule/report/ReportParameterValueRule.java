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
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Processed as “property = return value from ValueRule”. It performs processing
 * based on the argument’s value to return a different value that should be used in the criterion. In a ValueRule,
 * the argument is accessed through the variable name “value”. This is an alternative to a ValueScript embedded
 * within the report definition.
 * <p>
 * Output:
 * Value or list of values to use in query with the specified parameter
 */
@Slf4j
public abstract class ReportParameterValueRule
        extends AbstractJavaRuleExecutor<Object, ReportParameterValueRule.ReportParameterValueRuleArguments> {

    /**
     * Name of value argument name
     */
    public static final String ARG_VALUE_NAME = "value";
    /**
     * Name of arguments argument name
     */
    public static final String ARG_ARGUMENTS_NAME = "arguments";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ReportParameterValueRule.ARG_VALUE_NAME,
            ReportParameterValueRule.ARG_ARGUMENTS_NAME
    );

    /**
     * Default constructor
     */
    public ReportParameterValueRule() {
        super(Rule.Type.ReportParameterValue.name(), ReportParameterValueRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ReportParameterValueRule.ReportParameterValueRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ReportParameterValueRuleArguments.builder()
                .value(JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ReportParameterValueRule.ARG_VALUE_NAME))
                .arguments((Attributes) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ReportParameterValueRule.ARG_ARGUMENTS_NAME))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - value
     * - arguments
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ReportParameterValueRuleArguments {
        /**
         * Parameter value, as specified in report form
         */
        @Argument(name = ReportParameterValueRule.ARG_VALUE_NAME)
        private final Object value;
        /**
         * Report arguments map
         */
        @Argument(name = ReportParameterValueRule.ARG_ARGUMENTS_NAME)
        private final Attributes arguments;
    }
}
