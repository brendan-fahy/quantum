package quantum;

import android.app.Activity;

import java.lang.reflect.Method;

/**
 * Created by bfahy on 11/04/16.
 */
public class Quantum {

    public static final String GENERATED_SUFFIX = "$$QuantumEntangler";

    public static void entangle(Activity target) {
        try {
            Class<?> entangler = Class.forName(target.getClass().getName() + GENERATED_SUFFIX);
            Method entangle = entangler.getMethod("entangle", target.getClass(), Object.class);

            entangle.invoke(entangler.newInstance(), target, target);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to entangle Quantum-annotated Views for Activity " + target, ex);
        }
    }
}
