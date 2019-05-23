package com.sailpoint.improved.rule.form;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Application;
import sailpoint.object.Field;
import sailpoint.object.Form;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.tools.Message;

import java.util.Arrays;
import java.util.List;

/**
 * Examines a Field value and determines whether it is valid, as specified in the rule logic. If it is
 * not valid, one or more messages are returned from the rule; if the field value is valid, the rule should return null.
 * When messages are returned from the rule, the form is reloaded for the user to correct the error and the
 * messages are displayed on it.
 * <p>
 * Output:
 * List of messages from the validation process; IdentityIQ can process a Message, string, or a collection of Messages
 * or strings as the return value from this rule If any non-null value is returned, this means validation has
 * failed.
 */
@Slf4j
public abstract class ValidationRule
        extends AbstractJavaRuleExecutor<List<Message>, ValidationRule.ValidationRuleArguments> {

    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";
    /**
     * Name of app argument name
     */
    public static final String ARG_APPLICATION = "app";
    /**
     * Name of form argument name
     */
    public static final String ARG_FORM = "form";
    /**
     * Name of field argument name
     */
    public static final String ARG_FIELD = "field";
    /**
     * Name of value argument name
     */
    public static final String ARG_VALUE = "value";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            ValidationRule.ARG_IDENTITY,
            ValidationRule.ARG_APPLICATION,
            ValidationRule.ARG_FORM,
            ValidationRule.ARG_FIELD
    );

    /**
     * Default constructor
     */
    public ValidationRule() {
        super(Rule.Type.Validation.name(), ValidationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ValidationRule.ValidationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ValidationRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ValidationRule.ARG_IDENTITY))
                .application((Application) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ValidationRule.ARG_APPLICATION))
                .form((Form) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ValidationRule.ARG_FORM))
                .field((Field) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, ValidationRule.ARG_FIELD))
                .value(JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext, ValidationRule.ARG_VALUE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     * - app
     * - form
     * - field
     * - value
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ValidationRuleArguments {
        /**
         * Identity to whom the field value relates
         */
        @Argument(name = ValidationRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Reference to the Application object to which the Form applies
         */
        @Argument(name = ValidationRule.ARG_APPLICATION)
        private final Application application;
        /**
         * Reference to the Form object on which the Field exists
         */
        @Argument(name = ValidationRule.ARG_FORM)
        private final Form form;
        /**
         * Reference to the Field being validated
         */
        @Argument(name = ValidationRule.ARG_FIELD)
        private final Field field;
        /**
         * Object representing the field value
         */
        @Argument(name = ValidationRule.ARG_VALUE)
        private final Object value;
    }
}
