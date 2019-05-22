package com.sailpoint.improved.rule.notification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Attributes;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Notifiable;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Used to specify additional email recipients for certification reminder notifications,
 * escalation notifications, and escalation reminder notifications.
 * <p>
 * Output:
 * The identity name or names to whom the email should be sent
 */
@Slf4j
public abstract class EmailRecipientRule
        extends AbstractJavaRuleExecutor<List<String>, EmailRecipientRule.EmailRecipientRuleArguments> {

    /**
     * Name of item argument name
     */
    public static final String ARG_ITEM_NAME = "item";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            EmailRecipientRule.ARG_ITEM_NAME
    );

    /**
     * Default constructor
     */
    public EmailRecipientRule() {
        super(Rule.Type.EmailRecipient.name(), EmailRecipientRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected EmailRecipientRule.EmailRecipientRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return EmailRecipientRuleArguments.builder()
                .item((Notifiable) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, EmailRecipientRule.ARG_ITEM_NAME))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - item
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class EmailRecipientRuleArguments {
        /**
         * The Notifiable interface for objects that can be reminded, escalated, and expired
         */
        @Argument(name = EmailRecipientRule.ARG_ITEM_NAME)
        private final Notifiable item;
    }
}
