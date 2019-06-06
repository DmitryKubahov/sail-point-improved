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
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Specifies the desired response in IdentityIQ for a certain set of alerts read from an alert
 * source system (like SecurityIQ), and it includes logic for identifying which alerts should trigger the defined
 * response. An AlertMatch rule is one way to define that identifying logic (a set of Match Terms is the other).
 * <p>
 * Output:
 * Returns true if the alert should execute the alertDefinition’s response (i.e. it meets the identifying criteria for the specified response)
 */
@Slf4j
public abstract class AlertMatchRule
        extends AbstractJavaRuleExecutor<Boolean, AlertMatchRule.AlertMatchRuleArguments> {

    /**
     * Name of alert argument name
     */
    public static final String ARG_ALERT = "alert";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            AlertMatchRule.ARG_ALERT
    );

    /**
     * Default constructor
     */
    public AlertMatchRule() {
        super(Rule.Type.AlertMatch.name(), AlertMatchRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected AlertMatchRule.AlertMatchRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return AlertMatchRuleArguments.builder()
                .alert((Alert) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AlertMatchRule.ARG_ALERT))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - alert
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class AlertMatchRuleArguments {
        /**
         * The alert object to be evaluated against the rule’s conditions to determine if the alertDefinition’s response should be executed or not
         */
        @Argument(name = AlertMatchRule.ARG_ALERT)
        private final Alert alert;
    }
}
