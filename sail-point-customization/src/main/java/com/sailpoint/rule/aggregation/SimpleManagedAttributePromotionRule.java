package com.sailpoint.rule.aggregation;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.aggregation.ManagedAttributePromotionRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

/**
 * Simple implementation of {@link ManagedAttributePromotionRule} rule
 */
@Slf4j
@Rule(value = "Simple managed attribute promotion rule", type = sailpoint.object.Rule.Type.ManagedAttributePromotion)
public class SimpleManagedAttributePromotionRule extends ManagedAttributePromotionRule {

    /**
     * Log managed attribute promotion rule by INFO level
     */
    @Override
    protected void internalExecuteNoneOutput(JavaRuleContext context,
                                             ManagedAttributePromotionRuleArguments arguments) {
        log.info("Managed attribute promotion rule:[{}]", arguments.getAttribute());
    }
}
