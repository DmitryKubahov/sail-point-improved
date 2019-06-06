package com.sailpoint.rule.miscellaneous;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.miscellaneous.RiskScoreRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

import java.util.Random;

/**
 * Simple implementation of {@link RiskScoreRule} rule
 */
@Slf4j
@Rule(value = "Simple risk score rule", type = sailpoint.object.Rule.Type.RiskScore)
public class SimpleRiskScoreRule extends RiskScoreRule {

    /**
     * Log current identity and return random int value
     */
    @Override
    @Argument(name = "score", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Integer internalExecute(JavaRuleContext context,
                                      RiskScoreRuleArguments arguments) {
        log.info("Current identity:[{}]", arguments.getIdentity().getName());
        return new Random().nextInt();
    }
}
