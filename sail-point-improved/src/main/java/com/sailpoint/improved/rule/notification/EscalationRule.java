package com.sailpoint.improved.rule.notification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Notifiable;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Identifies a new owner for a workItem or certification when an “escalation” of the item is
 * triggered. For a certification, this occurs when the certification has not been signed off and a triggering time
 * period or number of reminder notices is reached. For a workItem, this can be when an inactive owner is
 * detected or according to the notification schedule specified in the workItem’s notificationConfig.
 * <p>
 * NOTE: The notification configuration mechanism for certifications was updated in version 6.0 to allow more
 * flexibility in reminder notifications and escalations. Beginning with 6.0, each certification escalation only runs
 * once at the prescribed date and time, so the rule only needs to return one escalation recipient. Subsequent
 * escalations can be configured to return a different recipient using the same or a different rule. This can simplify
 * the logic required in any given certification escalation rule, since a single escalation rule is no longer required to
 * manage escalation through a chain of people.
 * <p>
 * Output:
 * The identity name to whom the item is being assigned in escalation
 */
@Slf4j
public abstract class EscalationRule
        extends AbstractJavaRuleExecutor<String, EscalationRule.EscalationRuleArguments> {

    /**
     * Name of item argument name
     */
    public static final String ARG_ITEM = "item";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            EscalationRule.ARG_ITEM
    );

    /**
     * Default constructor
     */
    public EscalationRule() {
        super(Rule.Type.Escalation.name(), EscalationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected EscalationRule.EscalationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return EscalationRuleArguments.builder()
                .item((Notifiable) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, EscalationRule.ARG_ITEM))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - item
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class EscalationRuleArguments {
        /**
         * The Notifiable interface for the object (work item or certification) being escalated
         */
        @Argument(name = EscalationRule.ARG_ITEM)
        private final Notifiable item;
    }
}
