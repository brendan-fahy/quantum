package quantum.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
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

        result.addSuperinterface(ClassName.get("java.util", "Observer"));

        result.addMethod(createEntangleMethod());

        result.addMethod(createDetangleMethod());

        result.addMethod(createUpdateMethod());

        result.addField(FieldSpec.builder(TypeVariableName.get("T", ClassName.bestGuess(targetClass)), "target")
                .addModifiers(PRIVATE)
                .build());

        result.addField(FieldSpec.builder(String.class, "id").initializer("\"$L\"", connectionId)
                .addModifiers(PRIVATE)
                .build());

        return JavaFile.builder(classPackage, result.build())
                .addFileComment("Generated code from Quantum. Do not modify!")
                .build();
    }

    private MethodSpec createEntangleMethod() {
        MethodSpec.Builder result = MethodSpec.methodBuilder("entangle")
                .addModifiers(PUBLIC)
                .addParameter(TypeVariableName.get("T"), "target", FINAL);

        result.addStatement("this.target = target");
        result.addStatement("quantum.Quantum.getTangle(id).addObserver(this)");

        return result.build();
    }

    private MethodSpec createDetangleMethod() {
        return MethodSpec.methodBuilder("detangle")
                .addModifiers(PUBLIC)
                .addStatement("quantum.Quantum.getTangle(id).deleteObservers()")
                .build();
    }

    private MethodSpec createUpdateMethod() {
        MethodSpec.Builder result = MethodSpec.methodBuilder("update")
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ClassName.get("java.util", "Observable"), "observable")
                .addParameter(Object.class, "data");

        result.addStatement("target.$L.setVisibility((((quantum.Tangle<Boolean>) observable).getValue()) " +
                        "? android.view.View.GONE : android.view.View.VISIBLE)", fieldName);

        return result.build();
    }

}
