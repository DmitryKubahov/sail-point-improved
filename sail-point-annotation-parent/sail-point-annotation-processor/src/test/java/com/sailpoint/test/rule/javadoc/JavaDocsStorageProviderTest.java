package com.sailpoint.test.rule.javadoc;

import com.sailpoint.exception.AnnotationProcessorException;
import com.sailpoint.processor.javadoc.JavaDocsStorageProvider;
import com.sun.tools.javac.util.Name;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.UUID;

import static com.sailpoint.processor.javadoc.JavaDocsStorageProvider.JAVA_DOC_JSON_FILE_PATTERN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link JavaDocsStorageProvider} class
 */
public class JavaDocsStorageProviderTest {

    /**
     * Instance of {@link JavaDocsStorageProvider} for testing
     */
    private JavaDocsStorageProvider javaDocsStorageProvider;

    /**
     * Mock of {@link ProcessingEnvironment} instance
     */
    private ProcessingEnvironment processingEnvironment;

    /**
     * Mock of {@link Filer} instance
     */
    private Filer filer;
    /**
     * Mock of {@link FileObject} instance
     */
    private FileObject fileObject;
    /**
     * Mock of {@link Writer} instance for file object
     */
    private Writer writer;
    /**
     * Mock of {@link Reader} instance for file object
     */
    private Reader reader;
    /**
     * Mock of {@link Elements} instance for file object
     */
    private Elements elements;

    /**
     * Init
     * - mock of processing environment
     * - java doc processor
     */
    @Before
    public void init() throws IOException {
        this.filer = mock(Filer.class);
        this.fileObject = mock(FileObject.class);
        this.writer = mock(Writer.class);
        this.reader = mock(Reader.class);
        this.elements = mock(Elements.class);

        this.processingEnvironment = mock(ProcessingEnvironment.class);
        when(processingEnvironment.getFiler()).thenReturn(filer);
        when(processingEnvironment.getElementUtils()).thenReturn(elements);

        when(fileObject.openWriter()).thenReturn(writer);
        when(fileObject.openReader(anyBoolean())).thenReturn(reader);

        this.javaDocsStorageProvider = new JavaDocsStorageProvider(this.processingEnvironment);
    }

    /**
     * Test of writing java doc
     * Input:
     * - element with java doc
     * Output:
     * - without error
     * Expectation:
     * - call createResource in filer
     * - write method of writer from file object with trimmed java docs
     */
    @Test
    public void javaDocWriteTest() throws IOException {
        String testJavaDocValue = "  TEST JAVA DOC   ";
        String testQualifiedName = "TEST,TEST.TEST";
        String testElementNameValue = UUID.randomUUID().toString();
        ElementKind elementKind = ElementKind.FIELD;

        Element testElement = mock(Element.class);
        Name testElementName = mock(Name.class);
        when(testElementName.toString()).thenReturn(testElementNameValue);

        when(testElement.getSimpleName()).thenReturn(testElementName);
        when(testElement.accept(any(), any())).thenReturn(testQualifiedName);
        when(testElement.getKind()).thenReturn(elementKind);

        when(elements.getDocComment(eq(testElement))).thenReturn(testJavaDocValue);
        when(filer.createResource(any(), any(), any())).thenReturn(fileObject);

        javaDocsStorageProvider.saveJavaDoc(testElement);

        String expectedName = MessageFormat.format(JAVA_DOC_JSON_FILE_PATTERN, testElementNameValue);
        verify(filer)
                .createResource(eq(StandardLocation.SOURCE_OUTPUT), eq(testQualifiedName), eq(expectedName));
        verify(writer).write(eq(testJavaDocValue.trim()));
    }

    /**
     * Test of writing java doc
     * Input:
     * - element without simple name
     * Output:
     * - without error
     * Expectation:
     * - NO call of getting java docs
     * - NO call createResource in filer
     */
    @Test
    public void nullSimpleNameWriteTest() throws IOException {
        Element testElement = mock(Element.class);
        when(testElement.getSimpleName()).thenReturn(null);

        javaDocsStorageProvider.saveJavaDoc(testElement);

        verify(elements, never()).getDocComment(any());
        verify(filer, never()).createResource(any(), any(), any());
    }

