package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.certification.CertificationPreDelegationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Simple implementation of {@link CertificationPreDelegationRule} rule
 */
@Slf4j
@Rule(value = "Simple certification pre delegation rule", type = sailpoint.object.Rule.Type.CertificationPreDelegation)
public class SimpleCertificationPreDelegationRule extends CertificationPreDelegationRule {

    /**
     * Build random result map via {@link CertificationPreDelegationRule.RuleResult} builder
     */
    @Override
    @Argument(name = "map", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(SailPointContext context,
                                                  CertificationPreDelegationRuleArguments arguments) {

        log.info("Build random result map");
        return CertificationPreDelegationRule.RuleResult.builder()
                .certificationName(UUID.randomUUID().toString())
                .comments(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .reassign(new Random().nextBoolean())
                .recipientName(UUID.randomUUID().toString())
                .build().toMap();
    }
}
