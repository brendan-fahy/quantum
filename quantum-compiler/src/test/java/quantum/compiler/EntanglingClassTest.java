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
    public void typeSpecShouldHaveCorrectName() {
        TypeSpec typeSpec = entanglingClass.createTypeSpec();

        assertThat(typeSpec.name, is(CLASS_NAME));
    }

    @Test
    public void typeSpecShouldHaveParameterizedTypeVariable() {
        TypeSpec typeSpec = entanglingClass.createTypeSpec();

        assertThat(typeSpec.typeVariables, hasItem(TypeVariableName.get("T", ClassName.bestGuess(TARGET_CLASS))));
    }

    @Test
    public void typeSpecShouldHaveOnlyOneTypeVariable() {
        TypeSpec typeSpec = entanglingClass.createTypeSpec();

        assertThat(typeSpec.typeVariables, hasSize(1));
    }

    @Test
    public void typeSpecShouldBePublic() {
        TypeSpec typeSpec = entanglingClass.createTypeSpec();

        assertThat(typeSpec.modifiers, hasItem(Modifier.PUBLIC));
    }

    @Test
    public void typeSpecShouldHaveOnlyOneModifier() {
        TypeSpec typeSpec = entanglingClass.createTypeSpec();

        assertThat(typeSpec.modifiers, hasSize(1));
    }

    @Test
    public void typeSpecShouldImplementObserver() {
        TypeSpec typeSpec = entanglingClass.createTypeSpec();

        assertThat(typeSpec.superinterfaces, hasItem(ClassName.get("java.util", "Observer")));
    }

    @Test
    public void typeSpecShouldOnlyImplementOneInterface() {
        TypeSpec typeSpec = entanglingClass.createTypeSpec();

        assertThat(typeSpec.superinterfaces, hasSize(1));
    }

    @Test
    public void typeSpecShouldHaveTwoFields() {
        TypeSpec typeSpec = entanglingClass.createTypeSpec();

        assertThat(typeSpec.fieldSpecs, hasSize(2));
    }

    @Test
    public void typeSpecShouldHaveTargetAndIdFields() {
        TypeSpec typeSpec = entanglingClass.createTypeSpec();

        assertThat(typeSpec.fieldSpecs, allOf(
                hasItem(FieldSpec.builder(TypeVariableName.get("T", ClassName.bestGuess(TARGET_CLASS)), "target")
                        .addModifiers(PRIVATE)
                        .build()),
                hasItem(FieldSpec.builder(String.class, "id").initializer("\"$L\"", ID)
                        .addModifiers(PRIVATE)
                        .build())));
    }

    @Test public void updateMethodShouldHaveCorrectName() {
        MethodSpec method = entanglingClass.createUpdateMethod();

        assertThat(method.name, is("update"));
    }

    @Test
    public void updateMethodShouldBePublic() {
        MethodSpec method = entanglingClass.createUpdateMethod();

        assertThat(method.modifiers, hasItem(Modifier.PUBLIC));
    }

    @Test
    public void updateMethodShouldHaveOnlyOneModifier() {
        MethodSpec method = entanglingClass.createUpdateMethod();

        assertThat(method.modifiers, hasSize(1));
    }

    @Test
    public void updateMethodShouldHaveOverrideAnnotation() {
        MethodSpec method = entanglingClass.createUpdateMethod();

        assertThat(method.annotations, hasItem(AnnotationSpec.builder(Override.class).build()));
    }

    @Test
    public void updateMethodShouldHaveOnlyOneAnnotation() {
        MethodSpec method = entanglingClass.createUpdateMethod();

        assertThat(method.annotations, hasSize(1));
    }

    @Test
    public void updateMethodShouldHaveObservableAndDataParams() {
        MethodSpec method = entanglingClass.createUpdateMethod();

        ParameterSpec observableParam = ParameterSpec.builder(
                ClassName.get("java.util", "Observable"), "observable").build();
        ParameterSpec dataParam = ParameterSpec.builder(
                Object.class, "data").build();

        assertThat(method.parameters, hasItems(observableParam, dataParam));
    }

    @Test
    public void updateMethodShouldHaveOnlyTwoParams() {
        MethodSpec method = entanglingClass.createUpdateMethod();

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
    public void entangleMethodShouldHaveCorrectName() {
        MethodSpec entangle = entanglingClass.createEntangleMethod();

        assertThat(entangle.name, is("entangle"));
    }

    @Test
    public void entangleMethodShouldBePublic() {
        MethodSpec entangle = entanglingClass.createEntangleMethod();

        assertThat(entangle.modifiers, hasItem(Modifier.PUBLIC));
    }

    @Test
    public void entangleMethodShouldHaveOnlyOneModifier() {
        MethodSpec entangle = entanglingClass.createEntangleMethod();

        assertThat(entangle.modifiers, hasSize(1));
    }

    @Test
    public void entangleMethodShouldHaveTargetParameter() {
        MethodSpec entangle = entanglingClass.createEntangleMethod();

        assertThat(entangle.parameters, hasItem(ParameterSpec.builder(
                TypeVariableName.get("T"), "target", FINAL).build()));
    }
    @Test
    public void entangleMethodShouldHaveOnlyOneParameter() {
        MethodSpec entangle = entanglingClass.createEntangleMethod();

        assertThat(entangle.parameters, hasSize(1));
    }

    @Test
    public void entangleMethodCodeShouldAddObserver() {
        MethodSpec method = entanglingClass.createEntangleMethod();

        assertThat(method.code.toString(), is("this.target = target;\n" +
                "quantum.Quantum.getTangle(id).addObserver(this);\n"));
    }

    @Test
    public void detangleMethodShouldHaveCorrectName() {
        MethodSpec detangle = entanglingClass.createDetangleMethod();

        assertThat(detangle.name, is("detangle"));
    }

    @Test
    public void detangleMethodShouldBePublic() {
        MethodSpec detangle = entanglingClass.createDetangleMethod();

        assertThat(detangle.modifiers, hasItem(Modifier.PUBLIC));
    }

    @Test
    public void detangleMethodShouldHaveOnlyOneModifier() {
        MethodSpec detangle = entanglingClass.createDetangleMethod();

        assertThat(detangle.modifiers, hasSize(1));
    }

    @Test
    public void detangleMethodShouldHaveNoParameters() {
        MethodSpec detangle = entanglingClass.createDetangleMethod();

        assertThat(detangle.parameters, is(Collections.<ParameterSpec>emptyList()));
    }

    @Test
    public void detangleMethodCodeShouldDeleteObservers() {
        MethodSpec method = entanglingClass.createDetangleMethod();

        assertThat(method.code.toString(), is("quantum.Quantum.getTangle(id).deleteObservers();\n"));
    }

}