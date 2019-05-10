package com.sailpoint.rule.report;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.report.ReportCustomizerRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.tools.GeneralException;

/**
 * Simple implementation of {@link ReportCustomizerRule} rule
 */
@Slf4j
@Rule(value = "Simple report customizer rule", type = sailpoint.object.Rule.Type.ReportCustomizer)
public class SimpleReportCustomizerRule extends ReportCustomizerRule {

    /**
     * Log current live report and return null
     */
    @Override
    protected Object internalExecute(SailPointContext context, ReportCustomizerRuleArguments arguments)
            throws GeneralException {
        log.info("Lice report:[{}]", arguments.getReport());
        return null;
    }
}
