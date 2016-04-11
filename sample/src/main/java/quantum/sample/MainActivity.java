package quantum.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import quantum.Quantum;
import quantum.annotations.Entangle;

public class MainActivity extends AppCompatActivity {

    @Entangle("any_string")
    TextView tvSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSample = (TextView) findViewById(R.id.tvSample);

        Quantum.entangle(this);
    }
}
