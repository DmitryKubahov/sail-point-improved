package com.sailpoint.rule.report;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.report.ReportValidationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.Message;

import java.util.Collections;
import java.util.List;

/**
 * Simple implementation of {@link ReportValidationRule} rule
 */
@Slf4j
@Rule(value = "Simple report validation rule", type = sailpoint.object.Rule.Type.ReportValidator)
public class SimpleReportValidationRule extends ReportValidationRule {

    /**
     * Log current live report and form, Return empty list
     */
    @Override
    @Argument(name = "errors", type = ArgumentType.RETURNS, isReturnsType = true)
    protected List<Message> internalExecute(JavaRuleContext context,
                                            ReportValidationRuleArguments arguments) {
        log.info("Live report:[{}]", arguments.getReport());
        log.info("Report form:[{}]", arguments.getForm());
        return Collections.emptyList();
    }
}
