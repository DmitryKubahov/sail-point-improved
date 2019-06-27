package com.sailpoint.test.rule;

import com.sailpoint.processor.RuleAnnotationProcessor;
import com.sailpoint.test.AbstractAnnotationProcessorTest;
import sailpoint.object.Argument;

import javax.annotation.processing.Processor;

/**
 * Abstract rule generator test
 */
public abstract class AbstractRuleAnnotationProcessorTest extends AbstractAnnotationProcessorTest {

    /**
     * Creates argument with following attributes
     *
     * @param name        - name of argument
     * @param type        - type of argument
     * @param multi       - is argument collection flag
     * @param description - description of argument
     * @param prompt      - prompt of argument
     * @return instance of new argument
     */
    protected Argument buildArgument(String name, String type, boolean multi, String description, String prompt) {
        Argument argument = new Argument();
        argument.setName(name);
        argument.setType(type);
        argument.setMulti(multi);
        argument.setDescription(description);
        argument.setPrompt(prompt);
        return argument;
    }

    /**
     * Create rule annotation processor
     *
     * @return instance of rule annotation processor
     */
    @Override
    protected Processor buildProcessor() {
        return new RuleAnnotationProcessor();
    }
}
