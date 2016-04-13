package quantum.sample;

import android.app.Application;
import android.view.View;

import quantum.Quantum;
import quantum.Tangle;

public class SampleApplication extends Application {

    public static final String CONNECTION_ID = "connectionId";

    @Override
    public void onCreate() {
        super.onCreate();

        Quantum.registerTangle(CONNECTION_ID, new Tangle() {
            @Override
            public void act(View view) {
                view.setVisibility(View.GONE);
            }
        });
    }
}
