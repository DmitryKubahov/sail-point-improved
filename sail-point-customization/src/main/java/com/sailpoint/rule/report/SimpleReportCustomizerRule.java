package com.sailpoint.rule.report;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.report.ReportCustomizerRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

/**
 * Simple implementation of {@link ReportCustomizerRule} rule
 */
@Slf4j
@Rule(value = "Simple report customizer rule", type = sailpoint.object.Rule.Type.ReportCustomizer)
public class SimpleReportCustomizerRule extends ReportCustomizerRule {

    /**
     * Log current live report
     */
    @Override
    protected void internalExecuteNoneOutput(JavaRuleContext context,
                                             ReportCustomizerRuleArguments arguments) {
        log.info("Lice report:[{}]", arguments.getReport());
    }
}
