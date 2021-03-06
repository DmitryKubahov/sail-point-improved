package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.certification.CertificationAutomaticClosingRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

/**
 * Simple implementation of {@link CertificationAutomaticClosingRule} rule
 */
@Slf4j
@Rule(value = "Simple certification automatic closing rule", type = sailpoint.object.Rule.Type.CertificationAutomaticClosing)
public class SimpleCertificationAutomaticClosingRule extends CertificationAutomaticClosingRule {

    /**
     * Log current certification by INFO and return it
     */
    @Override
    protected void internalExecuteNoneOutput(JavaRuleContext context,
                                             CertificationAutomaticClosingRuleArguments arguments) {
        log.info("Certification:[{}]", arguments.getCertification());
    }
}
