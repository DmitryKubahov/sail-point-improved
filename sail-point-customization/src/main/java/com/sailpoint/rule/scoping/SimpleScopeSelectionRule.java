package com.sailpoint.rule.scoping;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.improved.rule.scoping.ScopeSelectionRule;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Scope;

/**
 * Simple implementation of {@link ScopeSelectionRule} rule
 */
@Slf4j
@Rule(value = "Simple scope selection rule", type = sailpoint.object.Rule.Type.ScopeSelection)
public class SimpleScopeSelectionRule extends ScopeSelectionRule {

    /**
     * Log current identity and return scope for 'All'
     */
    @Override
    @Argument(name = "scope", type = ArgumentType.RETURNS, isReturnsType = true)
    protected Scope internalExecute(JavaRuleContext context,
                                    ScopeSelectionRuleArguments arguments) {
        log.info("Current identity:[{}]", arguments.getIdentity());
        return new Scope("All");
    }
}
