package quantum.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxCompoundButton;

import quantum.Quantum;
import quantum.annotations.Entangle;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @Entangle(SampleApplication.CONNECTION_ID)
    TextView tvSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSample = (TextView) findViewById(R.id.tvSample);

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);

        RxCompoundButton.checkedChanges(checkBox)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            Quantum.entangle(MainActivity.this);
                        } else {
                            Quantum.detangle(MainActivity.this);
                        }
                    }
                });
    }
}
