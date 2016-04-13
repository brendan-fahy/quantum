package quantum.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

final class EntanglingClass {

    private final String classPackage;
    private final String className;
    private final String targetClass;
    private final String fieldName;
    private final String connectionId;

    EntanglingClass(String classPackage, String className, String targetClass, String fieldName, String connectionId) {
        this.classPackage = classPackage;
        this.className = className;
        this.targetClass = targetClass;
        this.fieldName = fieldName;
        this.connectionId = connectionId;
    }

    JavaFile brewJava() {
        TypeSpec.Builder result = TypeSpec.classBuilder(className)
                .addModifiers(PUBLIC)
                .addTypeVariable(TypeVariableName.get("T", ClassName.bestGuess(targetClass)));

        result.addMethod(createEntangleMethod());

        return JavaFile.builder(classPackage, result.build())
                .addFileComment("Generated code from Quantum. Do not modify!")
                .build();
    }

    private MethodSpec createEntangleMethod() {
        MethodSpec.Builder result = MethodSpec.methodBuilder("entangle")
                .addModifiers(PUBLIC)
                .addParameter(TypeVariableName.get("T"), "target", FINAL)
                .addParameter(Object.class, "source");

//        result.addStatement("target.$L.setVisibility(android.view.View.GONE)", fieldName);

        result.addStatement("String connectionId = \"$L\"", connectionId);
        result.addStatement("quantum.Quantum.getTangle(connectionId).act(target.$L)", fieldName);

        return result.build();
    }

}
