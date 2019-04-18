package com.sailpoint.improved.rule.runner;

import lombok.extern.slf4j.Slf4j;
import sailpoint.api.SailPointFactory;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.JavaRuleExecutor;
import sailpoint.object.Rule;
import sailpoint.server.BSFRuleRunner;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Improved java rule runner,
 * After finding class of rule:
 * 1 - get it instance by calling static method getInstance
 * 2 - call execute method of class
 */
@Slf4j
public class JavaRuleRunner<T extends JavaRuleExecutor> extends BSFRuleRunner {

    /**
     * Override only run java rule
     *
     * @param rule          - rule to run
     * @param parameters    - passed parameters
     * @param ruleLibraries - rule libraries
     * @return rule executing result
     * @throws GeneralException - error while execute rule
     */
    @Override
    public Object runJavaRule(Rule rule, Map<String, Object> parameters, List<Rule> ruleLibraries)
            throws GeneralException {
        log.debug("Rule language is java");
        log.trace("Rule:[{}], parameters:[{}], libraries:[{}]", rule, parameters, ruleLibraries);

        log.debug("Validate input parameters");
        validateRule(rule);

        log.debug("Try to get rule instance and run it");
        try {
            String className = rule.getAttributeValue(Dictionary.ATTR_JAVA_RULE_CLASS_NAME).toString();
            log.debug("Try to init rule class by name. Rule:[{}], class name:[{}]", rule.getName(), className);
            T ruleExecutor = getRuleExecutor(className);
            log.debug("Create rule context and run it");
            return ruleExecutor.execute(new JavaRuleContext(SailPointFactory.getCurrentContext(), parameters));
        } catch (GeneralException ex) {
            log.error("Got general exception:[{}]", ex.getMessage(), ex);
            throw ex;
        } catch (Throwable ex) {
            log.error("Got unknown exception:[{}]", ex.getMessage(), ex);
            throw new GeneralException(ex.getMessage());
        }
    }

    /**
     * Get java rule executor instance via reflection in class by className
     *
     * @param ruleExecutorClassName - rule executor class name value
     * @return instance of rule executor
     */
    private T getRuleExecutor(String ruleExecutorClassName) throws GeneralException {
        try {
            log.debug("Try to initialize class of rule executor by name:[{}]", ruleExecutorClassName);
            Class<T> ruleExecutorClass = (Class<T>) Class.forName(ruleExecutorClassName);
            log.debug("Get instance of rule executor via reflection calling method getInstance in rule executor class");
            Method getInstanceMethod = ruleExecutorClass.getMethod(Dictionary.JAVA_RULE_GET_INSTANCE_METHOD_NAME);
            return (T) getInstanceMethod.invoke(null);
        } catch (Exception ex) {
            log.error("Got:[{}] while initialize rule executor instance", ex.getMessage(), ex);
            throw new GeneralException(ex);
        }
    }

    /**
     * Validates input parameters before running rule. Validates:
     * - rule contains {@link JavaRuleRunner ,Dictionary#ATTR_JAVA_RULE_CLASS_NAME} attribute, not null and not empty
     *
     * @param rule - rule for run
     */
    private void validateRule(Rule rule) throws GeneralException {
        log.debug("Validate rule");
        Object className = rule.getAttributeValue(Dictionary.ATTR_JAVA_RULE_CLASS_NAME);
        if (className != null && Util.isNullOrEmpty(className.toString())) {
            String errorMessage = MessageFormat.format(
                    JavaRuleRunner.Dictionary.VALIDATION_RULE_ERROR_MESSAGE, rule.getName(),
                    "rule class attribute is empty");
            log.error(errorMessage);
            throw new GeneralException(errorMessage);
        }
    }

    /**
     * Dictionary for JavaRuleRunner
     */
    public static final class Dictionary {

        /**
         * Attribute name for class name. Must be full name of class
         */
        public static final String ATTR_JAVA_RULE_CLASS_NAME = "ruleClass";

        /**
         * Get instance rule method
         */
        public static final String JAVA_RULE_GET_INSTANCE_METHOD_NAME = "getInstance";

        /**
         * Validate parameters error message. Parameters:
         * 0 - rule name
         * 1 - validation error message
         */
        public static final String VALIDATION_RULE_ERROR_MESSAGE = "Rule:[{0}], validation error:[{1}]";

        /**
         * Only dictionary, Nothing more
         */
        private Dictionary() {
        }
    }
}
