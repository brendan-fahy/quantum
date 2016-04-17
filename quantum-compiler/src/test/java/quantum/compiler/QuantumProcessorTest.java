package quantum.compiler;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class QuantumProcessorTest {

    QuantumProcessor processor;

    @Before
    public void setup() {
        processor = new QuantumProcessor();
    }

    @Test
    public void shouldGetSupportedAnnotationTypes() {
        Set<String> annotationTypes = processor.getSupportedAnnotationTypes();

        assertThat(annotationTypes, hasSize(1));
        assertThat(annotationTypes, hasItem("quantum.annotations.Entangle"));
    }

    @Test
    public void shouldGetLatestSupportedSourceVersion() {
        assertThat(processor.getSupportedSourceVersion(), is(SourceVersion.latestSupported()));
    }

    @Test
    public void initShouldGetFilerAndElementUtils() {
        ProcessingEnvironment mockEnv = mock(ProcessingEnvironment.class);

        processor.init(mockEnv);

        verify(mockEnv).getFiler();
        verify(mockEnv).getElementUtils();
    }

    @Test
    public void shouldGetClassName() {
        TypeElement mockType = mock(TypeElement.class);
        Name mockName = mock(Name.class);
        when(mockName.toString()).thenReturn("quantum.sample.MainActivity");
        when(mockType.getQualifiedName()).thenReturn(mockName);

        String className = QuantumProcessor.getClassName(mockType, "quantum.sample");

        assertThat(className, is("MainActivity"));
    }
}