package quantum;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class QuantumTest {

    @Before
    public void setup() {

    }

    @Test
    public void getTangleShouldBeNullSafe() {
        // No Tangles added yet, map is null.
        assertThat(Quantum.getTangle("anything"), is(Quantum.TANGLE_NO_OP));

        // Map still null, key null now too.
        assertThat(Quantum.getTangle(null), is(Quantum.TANGLE_NO_OP));
    }

    @Test
    public void shouldRegisterNewTangle() {
        String id = "testId";
        assertThat(Quantum.getTangle(id), is(Quantum.TANGLE_NO_OP));

        Tangle testTangle = new Tangle();
        Quantum.registerTangle(id, testTangle);

        assertThat(Quantum.getTangle(id), is(testTangle));
    }

    @Test
    public void shouldGetNoOpTangleForInvalidId() {
        assertThat(Quantum.getTangle("anything"), is(Quantum.TANGLE_NO_OP));

        Quantum.registerTangle("validId", new Tangle());

        assertThat(Quantum.getTangle("invalidId"), is(Quantum.TANGLE_NO_OP));
    }

    @Test public void unregisterShouldUnregisterATangle() {
        String id = "testId";
        Tangle testTangle = new Tangle();
        Quantum.registerTangle(id, testTangle);

        assertThat(Quantum.getTangle(id), is(testTangle));

        Quantum.unregisterTangle(id);
        assertThat(Quantum.getTangle(id), is(Quantum.TANGLE_NO_OP));
    }

}