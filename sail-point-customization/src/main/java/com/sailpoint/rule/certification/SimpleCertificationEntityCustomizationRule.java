package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.certification.CertificationEntityCustomizationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

/**
 * Simple implementation of {@link CertificationEntityCustomizationRule} rule
 */
@Slf4j
@Rule(value = "Simple certification entity customization rule", type = sailpoint.object.Rule.Type.CertificationEntityCustomization)
public class SimpleCertificationEntityCustomizationRule extends CertificationEntityCustomizationRule {

    /**
     * Log current certifiable entity by INFO and return it
     */
    @Override
    protected void internalExecuteNoneOutput(SailPointContext context,
                                             CertificationEntityCustomizationRuleArguments arguments) {
        log.info("Certifiable entity:[{}]", arguments.getCertifiableEntity());
    }
}
