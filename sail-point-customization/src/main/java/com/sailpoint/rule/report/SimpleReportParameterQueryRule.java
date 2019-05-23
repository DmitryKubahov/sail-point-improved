package com.sailpoint.rule.report;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.report.ReportParameterQueryRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.QueryOptions;

/**
 * Simple implementation of {@link ReportParameterQueryRule} rule
 */
@Slf4j
@Rule(value = "Simple report parameter query rule", type = sailpoint.object.Rule.Type.ReportParameterQuery)
public class SimpleReportParameterQueryRule extends ReportParameterQueryRule {

    /**
     * Log current query options and return it
     */
    @Override
    @Argument(name = "queryOptions", type = ArgumentType.RETURNS, isReturnsType = true)
    protected QueryOptions internalExecute(JavaRuleContext context,
                                           ReportParameterQueryRuleArguments arguments) {
        log.info("Report query options:[{}]", arguments.getQueryOptions());
        return arguments.getQueryOptions();
    }
}
