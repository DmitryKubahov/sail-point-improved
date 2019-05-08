package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.certification.CertificationItemCompletionRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

/**
 * Simple implementation of {@link CertificationItemCompletionRule} rule
 */
@Slf4j
@Rule(value = "Simple certification item completion rule", type = sailpoint.object.Rule.Type.CertificationItemCompletion)
public class SimpleCertificationItemCompletionRule extends CertificationItemCompletionRule {

    /**
     * Log current item and entity by INFO and return true
     */
    @Override
    @Argument(name = "complete", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Boolean internalExecute(SailPointContext context,
                                      CertificationItemCompletionRuleArguments arguments) {

        log.info("Item:[{}]", arguments.getItem());
        log.info("Entity:[{}]", arguments.getEntity());
        return Boolean.TRUE;
    }
}
