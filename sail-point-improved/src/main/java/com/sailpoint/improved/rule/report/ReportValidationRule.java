package com.sailpoint.improved.rule.report;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Form;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.LiveReport;
import sailpoint.object.Rule;
import sailpoint.tools.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Used to perform report-specific form validation in case where the single field
 * validation option available in forms does not meet the needs of the report. This can be specified as a rule
 * (ValidationRule) or as a script (ValidationScript) embedded within the LiveReport definition.
 * <p>
 * Output:
 * List of messages indicating where/why validation has failed; list should be returned as null
 * or empty if the form entries pass validation
 */
@Slf4j
public abstract class ReportValidationRule
        extends AbstractJavaRuleExecutor<List<Message>, ReportValidationRule.ReportValidationRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_REPORT_NAME = "report";
    /**
     * Name of form argument name
     */
    public static final String ARG_FORM_NAME = "form";
    /**
     * Name of locale argument name
     */
    public static final String ARG_LOCALE_NAME = "locale";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ReportValidationRule.ARG_REPORT_NAME,
            ReportValidationRule.ARG_FORM_NAME,
            ReportValidationRule.ARG_LOCALE_NAME
    );

    /**
     * Default constructor
     */
    public ReportValidationRule() {
        super(Rule.Type.ReportValidator.name(), ReportValidationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ReportValidationRule.ReportValidationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ReportValidationRuleArguments.builder()
                .report((LiveReport) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ReportValidationRule.ARG_REPORT_NAME))
                .form((Form) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ReportValidationRule.ARG_FORM_NAME))
                .locale((Locale) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ReportValidationRule.ARG_LOCALE_NAME))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - report
     * - form
     * - locale
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ReportValidationRuleArguments {
        /**
         * The report definition itself
         */
        @Argument(name = ReportValidationRule.ARG_REPORT_NAME)
        private final LiveReport report;
        /**
         * Form for rendering parameter UI, requiring validation of user input
         */
        @Argument(name = ReportValidationRule.ARG_FORM_NAME)
        private final Form form;
        /**
         * Represents a specific geographical, political, or cultural region; used for locale-specific rendering/calculations (e.g. number formatting)
         */
        @Argument(name = ReportValidationRule.ARG_LOCALE_NAME)
        private final Locale locale;
    }
}
