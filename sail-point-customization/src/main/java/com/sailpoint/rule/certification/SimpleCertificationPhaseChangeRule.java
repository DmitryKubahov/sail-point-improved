package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.improved.rule.certification.CertificationPhaseChangeRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

/**
 * Simple implementation of {@link CertificationPhaseChangeRule} rule
 */
@Slf4j
@Rule(value = "Simple certification phase change rule", type = sailpoint.object.Rule.Type.CertificationPhaseChange)
public class SimpleCertificationPhaseChangeRule extends CertificationPhaseChangeRule {

    /**
     * Log current prev and next phase by INFO and return next phase
     */
    @Override
    protected Object internalExecute(SailPointContext context, CertificationPhaseChangeRuleArguments arguments) {

        log.info("Previous phase:[{}]", arguments.getPreviousPhase());
        log.info("Next phase:[{}]", arguments.getNextPhase());
        return arguments.getNextPhase();
    }
}
