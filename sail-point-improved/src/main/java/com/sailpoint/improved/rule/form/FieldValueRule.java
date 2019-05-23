package com.sailpoint.improved.rule.form;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Sets the default value for a form field. In a provisioning policy, fields that are assigned a
 * value with a rule (or any other method) are usually not presented to a user on a data-gathering form, so this
 * becomes the defined value for the field, not a just default that can be overridden.
 * <p>
 * Output:
 * The value to set for the field
 */
@Slf4j
public abstract class FieldValueRule
        extends AbstractJavaRuleExecutor<Object, FieldValueRule.FieldValueRuleArguments> {

    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            FieldValueRule.ARG_IDENTITY
    );

    /**
     * Default constructor
     */
    public FieldValueRule() {
        super(Rule.Type.FieldValue.name(), FieldValueRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected FieldValueRule.FieldValueRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return FieldValueRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, FieldValueRule.ARG_IDENTITY))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class FieldValueRuleArguments {
        /**
         * Reference to the Identity for whom the field value is being set
         */
        @Argument(name = FieldValueRule.ARG_IDENTITY)
        private final Identity identity;
    }
}
