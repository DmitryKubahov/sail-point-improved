package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.certification.CertificationSignOffApproverRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.workflow.IdentityLibrary;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * Simple implementation of {@link CertificationSignOffApproverRule} rule
 */
@Slf4j
@Rule(value = "Simple certification sign off approver rule", type = sailpoint.object.Rule.Type.CertificationSignOffApprover)
public class SimpleCertificationSignOffApproverRule extends CertificationSignOffApproverRule {

    /**
     * Log current certifier and return map with {@link IdentityLibrary#ARG_IDENTITY_NAME} random value
     */
    @Override
    @Argument(name = "results", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Map<String, Object> internalExecute(SailPointContext context,
                                                  CertificationSignOffApproverRuleArguments arguments) {

        log.info("Certifier:[{}]", arguments.getCertifier());
        return Collections.singletonMap(IdentityLibrary.ARG_IDENTITY_NAME, UUID.randomUUID().toString());
    }
}
