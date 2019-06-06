package com.sailpoint.improved.rule.miscellaneous;

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
 * When risk is associated with specific Identity attributes, custom risk score components can be created to reflect
 * that risk in the identity risk scores. Scoring for these Identity-attribute-based components can either be based
 * on a score assigned to a particular Identity attribute value or be calculated through a RiskScore rule. The
 * resultant score is factored into the identity risk score based on the “weight” assigned to the component in the
 * composite score for the Identity.
 * <p>
 * Output:
 * Value of the risk score to assign for the Identity Attribute score
 */
@Slf4j
public abstract class RiskScoreRule
        extends AbstractJavaRuleExecutor<Integer, RiskScoreRule.RiskScoreRuleArguments> {

    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY = "identity";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            RiskScoreRule.ARG_IDENTITY
    );

    /**
     * Default constructor
     */
    public RiskScoreRule() {
        super(Rule.Type.RiskScore.name(), RiskScoreRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected RiskScoreRule.RiskScoreRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return RiskScoreRuleArguments.builder()
                .identity((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, RiskScoreRule.ARG_IDENTITY))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - identity
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class RiskScoreRuleArguments {
        /**
         * Reference to the Identity being scored
         */
        @Argument(name = RiskScoreRule.ARG_IDENTITY)
        private final Identity identity;
    }
}
