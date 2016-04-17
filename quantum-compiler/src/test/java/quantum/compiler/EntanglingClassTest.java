package quantum.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import javax.lang.model.element.Modifier;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

public class EntanglingClassTest {

    public static final String PACKAGE_NAME = "package";
    public static final String CLASS_NAME = "Class";
    public static final String TARGET_CLASS = "TargetClass";
    public static final String FIELD_NAME = "fieldName";
    public static final String ID = "ID";
    private EntanglingClass entanglingClass;

    @Before
    public void setup() {
        entanglingClass = new EntanglingClass(
                PACKAGE_NAME,
                CLASS_NAME,
                TARGET_CLASS,
                FIELD_NAME,
                ID);
    }

    @Test
    public void javaFileShouldHaveCorrectPackageName() {
        JavaFile javaFile = entanglingClass.brewJava();

        assertThat(javaFile.packageName, is(PACKAGE_NAME));
    }

    @Test
    public void javaFileShouldHaveGeneratedComment() {
        JavaFile file = entanglingClass.brewJava();

        assertThat(file.fileComment.toString(), is(EntanglingClass.GENERATED_CODE_COMMENT));
    }

    @Test
    public void typeSpecShouldBeProperlyFormed() {
        TypeSpec typeSpec = entanglingClass.createTypeSpec();

        assertThat(typeSpec.name, is(CLASS_NAME));

        assertThat(typeSpec.typeVariables, hasSize(1));
        assertThat(typeSpec.typeVariables, hasItem(TypeVariableName.get("T", ClassName.bestGuess(TARGET_CLASS))));

        assertThat(typeSpec.modifiers, hasItem(Modifier.PUBLIC));
        assertThat(typeSpec.modifiers, hasSize(1));

        assertThat(typeSpec.superinterfaces, hasItem(ClassName.get("java.util", "Observer")));
        assertThat(typeSpec.superinterfaces, hasSize(1));
    }

    @Test
    public void typeSpecShouldHaveCorrectFields() {
        TypeSpec typeSpec = entanglingClass.createTypeSpec();

        assertThat(typeSpec.fieldSpecs, hasSize(2));
        assertThat(typeSpec.fieldSpecs, allOf(
                hasItem(FieldSpec.builder(TypeVariableName.get("T", ClassName.bestGuess(TARGET_CLASS)), "target")
                        .addModifiers(PRIVATE)
                        .build()),
                hasItem(FieldSpec.builder(String.class, "id").initializer("\"$L\"", ID)
                        .addModifiers(PRIVATE)
                        .build())));
    }

    @Test
    public void updateMethodShouldBePublicOverrideWithCorrectParams() {
        MethodSpec method = entanglingClass.createUpdateMethod();

        assertThat(method.name, is("update"));
        assertThat(method.modifiers, hasItem(Modifier.PUBLIC));
        assertThat(method.modifiers, hasSize(1));
        assertThat(method.annotations, hasItem(AnnotationSpec.builder(Override.class).build()));
        assertThat(method.annotations, hasSize(1));

        ParameterSpec observableParam = ParameterSpec.builder(
                ClassName.get("java.util", "Observable"), "observable").build();
        ParameterSpec dataParam = ParameterSpec.builder(
                Object.class, "data").build();

        assertThat(method.parameters, hasItems(observableParam, dataParam));
        assertThat(method.parameters, hasSize(2));
    }

    @Test
    public void updateMethodShouldHaveVisibilityCode() {
        MethodSpec method = entanglingClass.createUpdateMethod();

        assertThat(method.code.toString(), is(String.format(
                "target.%s.setVisibility((((quantum.Tangle<Boolean>) observable).getValue()) " +
                "? android.view.View.GONE : android.view.View.VISIBLE);\n", FIELD_NAME)));
    }

    @Test
    public void entangleMethodShouldHaveCorrectSignature() {
        MethodSpec method = entanglingClass.createEntangleMethod();

        assertThat(method.name, is("entangle"));
        assertThat(method.modifiers, hasItem(Modifier.PUBLIC));
        assertThat(method.modifiers, hasSize(1));
        assertThat(method.parameters, hasItem(ParameterSpec.builder(
                TypeVariableName.get("T"), "target", FINAL).build()));
        assertThat(method.parameters, hasSize(1));
    }

    @Test
    public void entangleMethodShouldAddObserver() {
        MethodSpec method = entanglingClass.createEntangleMethod();

        assertThat(method.code.toString(), is("this.target = target;\n" +
                "quantum.Quantum.getTangle(id).addObserver(this);\n"));
    }

    @Test
    public void detangleMethodShouldHaveCorrectSignature() {
        MethodSpec method = entanglingClass.createDetangleMethod();

        assertThat(method.name, is("detangle"));
        assertThat(method.modifiers, hasItem(Modifier.PUBLIC));
        assertThat(method.modifiers, hasSize(1));
        assertThat(method.parameters, is(Collections.<ParameterSpec>emptyList()));
    }

    @Test
    public void detangleMethodShouldDeleteObservers() {
        MethodSpec method = entanglingClass.createDetangleMethod();

        assertThat(method.code.toString(), is("quantum.Quantum.getTangle(id).deleteObservers();\n"));
    }

}