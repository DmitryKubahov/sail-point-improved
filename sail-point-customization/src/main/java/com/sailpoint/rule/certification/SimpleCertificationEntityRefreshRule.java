package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.certification.CertificationEntityRefreshRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

/**
 * Simple implementation of {@link CertificationEntityRefreshRule} rule
 */
@Slf4j
@Rule(value = "Simple certification entity refresh rule", type = sailpoint.object.Rule.Type.CertificationEntityRefresh)
public class SimpleCertificationEntityRefreshRule extends CertificationEntityRefreshRule {

    /**
     * Log current certification entity by INFO and return it
     */
    @Override
    protected void internalExecuteNoneOutput(SailPointContext context,
                                             CertificationEntityRefreshRuleArguments arguments) {
        log.info("Certification entity:[{}]", arguments.getCertificationEntity());
    }
}
