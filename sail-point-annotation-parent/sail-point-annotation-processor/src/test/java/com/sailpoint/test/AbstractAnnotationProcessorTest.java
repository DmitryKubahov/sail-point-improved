package com.sailpoint.test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import com.sailpoint.processor.SailPointAnnotationProcessorDictionary;
import org.junit.Before;
import sailpoint.tools.xml.XMLObjectFactory;

import javax.annotation.processing.Processor;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

/**
 * Abstract annotation processor class for all test with compiler
 */
public abstract class AbstractAnnotationProcessorTest {

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
        compiler = javac().withProcessors(buildProcessor()).withOptions(
                "-A".concat(SailPointAnnotationProcessorDictionary.GENERATION_PATH).concat("=").concat(TEST_XML_PATH));
        Compilation compilation = compile(compiler);
        assertThat(compilation).succeededWithoutWarnings();
    }

    /**
     * Creates instance of processor to test
     *
     * @return instance of annotation processor to test
     */
    protected abstract Processor buildProcessor();

    /**
     * Default implementation of building compilation
     *
     * @param compiler - compile instance
     * @return compilation instance from compile
     */
    protected Compilation compile(Compiler compiler) {
        return compiler
                .compile(JavaFileObjects
                        .forResource(getClass().getClassLoader().getResource(getJavaClassFilePathName())));
    }

    /**
     * Return path to test java classes for testing
     *
     * @return path to java class
     */
    protected abstract String getJavaClassFilePathName();

}
