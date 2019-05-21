package com.sailpoint.improved.rule.report;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractNoneOutputJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.LiveReport;
import sailpoint.object.Rule;
import sailpoint.object.TaskDefinition;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * A ReportCustomizer rule runs during report-UI rendering to create a dynamically-built form for report-filter
 * specification. Out of the box, many of SailPoint’s out of the box reports allow filtering based on any editable
 * identity attribute, including extended attributes. Since extended attributes are customer-defined, the form for
 * specifying these attributes as filters necessarily cannot be shipped complete and therefore must be built
 * dynamically.
 * <p>
 * Output:
 * None; the rule’s logic is should directly perform the necessary updates to objects; in the out of the box
 * examples, the rules add attributes for the ReportingLibrary class to use in rendering the filter form.
 */
@Slf4j
public abstract class ReportCustomizerRule
        extends AbstractNoneOutputJavaRuleExecutor<ReportCustomizerRule.ReportCustomizerRuleArguments> {

    /**
     * Name of taskDefinition argument name
     */
    public static final String ARG_TASK_DEFINITION_NAME = "taskDefinition";
    /**
     * Name of application argument name
     */
    public static final String ARG_REPORT_NAME = "report";
    /**
     * Name of locale argument name
     */
    public static final String ARG_LOCALE_NAME = "locale";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ReportCustomizerRule.ARG_TASK_DEFINITION_NAME,
            ReportCustomizerRule.ARG_REPORT_NAME,
            ReportCustomizerRule.ARG_LOCALE_NAME
    );

    /**
     * Default constructor
     */
    public ReportCustomizerRule() {
        super(Rule.Type.ReportCustomizer.name(), ReportCustomizerRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ReportCustomizerRule.ReportCustomizerRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ReportCustomizerRuleArguments.builder()
                .taskDefinition((TaskDefinition) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ReportCustomizerRule.ARG_TASK_DEFINITION_NAME))
                .report((LiveReport) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ReportCustomizerRule.ARG_REPORT_NAME))
                .locale((Locale) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ReportCustomizerRule.ARG_LOCALE_NAME))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - taskDefinition
     * - report
     * - locale
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ReportCustomizerRuleArguments {
        /**
         * Reference to the taskDefinition object that contains the report definition
         */
        @Argument(name = ReportCustomizerRule.ARG_TASK_DEFINITION_NAME)
        private final TaskDefinition taskDefinition;
        /**
         * The report definition itself
         */
        @Argument(name = ReportCustomizerRule.ARG_REPORT_NAME)
        private final LiveReport report;
        /**
         * Represents a specific geographical, political, or cultural region; used for locale-specific rendering/calculations (e.g. number formatting)
         */
        @Argument(name = ReportCustomizerRule.ARG_LOCALE_NAME)
        private final Locale locale;
    }
}
