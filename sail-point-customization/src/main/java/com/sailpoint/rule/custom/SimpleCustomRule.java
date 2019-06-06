package com.sailpoint.rule.custom;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractNoneOutputJavaRuleExecutor;
import com.sailpoint.improved.rule.alert.AlertCorrelationRule;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

import java.util.Collections;

/**
 * Simple implementation of custom rule
 */
@Slf4j
@Rule(value = "Simple custom rule")
public class SimpleCustomRule extends AbstractNoneOutputJavaRuleExecutor<SimpleCustomRule.CustomRuleArguments> {

    /**
     * Default constructor for rules without output
     */
    public SimpleCustomRule() {
        super("My custom rule type", Collections.emptyList());
    }

    /**
     * Create new argument container
     *
     * @param javaRuleContext - current rule context
     * @return new current custom rule argument
     */
    @Override
    protected CustomRuleArguments buildContainerArguments(JavaRuleContext javaRuleContext) {
        return CustomRuleArguments.builder().build();
    }


    /**
     * Do not do anything
     *
     * @param javaRuleContext    - java rule context
     * @param containerArguments - argument container for current rule
     */
    @Override
    protected void internalExecuteNoneOutput(JavaRuleContext javaRuleContext, CustomRuleArguments containerArguments) {

    }

    /**
     * Arguments container for custom task without parameters
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static final class CustomRuleArguments {

    }
}
