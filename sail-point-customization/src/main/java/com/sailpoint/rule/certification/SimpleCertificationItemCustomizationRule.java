package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.certification.CertificationItemCustomizationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

/**
 * Simple implementation of {@link CertificationItemCustomizationRule} rule
 */
@Slf4j
@Rule(value = "Simple certification item customization rule", type = sailpoint.object.Rule.Type.CertificationItemCustomization)
public class SimpleCertificationItemCustomizationRule extends CertificationItemCustomizationRule {

    /**
     * Log current certifiable entity and certification item by INFO and return item
     */
    @Override
    protected Object internalExecute(SailPointContext context,
                                     CertificationItemCustomizationRuleArguments arguments) {

        log.info("Certifiable entity:[{}]", arguments.getCertifiableEntity());
        log.info("Certifiable item:[{}]", arguments.getItem());
        return arguments.getItem();
    }
}
