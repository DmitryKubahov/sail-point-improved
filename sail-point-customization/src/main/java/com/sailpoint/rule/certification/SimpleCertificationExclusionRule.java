package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.certification.CertificationExclusionRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

import java.util.UUID;

/**
 * Simple implementation of {@link CertificationExclusionRule} rule
 */
@Slf4j
@Rule(value = "Simple certification exclusion rule", type = sailpoint.object.Rule.Type.CertificationExclusion)
public class SimpleCertificationExclusionRule extends CertificationExclusionRule {

    /**
     * Log current certification by INFO level and return random string
     */
    @Override
    @Argument(name = "explanation", type = ArgumentType.RETURNS, isReturnsType = true)
    protected String internalExecute(JavaRuleContext context,
                                     CertificationExclusionRule.CertificationExclusionRuleArguments arguments) {

        log.info("Certification:[{}]", arguments.getCertification());
        return UUID.randomUUID().toString();
    }
}
