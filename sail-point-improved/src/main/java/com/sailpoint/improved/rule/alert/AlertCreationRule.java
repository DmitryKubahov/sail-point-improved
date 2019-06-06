package com.sailpoint.improved.rule.alert;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Alert;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Runs during alert aggregation, as the alert record is being created in IdentityIQ. It is
 * primarily used for populating attributes of the Alert record that are not auto-populated by the connector.
 * Common examples include the Display Name or Alert Date attributes which will be included in the summary
 * data shown on the Alerts list in the UI if populated.
 * <p>
 * NOTE: If the AlertCreation rule returns null, this will cause the alert aggregation process to skip the alert (i.e. not
 * create it in IdentityIQ).
 * <p>
 * Output:
 * The alert object being created (with any updates made by the rule)
 */
@Slf4j
public abstract class AlertCreationRule
        extends AbstractJavaRuleExecutor<Alert, AlertCreationRule.AlertCreationRuleArguments> {

    /**
     * Name of application argument name
     */
    public static final String ARG_APPLICATION = "application";
    /**
     * Name of alert argument name
     */
    public static final String ARG_ALERT = "alert";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            AlertCreationRule.ARG_APPLICATION,
            AlertCreationRule.ARG_ALERT
    );

    /**
     * Default constructor
     */
    public AlertCreationRule() {
        super(Rule.Type.AlertCreation.name(), AlertCreationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected AlertCreationRule.AlertCreationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return AlertCreationRuleArguments.builder()
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AlertCreationRule.ARG_APPLICATION))
                .alert((Alert) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AlertCreationRule.ARG_ALERT))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - application
     * - alert
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class AlertCreationRuleArguments {
        /**
         * The application object from which the alert was aggregated
         */
        @Argument(name = AlertCreationRule.ARG_APPLICATION)
        private final Application application;
        /**
         * The alert object to be evaluated against the rule’s conditions to determine if the alertDefinition’s response should be executed or not
         */
        @Argument(name = AlertCreationRule.ARG_ALERT)
        private final Alert alert;
    }
}