    /**
     * Test of writing java doc
     * Input:
     * - element with simple name but with unsupported kind
     * Output:
     * - without error
     * Expectation:
     * - NO call of getting java docs
     * - NO call createResource in filer
     */
    @Test
    public void unsupportedKindWriteTest() throws IOException {
        Element testElement = mock(Element.class);
        Name testElementName = mock(Name.class);
        when(testElement.getSimpleName()).thenReturn(testElementName);

        for (ElementKind unsupportedKind : ElementKind.values()) {
            if (!JavaDocsStorageProvider.SUPPORTED_ELEMENTS_KIND.contains(unsupportedKind)) {
                javaDocsStorageProvider.saveJavaDoc(testElement);
            }
        }

        verify(elements, never()).getDocComment(any());
        verify(filer, never()).createResource(any(), any(), any());
    }

    /**
     * Test of writing java doc
     * Input:
     * - element with empty java doc
     * Output:
     * - without error
     * Expectation:
     * - call of getting java docs
     * - NO call createResource in filer
     */
    @Test
    public void emptyJavaDocWriteTest() throws IOException {
        String testJavaDocValue = "     ";
        ElementKind elementKind = ElementKind.FIELD;

        Element testElement = mock(Element.class);
        Name testElementName = mock(Name.class);

        when(testElement.getSimpleName()).thenReturn(testElementName);
        when(testElement.getKind()).thenReturn(elementKind);
        when(elements.getDocComment(eq(testElement))).thenReturn(testJavaDocValue);

        javaDocsStorageProvider.saveJavaDoc(testElement);

        verify(elements).getDocComment(eq(testElement));
        verify(filer, never()).createResource(any(), any(), any());
    }

    /**
     * Test of writing java doc with IOException of getting resource
     * Input:
     * - element with java doc
     * Output:
     * - AnnotationProcessorException
     * Expectation:
     * - AnnotationProcessorException
     */
    @Test
    public void exceptionWriteTest() throws IOException {
        String testJavaDocValue = "  TEST JAVA DOC   ";
        String testQualifiedName = "TEST,TEST.TEST";
        String testElementNameValue = UUID.randomUUID().toString();
        ElementKind elementKind = ElementKind.FIELD;

        Element testElement = mock(Element.class);
        Name testElementName = mock(Name.class);
        when(testElementName.toString()).thenReturn(testElementNameValue);

        when(testElement.getSimpleName()).thenReturn(testElementName);
        when(testElement.accept(any(), any())).thenReturn(testQualifiedName);
        when(testElement.getKind()).thenReturn(elementKind);

        when(elements.getDocComment(eq(testElement))).thenReturn(testJavaDocValue);
        when(filer.createResource(any(), any(), any())).thenThrow(IOException.class);

        try {
            javaDocsStorageProvider.saveJavaDoc(testElement);
        } catch (AnnotationProcessorException ex) {
            // Not fail exception
        } catch (Exception ex) {
            fail("Must throw AnnotationProcessorException");
        }

        verify(elements).getDocComment(eq(testElement));
        verify(writer, never()).write(eq(testJavaDocValue.trim()));
    }

    /**
     * Test of reading java doc
     * Input:
     * - element with java doc
     * Output:
     * - trimmed java doc from element
     * Expectation:
     * - call of getting java docs
     * - NO call getResource in filer
     */
    @Test
    public void javaDocReadTest() throws IOException {
        String testJavaDocValue = "  TEST JAVA DOC   ";
        String testQualifiedName = "TEST,TEST.TEST";
        String testElementNameValue = UUID.randomUUID().toString();
        ElementKind elementKind = ElementKind.FIELD;

        Element testElement = mock(Element.class);
        Name testElementName = mock(Name.class);
        when(testElementName.toString()).thenReturn(testElementNameValue);

        when(testElement.getSimpleName()).thenReturn(testElementName);
        when(testElement.accept(any(), any())).thenReturn(testQualifiedName);
        when(testElement.getKind()).thenReturn(elementKind);

        when(elements.getDocComment(eq(testElement))).thenReturn(testJavaDocValue);

        assertEquals("Expected java doc not match", testJavaDocValue.trim(),
                javaDocsStorageProvider.readJavaDoc(testElement));

        verify(elements).getDocComment(eq(testElement));
        verify(filer, never()).getResource(any(), any(), any());
    }

