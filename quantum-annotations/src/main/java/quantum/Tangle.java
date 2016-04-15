package quantum;

import java.util.Observable;

/**
 * Created by bfahy on 15/04/16.
 */
public class Tangle<T> extends Observable {

    T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        setChanged();
        notifyObservers();
    }
}
