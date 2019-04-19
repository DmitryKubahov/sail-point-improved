package com.sailpoint.test.rule;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import com.sailpoint.processor.RuleAnnotationProcessor;
import com.sailpoint.processor.SailPointAnnotationProcessorDictionary;
import org.junit.Before;
import sailpoint.object.Argument;
import sailpoint.tools.xml.XMLObjectFactory;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

/**
 * Abstract rule generator test
 */
public abstract class AbstractRuleAnnotationProcessorTest {

    /**
     * Path for test generated rule
     */
    protected static final String TEST_XML_PATH = "target/test-classes/";
    /**
     * Sail point object factory
     */
    protected static final XMLObjectFactory XML_OBJECT_FACTORY = XMLObjectFactory.getInstance();
    /**
     * Rule process annotation compiler instance
     */
    private Compiler compiler;

    /**
     * Init compilation
     */
    @Before
    public void init() {
        compiler = javac().withProcessors(new RuleAnnotationProcessor()).withOptions(
                "-A".concat(SailPointAnnotationProcessorDictionary.GENERATION_PATH).concat("=").concat(TEST_XML_PATH));
        Compilation compilation = compile(compiler);
        assertThat(compilation).succeededWithoutWarnings();
    }

    /**
     * Default implementation of building compilation
     *
     * @param compiler - compile instance
     * @return compilation instance from compile
     */
    protected Compilation compile(Compiler compiler) {
        return compiler
                .compile(JavaFileObjects.forResource(getClass().getClassLoader().getResource(getRuleFilePathName())));
    }

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
     * Return path to test java rule file for testing
     *
     * @return path to java rule
     */
    protected abstract String getRuleFilePathName();

}
