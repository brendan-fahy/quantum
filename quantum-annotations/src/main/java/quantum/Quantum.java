package quantum;

import android.app.Activity;
import android.view.View;

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
            Method entangle = entangler.getMethod("entangle", target.getClass(), Object.class);

            entangle.invoke(entangler.newInstance(), target, target);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to entangle Quantum-annotated Views for Activity " + target, ex);
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

    private static final Tangle TANGLE_NO_OP = new Tangle() {
        @Override
        public void act(View view) {
            // Do nothing.
        }
    };
}