    /**
     * Test of reading java doc from file
     * Input:
     * - element without java doc
     * Output:
     * - trimmed java doc from resource
     * Expectation:
     * - call of getting java docs
     * - call getResource in filer
     * - call read of reader
     */
    @Test
    public void javaDocFileReadTest() throws IOException {
        String testQualifiedName = "TEST,TEST.TEST";
        String testElementNameValue = UUID.randomUUID().toString();
        ElementKind elementKind = ElementKind.FIELD;

        Element testElement = mock(Element.class);
        Name testElementName = mock(Name.class);
        when(testElementName.toString()).thenReturn(testElementNameValue);

        when(testElement.getSimpleName()).thenReturn(testElementName);
        when(testElement.accept(any(), any())).thenReturn(testQualifiedName);
        when(testElement.getKind()).thenReturn(elementKind);

        when(elements.getDocComment(eq(testElement))).thenReturn(null);
        when(reader.read((char[]) any())).thenReturn(-1);
        when(filer.getResource(any(), any(), any())).thenReturn(fileObject);

        javaDocsStorageProvider.readJavaDoc(testElement);

        verify(elements).getDocComment(eq(testElement));

        String expectedName = MessageFormat.format(JAVA_DOC_JSON_FILE_PATTERN, testElementNameValue);
        verify(filer).getResource(eq(StandardLocation.CLASS_PATH), eq(testQualifiedName), eq(expectedName));
        verify(reader).read((char[]) any());
    }

    /**
     * Test of reading java doc
     * Input:
     * - element with java doc with unsupported type
     * Output:
     * - trimmed java doc from element
     * Expectation:
     * - call of getting java docs
     * - NO call getResource in filer
     */
    @Test
    public void javaDocReadUnsupportedKindTest() throws IOException {
        String testQualifiedName = "TEST,TEST.TEST";
        String testElementNameValue = UUID.randomUUID().toString();
        int runCount = 0;

        Element testElement = mock(Element.class);
        Name testElementName = mock(Name.class);
        when(testElementName.toString()).thenReturn(testElementNameValue);

        when(testElement.getSimpleName()).thenReturn(testElementName);
        when(testElement.accept(any(), any())).thenReturn(testQualifiedName);


        for (ElementKind unsupportedKind : ElementKind.values()) {
            if (!JavaDocsStorageProvider.SUPPORTED_ELEMENTS_KIND.contains(unsupportedKind)) {
                String testJavaDoc = "  ".concat(UUID.randomUUID().toString()).concat(" ");
                when(testElement.getKind()).thenReturn(unsupportedKind);
                when(elements.getDocComment(eq(testElement))).thenReturn(testJavaDoc);
                assertEquals("Expected java doc not match for elementKind:" + unsupportedKind, testJavaDoc.trim(),
                        javaDocsStorageProvider.readJavaDoc(testElement));
                runCount++;
            }
        }
        verify(elements, times(runCount)).getDocComment(eq(testElement));
        verify(filer, never()).getResource(any(), any(), any());
    }

    /**
     * Test of reading java doc file cause IOException
     * Input:
     * - element without java doc
     * Output:
     * - null
     * Expectation:
     * - call of getting java docs
     * - call getResource in filer
     * - NO call read of reader
     */
    @Test
    public void javaDocFileReadIOExceptionTest() throws IOException {
        String testQualifiedName = "TEST,TEST.TEST";
        String testElementNameValue = UUID.randomUUID().toString();
        ElementKind elementKind = ElementKind.FIELD;

        Element testElement = mock(Element.class);
        Name testElementName = mock(Name.class);
        when(testElementName.toString()).thenReturn(testElementNameValue);

        when(testElement.getSimpleName()).thenReturn(testElementName);
        when(testElement.accept(any(), any())).thenReturn(testQualifiedName);
        when(testElement.getKind()).thenReturn(elementKind);

        when(elements.getDocComment(eq(testElement))).thenReturn(null);
        when(reader.read((char[]) any())).thenReturn(-1);
        when(filer.getResource(any(), any(), any())).thenThrow(IOException.class);

        assertNull("With IOException while reading java doc file must return NULL",
                javaDocsStorageProvider.readJavaDoc(testElement));

        verify(elements).getDocComment(eq(testElement));
        String expectedName = MessageFormat.format(JAVA_DOC_JSON_FILE_PATTERN, testElementNameValue);
        verify(filer).getResource(eq(StandardLocation.CLASS_PATH), eq(testQualifiedName), eq(expectedName));
        verify(reader, never()).read((char[]) any());
    }
}
