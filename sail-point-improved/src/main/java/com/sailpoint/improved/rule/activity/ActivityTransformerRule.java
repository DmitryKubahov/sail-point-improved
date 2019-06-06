package com.sailpoint.improved.rule.activity;

import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.ApplicationActivity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Collections;

/**
 * Manipulates the activity data read from the activity data source to transform it into
 * the ApplicationActivity format that is required for IdentityIQ to record it. The activity data is passed to this rule
 * in different ways, depending on the activity data source and activity collector used.
 * <p>
 * Output:
 * The ApplicationActivity object that represents the activity
 */
@Slf4j
public abstract class ActivityTransformerRule
        extends AbstractJavaRuleExecutor<ApplicationActivity, ActivityTransformerRule.ActivityTransformerRuleArguments> {

    /**
     * Default constructor
     */
    public ActivityTransformerRule() {
        super(Rule.Type.ActivityTransformer.name(), Collections.emptyList());
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected ActivityTransformerRule.ActivityTransformerRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return ActivityTransformerRuleArguments.builder().build();
    }

    /**
     * Inputs (in addition to the common arguments): Variable based on the collector type
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class ActivityTransformerRuleArguments {
    }
}
