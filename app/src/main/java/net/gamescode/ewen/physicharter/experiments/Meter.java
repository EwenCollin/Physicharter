package net.gamescode.ewen.physicharter.experiments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.gamescode.ewen.physicharter.MainActivity;
import net.gamescode.ewen.physicharter.R;

import java.util.ArrayList;

public class Meter extends AppCompatActivity implements SensorEventListener {


    private SensorManager sensorManager;
    private Sensor accelerometer;

    private Button resetButton;
    private long lastTime = System.currentTimeMillis();
    private double lastAX, lastAY, lastAZ, lastVX, lastVY, lastVZ, lastDX, lastDY, lastDZ, distance, deltaX, deltaY, deltaZ;
    private ArrayList<Double> AccelerationX, AccelerationY, AccelerationZ, SpeedX, SpeedY, SpeedZ, PositionX, PositionY, PositionZ;

    private TextView distanceDisplay, textLastAX, textLastAY, textLastAZ, textLastDX, textLastDY, textLastDZ, textLastVX, textLastVY, textLastVZ, textElapsedTime, textNewNorm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter);


        resetButton = (Button) findViewById(R.id.buttonResetMeter);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lastAX = 0.0;
                lastAY = 0.0;
                lastAZ = 0.0;
                lastDX = 0.0;
                lastDY = 0.0;
                lastDZ = 0.0;
                lastVX = 0.0;
                lastVY = 0.0;
                lastVZ = 0.0;
                distance = 0.0;
                AccelerationX.removeAll(AccelerationX);
                AccelerationY.removeAll(AccelerationY);
                AccelerationZ.removeAll(AccelerationZ);
                SpeedX.removeAll(SpeedX);
                SpeedY.removeAll(SpeedY);
                SpeedZ.removeAll(SpeedZ);
            }
        });


        distanceDisplay = (TextView) findViewById(R.id.distance_display);
        textLastAX = (TextView) findViewById(R.id.textLastAX);
        textLastAY = (TextView) findViewById(R.id.textLastAY);
        textLastAZ = (TextView) findViewById(R.id.textLastAZ);

        textLastVX = (TextView) findViewById(R.id.textLastVX);
        textLastVY = (TextView) findViewById(R.id.textLastVY);
        textLastVZ = (TextView) findViewById(R.id.textLastVZ);

        textLastDX = (TextView) findViewById(R.id.textLastDX);
        textLastDY = (TextView) findViewById(R.id.textLastDY);
        textLastDZ = (TextView) findViewById(R.id.textLastDZ);

        textNewNorm = (TextView) findViewById(R.id.textNewNorm);

        textElapsedTime = (TextView) findViewById(R.id.textElapsedTime);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // fai! we dont have an accelerometer!
        }

        lastAX = 0.0;
        lastAY = 0.0;
        lastAZ = 0.0;
        lastDX = 0.0;
        lastDY = 0.0;
        lastDZ = 0.0;
        lastVX = 0.0;
        lastVY = 0.0;
        lastVZ = 0.0;

        AccelerationX = new ArrayList<>();
        AccelerationY = new ArrayList<>();
        AccelerationZ = new ArrayList<>();
        SpeedX = new ArrayList<>();
        SpeedY = new ArrayList<>();
        SpeedZ = new ArrayList<>();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float precisionA = (float) 0.0;
        float precisionB = (float) 0.15;
        float precisionV = (float) 1.5;

        if(event.values[0] >= precisionA || event.values[0] <= -precisionA) {
            lastAX = event.values[0];
        }else lastAX = 0;
        if(event.values[1] >= precisionA || event.values[1] <= -precisionA) {
            lastAY = event.values[1];
        }else lastAY = 0;
        if(event.values[2] >= precisionA || event.values[2] <= -precisionA) {
            lastAZ = event.values[2];
        }else lastAZ = 0;

        AccelerationX.add(lastAX);
        AccelerationY.add(lastAY);
        AccelerationZ.add(lastAZ);


        long elapsedTime = System.currentTimeMillis() - lastTime;
        double elapsedTimeSecs = (double) elapsedTime/1000;
        lastTime = System.currentTimeMillis();



        double previousAX = lastAX;
        double previousAY = lastAY;
        double previousAZ = lastAZ;


        double previousVX = lastVX;
        double previousVY = lastVY;
        double previousVZ = lastVZ;

        lastVX = elapsedTimeSecs*(lastAX + previousAX)/2 + lastVX;
        lastVY = elapsedTimeSecs*(lastAY + previousAY)/2 + lastVY;
        lastVZ = elapsedTimeSecs*(lastAZ + previousAZ)/2 + lastVZ;

        SpeedX.add(lastVX);
        SpeedX.add(lastVZ);
        SpeedX.add(lastVY);


        if(lastVX <= precisionV && lastVX >= -precisionV
                && lastAX <= precisionB && lastAX >= -precisionB
                /*&& MeterUtils.isDecreasing(SpeedX, 5)*/) lastVX = 0;
        if(lastVY <= precisionV && lastVY >= -precisionV
                && lastAY <= precisionB && lastAY >= -precisionB
                /*&& MeterUtils.isDecreasing(SpeedY, 5)*/) lastVY = 0;
        if(lastVZ <= precisionV && lastVZ >= -precisionV
                && lastAZ <= precisionB && lastAZ >= -precisionB
                /*&& MeterUtils.isDecreasing(SpeedZ, 5)*/) lastVZ = 0;

        lastDX = Math.pow(elapsedTimeSecs, 2)*(lastAX + previousAX)/2 + lastVX*elapsedTimeSecs + lastDX;
        lastDY = Math.pow(elapsedTimeSecs, 2)*(lastAY + previousAY)/2 + lastVY*elapsedTimeSecs + lastDY;
        lastDZ = Math.pow(elapsedTimeSecs, 2)*(lastAZ + previousAZ)/2 + lastVZ*elapsedTimeSecs + lastDZ;

        distance = (double) Math.sqrt(lastDX*lastDX + lastDY*lastDY + lastDZ*lastDZ);


        textLastAX.setText("Value Acceleration X = " + String.valueOf(lastAX));
        textLastAY.setText("Value Acceleration Y = " + String.valueOf(lastAY));
        textLastAZ.setText("Value Acceleration Z = " + String.valueOf(lastAZ));

        textLastVX.setText("Value Vitesse X = " + String.valueOf(lastVX));
        textLastVY.setText("Value Vitesse Y = " + String.valueOf(lastVY));
        textLastVZ.setText("Value Vitesse Z = " + String.valueOf(lastVZ));

        textLastDX.setText("Value Distance X = " + String.valueOf(lastDX));
        textLastDY.setText("Value Distance Y = " + String.valueOf(lastDY));
        textLastDZ.setText("Value Distance Z = " + String.valueOf(lastDZ));


        textElapsedTime.setText("Value Elapsed Time = " + String.valueOf(elapsedTimeSecs));

        //textLastDZ.setText("Value Elapsed Time" + String.valueOf((double)elapsedTime/1000));

        distanceDisplay.setText(String.valueOf(distance));

        textNewNorm.setText(String.valueOf(Math.abs(lastDX) + Math.abs(lastDY) + Math.abs(lastDZ)));


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
