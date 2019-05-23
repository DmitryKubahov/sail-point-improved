package com.sailpoint.improved.rule.form;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Field;
import sailpoint.object.Form;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Specifies the set of values to display in the drop-down list in a listbox presented on a
 * provisioning policy or other form.
 * <p>
 * Output:
 * Object (possibly a collection) containing the allowed values for a given field
 */
@Slf4j
public abstract class AllowedValuesRule
        extends AbstractJavaRuleExecutor<Object, AllowedValuesRule.AllowedValuesRuleArguments> {

    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";
    /**
     * Name of form argument name
     */
    public static final String ARG_FORM = "form";
    /**
     * Name of field argument name
     */
    public static final String ARG_FIELD = "field";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            AllowedValuesRule.ARG_IDENTITY,
            AllowedValuesRule.ARG_FORM,
            AllowedValuesRule.ARG_FIELD
    );

    /**
     * Default constructor
     */
    public AllowedValuesRule() {
        super(Rule.Type.AllowedValues.name(), AllowedValuesRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected AllowedValuesRule.AllowedValuesRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return AllowedValuesRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AllowedValuesRule.ARG_IDENTITY))
                .form((Form) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AllowedValuesRule.ARG_FORM))
                .field((Field) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, AllowedValuesRule.ARG_FIELD))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     * - form
     * - field
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class AllowedValuesRuleArguments {
        /**
         * Reference to the Identity to whom the provisioning policy or form applies
         */
        @Argument(name = AllowedValuesRule.ARG_IDENTITY)
        private final Identity identity;
        /**
         * Reference to the form object holding the field where the allowed values are being set;
         * for provisioning policies, this is a form built at run-time based on the Template
         */
        @Argument(name = AllowedValuesRule.ARG_FORM)
        private final Form form;
        /**
         * Reference to the field object in which the allowed
         */
        @Argument(name = AllowedValuesRule.ARG_FIELD)
        private final Field field;
    }
}
