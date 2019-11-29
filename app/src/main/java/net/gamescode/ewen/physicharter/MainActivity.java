package net.gamescode.ewen.physicharter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.gamescode.ewen.physicharter.experiments.Accelerometer;
import net.gamescode.ewen.physicharter.experiments.ChartTest;
import net.gamescode.ewen.physicharter.experiments.Meter;

public class MainActivity extends AppCompatActivity {

    private Button accelerometerButton;
    private Button testButton;
    private Button meterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testButton = (Button) findViewById(R.id.buttonTest);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChartTest.class));
            }
        });

        meterButton = (Button) findViewById(R.id.buttonMeter);

        meterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Meter.class));
            }
        });

        accelerometerButton = (Button) findViewById(R.id.buttonAccelerometer);

        accelerometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Accelerometer.class));
            }
        });
    }
}
