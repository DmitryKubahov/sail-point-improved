package com.sailpoint.rule.certification;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.certification.CertifierRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Simple implementation of {@link CertifierRule} rule
 */
@Slf4j
@Rule(value = "Simple certifier rule", type = sailpoint.object.Rule.Type.Certifier)
public class SimpleCertifierRule extends CertifierRule<List<String>> {

    /**
     * Log current group by INFO level and return random list of strings
     */
    @Override
    @Argument(name = "certifiers", type = ArgumentType.RETURNS, isReturnsType = true)
    protected List<String> internalExecute(JavaRuleContext context,
                                           CertifierRuleArguments arguments) {

        log.info("Group:[{}]", arguments.getGroup());
        return Collections.singletonList(UUID.randomUUID().toString());
    }
}
