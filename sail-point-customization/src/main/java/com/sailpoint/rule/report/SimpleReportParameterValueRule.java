package com.sailpoint.rule.report;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.report.ReportParameterValueRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

/**
 * Simple implementation of {@link ReportParameterValueRule} rule
 */
@Slf4j
@Rule(value = "Simple report parameter value rule", type = sailpoint.object.Rule.Type.ReportParameterValue)
public class SimpleReportParameterValueRule extends ReportParameterValueRule {

    /**
     * Log current value and return it
     */
    @Override
    @Argument(name = "result", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Object internalExecute(JavaRuleContext context,
                                     ReportParameterValueRuleArguments arguments) {
        log.info("Report parameter value:[{}]", arguments.getValue());
        return arguments.getValue();
    }
}
