package quantum;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bfahy on 11/04/16.
 */
public class Quantum {

    public static final String GENERATED_SUFFIX = "$$QuantumEntangler";

    private static Map<String, Tangle> entanglings;

    public static void entangle(Activity target) {
        try {
            Class<?> entangler = Class.forName(target.getClass().getName() + GENERATED_SUFFIX);
            Method entangle = entangler.getMethod("entangle", target.getClass());

            entangle.invoke(entangler.newInstance(), target);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to entangle Quantum-annotated Views for Activity " + target, ex);
        }
    }

    public static void detangle(Activity target) {
        try{
            Class<?> untangler = Class.forName(target.getClass().getName() + GENERATED_SUFFIX);
            Method untangle = untangler.getMethod("detangle");

            untangle.invoke(untangler.newInstance());
        } catch (Exception ex) {
            throw new RuntimeException("Unable to detangle Quantum-annoted Views for Activity " + target, ex);
        }
    }

    public static void registerTangle(String id, Tangle tangle) {
        if (entanglings == null) {
            entanglings = new HashMap<>();
        }
        entanglings.put(id, tangle);
    }

    public static void unregisterTangle(String id) {
        if (entanglings == null) {
            return;
        }
        entanglings.remove(id);
    }

    public static Tangle getTangle(String id) {
        if (entanglings == null || !entanglings.containsKey(id)) {
            return TANGLE_NO_OP;
        } else {
            return entanglings.get(id);
        }
    }

    @VisibleForTesting
    static final Tangle TANGLE_NO_OP = new Tangle() {

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public void setValue(Object value) {
            // Do nothing.
        }
    };
}
