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
import sailpoint.object.SailPointObject;

import java.util.Arrays;
import java.util.List;

/**
 * Allows the alert to be associated to some other object in IdentityIQ. IdentityIQ allows
 * an alert to be correlated to any object, though this correlation typically maps to the identity who owns the
 * account which performed the action that generated the alert or to the account (Link) itself. The correlation
 * records the object name and ID on the alert, rather than attaching the alert to the object. This correlation is
 * used in the alert response – the object is provided to the email, workflow, or certification event that is triggered
 * for the alert by an AlertDefinition. The AlertCorrelation rule runs just before the AlertCreation rule.
 * <p>
 * Output:
 * Returns the object which should be correlated to the alert
 */
@Slf4j
public abstract class AlertCorrelationRule
        extends AbstractJavaRuleExecutor<SailPointObject, AlertCorrelationRule.AlertCorrelationRuleArguments> {

    /**
     * Name of source argument name
     */
    public static final String ARG_SOURCE = "source";
    /**
     * Name of alert argument name
     */
    public static final String ARG_ALERT = "alert";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            AlertCorrelationRule.ARG_SOURCE,
            AlertCorrelationRule.ARG_ALERT
    );

    /**
     * Default constructor
     */
    public AlertCorrelationRule() {
        super(Rule.Type.AlertCorrelation.name(), AlertCorrelationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected AlertCorrelationRule.AlertCorrelationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return AlertCorrelationRuleArguments.builder()
                .source((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AlertCorrelationRule.ARG_SOURCE))
                .alert((Alert) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AlertCorrelationRule.ARG_ALERT))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - source
     * - alert
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class AlertCorrelationRuleArguments {
        /**
         * The application object from which the alert was aggregated
         */
        @Argument(name = AlertCorrelationRule.ARG_SOURCE)
        private final Application source;
        /**
         * The alert object to be evaluated against the rule’s conditions to determine if the alertDefinition’s response should be executed or not
         */
        @Argument(name = AlertCorrelationRule.ARG_ALERT)
        private final Alert alert;
    }
}
