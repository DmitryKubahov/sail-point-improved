package com.sailpoint.improved.rule;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.JavaRuleContext;
import sailpoint.tools.GeneralException;

import java.util.List;

/**
 * Abstract class for java rule without outputs
 */
@Slf4j
@Getter
public abstract class AbstractNoneOutputJavaRuleExecutor<C> extends AbstractJavaRuleExecutor<Object, C> {

    /**
     * Default constructor for rules without output
     *
     * @param ruleType          - current rule type value
     * @param noneNullArguments - list of arguments for none null checking
     */
    protected AbstractNoneOutputJavaRuleExecutor(String ruleType, List<String> noneNullArguments) {
        super(ruleType, noneNullArguments);
    }

    /**
     * Internal execution of java rule
     *
     * @param javaRuleContext    - java rule context
     * @param containerArguments - argument container for current rule
     * @return rule execution result
     * @throws GeneralException - execution error
     */
    protected Object internalExecute(JavaRuleContext javaRuleContext, C containerArguments)
            throws GeneralException {
        internalExecuteNoneOutput(javaRuleContext, containerArguments);
        return null;
    }

    /**
     * Internal execution of java rule without output
     *
     * @param javaRuleContext    - java rule context
     * @param containerArguments - argument container for current rule
     * @throws GeneralException - execution error
     */
    protected abstract void internalExecuteNoneOutput(JavaRuleContext javaRuleContext, C containerArguments)
            throws GeneralException;


}
