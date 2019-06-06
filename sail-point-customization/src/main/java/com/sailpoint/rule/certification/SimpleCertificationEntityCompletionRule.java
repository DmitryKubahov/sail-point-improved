package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.certification.CertificationEntityCompletionRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.Message;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Simple implementation of {@link CertificationEntityCompletionRule} rule
 */
@Slf4j
@Rule(value = "Simple certification entity completion rule", type = sailpoint.object.Rule.Type.CertificationEntityCompletion)
public class SimpleCertificationEntityCompletionRule extends CertificationEntityCompletionRule<Message> {

    /**
     * Log current certification entity by INFO and return list with one random message
     */
    @Override
    @Argument(name = "messages", type = ArgumentType.RETURNS, isReturnsType = true)
    protected List<Message> internalExecute(JavaRuleContext context,
                                            CertificationEntityCompletionRuleArguments arguments) {

        log.info("Certification entity:[{}]", arguments.getCertificationEntity());
        return Collections.singletonList(Message.error(UUID.randomUUID().toString()));
    }
}
