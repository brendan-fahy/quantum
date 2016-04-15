package quantum.sample;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import quantum.Quantum;
import quantum.Tangle;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SampleApplication extends Application {

    public static final String CONNECTION_ID = "connectionId";

    @Override
    public void onCreate() {
        super.onCreate();

        final Tangle<Boolean> tangle = new Tangle<>();

        Quantum.registerTangle(CONNECTION_ID, tangle);

        Observable.interval(1l, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        tangle.setValue(tangle.getValue() == null || !tangle.getValue());
                    }
                });
    }
}
