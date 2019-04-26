package com.sailpoint.test.rule.javadoc;

import com.sailpoint.processor.javadoc.JavaDocsAnnotationProcessor;
import com.sailpoint.processor.javadoc.JavaDocsStorageProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Test for {@link JavaDocsAnnotationProcessor} class
 */
public class JavaDocsAnnotationProcessorTest {

    /**
     * Instance of {@link JavaDocsAnnotationProcessor} class for test
     */
    private JavaDocsAnnotationProcessor javaDocsAnnotationProcessor;
    /**
     * Instance of {@link JavaDocsStorageProvider} for mocking
     */
    private JavaDocsStorageProvider javaDocsProvider;

    /**
     * Init java docs annotation processor and set java doc storage via reflection
     */
    @Before
    public void init() {
        this.javaDocsProvider = mock(JavaDocsStorageProvider.class);
        this.javaDocsAnnotationProcessor = new JavaDocsAnnotationProcessor();
        ReflectionTestUtils.setField(this.javaDocsAnnotationProcessor, "javaDocsProvider", this.javaDocsProvider);
    }

    /**
     * Test writing java docs for simple element
     * Input:
     * - round environment with 1 element
     * Output:
     * - without error
     * Expectation:
     * - 1 call of {@link JavaDocsStorageProvider#saveJavaDoc(Element)}
     */
    @Test
    public void oneElementTest() {
        Set<Element> rootElements = new HashSet<>(generateElements(1));
        RoundEnvironment roundEnvironment = mock(RoundEnvironment.class);
        doReturn(rootElements).when(roundEnvironment).getRootElements();

        this.javaDocsAnnotationProcessor.process(Collections.emptySet(), roundEnvironment);
        verify(javaDocsProvider).saveJavaDoc(any());
    }

    /**
     * Test writing java docs for group of elements
     * Input:
     * - round environment with group of elements each of them contains group of elements too
     * Output:
     * - without error
     * Expectation:
     * - N (count of all elements) call of {@link JavaDocsStorageProvider#saveJavaDoc(Element)}
     */
    @Test
    public void groupsElementTest() {
        int maxCount = 5;
        Set<Element> rootElements = new HashSet<>(generateElements(maxCount));
        List<Element> elements = generateElements(maxCount);
        rootElements.forEach(rootElement -> doReturn(elements).when(rootElement).getEnclosedElements());
        RoundEnvironment roundEnvironment = mock(RoundEnvironment.class);
        doReturn(rootElements).when(roundEnvironment).getRootElements();

        this.javaDocsAnnotationProcessor.process(Collections.emptySet(), roundEnvironment);
        verify(javaDocsProvider, times(maxCount + maxCount * maxCount)).saveJavaDoc(any());
    }

    /**
     * Test writing java docs for null of group elements
     * Input:
     * - round environment with null of root elements
     * Output:
     * - without error
     * Expectation:
     * - no call of {@link JavaDocsStorageProvider#saveJavaDoc(Element)}
     */
    @Test
    public void nullRootElementTest() {
        RoundEnvironment roundEnvironment = mock(RoundEnvironment.class);
        when(roundEnvironment.getRootElements()).thenReturn(null);

        this.javaDocsAnnotationProcessor.process(Collections.emptySet(), roundEnvironment);
        verify(javaDocsProvider, never()).saveJavaDoc(any());
    }

    /**
     * Generate mock objects of {@link Element} type
     *
     * @param count - count of elements
     * @return list of mock elements
     */
    private List<Element> generateElements(int count) {
        List<Element> elements = new ArrayList<>(count);
        while (count-- > 0) {
            elements.add(mock(Element.class));
        }
        return elements;
    }
}
