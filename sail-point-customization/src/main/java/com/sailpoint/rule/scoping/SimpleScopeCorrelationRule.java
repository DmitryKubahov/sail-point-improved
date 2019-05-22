package com.sailpoint.rule.scoping;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.scoping.ScopeCorrelationRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointContext;
import sailpoint.object.Scope;

import java.util.Collections;
import java.util.List;

/**
 * Simple implementation of {@link ScopeCorrelationRule} rule
 */
@Slf4j
@Rule(value = "Simple scope correlation rule", type = sailpoint.object.Rule.Type.ScopeCorrelation)
public class SimpleScopeCorrelationRule extends ScopeCorrelationRule {

    /**
     * Log current identity and return one scope for 'All'
     */
    @Override
    @Argument(name = "scopes", type = ArgumentType.RETURNS, isReturnsType = true)
    protected List<Scope> internalExecute(SailPointContext sailPointContext,
                                          ScopeCorrelationRule.ScopeCorrelationRuleArguments arguments) {
        log.info("Current identity:[{}]", arguments.getIdentity());
        return Collections.singletonList(new Scope("All"));
    }
}
