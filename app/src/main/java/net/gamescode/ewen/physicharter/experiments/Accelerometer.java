package net.gamescode.ewen.physicharter.experiments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.renderscript.Sampler;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.CircularGauge;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.anychart.graphics.vector.text.HAlign;

import net.gamescode.ewen.physicharter.R;

import java.util.ArrayList;
import java.util.List;

public class Accelerometer extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private float lastX, lastY, lastZ;

    private CircularGauge circularGauge;
    private AnyChartView anyChartView;
    private AnyChartView anyChartView2;
    private Cartesian cartesian;
    private Set cartesianSet;

    private List<DataEntry> seriesData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment);

        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView2 = findViewById(R.id.any_chart_view2);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // fai! we dont have an accelerometer!
        }




        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        setChartCircular();
        anyChartView.setChart(circularGauge);

        APIlib.getInstance().setActiveAnyChartView(anyChartView2);
        setChartCartesian();
        anyChartView2.setChart(cartesian);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        lastX = event.values[0];
        lastY = event.values[1];
        lastZ = event.values[2];

        if(deltaX > 0.5 || deltaX == 0) {
            updateCharts((double) deltaX);
        }
        else updateCharts(0D);
    }


    private void updateCharts(double acceleration) {
        double roundOffAcceleration = (double) Math.round(acceleration * 100) / 100;
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        circularGauge.label(1)
                .text("<span style=\"font-size: 20\">" + roundOffAcceleration + "</span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER);
        SingleValueDataSet accelerationValue = new SingleValueDataSet(new Double[] { acceleration });
        circularGauge.data(accelerationValue);

        APIlib.getInstance().setActiveAnyChartView(anyChartView2);
        cartesianSet.append("{x: +" + getTimeSecs() + ", value : "+ acceleration +"}");

    }

    private long lastTimeMillis = System.currentTimeMillis();
    private float getTimeSecs() {
        float secs =  (float)(((double)System.currentTimeMillis() - (double)lastTimeMillis)/1000);
        float roundOffSecs = (float) Math.round(secs * 2) / 2;
        return roundOffSecs;
    }

    private void setChartCartesian() {
        cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Accélération en fonction du temps");

        cartesian.yAxis(0).title("Accélération (m.s-2)");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        cartesianSet = Set.instantiate();
        seriesData.add(new ValueDataEntry("2001", 13.5));
        seriesData.add(new ValueDataEntry("2002", 14.8));
        seriesData.add(new ValueDataEntry("2003", 16.6));
        /*
        for(int x = 0; x < 50; x++) {
            cartesianSet.append("{x: +" + x + ", value : "+ Math.random()*500 +"}");
        }*/

        Mapping series1Mapping = cartesianSet.mapAs("{ x: 'x', value: 'value' }");
        Line series1 = cartesian.line(series1Mapping);
        series1.name("Accélération");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
    }

    private void setChartCircular() {
        circularGauge = AnyChart.circular();

        circularGauge.animation(true);
        //circularGauge.autoRedraw(true);
        circularGauge.fill("#fff")
                .stroke(null)
                .padding(0, 0, 0, 0)
                .margin(30, 30, 30, 30);
        circularGauge.startAngle(0)
                .sweepAngle(360);

        SingleValueDataSet accelerationValue = new SingleValueDataSet(new Double[] { (double) deltaX });
        circularGauge.data(accelerationValue);

        circularGauge.axis(0)
                .startAngle(-150)
                .radius(80)
                .sweepAngle(300)
                .width(3)
                .ticks("{ type: 'line', length: 4, position: 'outside' }");

        circularGauge.axis(0).labels().position("outside");

        circularGauge.axis(0).scale()
                .minimum(0)
                .maximum(100);

        circularGauge.axis(0).scale()
                .ticks("{interval: 10}")
                .minorTicks("{interval: 5}");

        circularGauge.needle(0)
                .stroke(null)
                .startRadius("6%")
                .endRadius("38%")
                .startWidth("2%")
                .endWidth(0);

        circularGauge.cap()
                .radius("4%")
                .enabled(true)
                .stroke(null);

        circularGauge.label(0)
                .text("<span style=\"font-size: 18\">Accélération</span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER);
        circularGauge.label(0)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(100)
                .padding(15, 20, 0, 0);

        circularGauge.label(1)
                .text("<span style=\"font-size: 20\">" + deltaX + "</span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER);
        circularGauge.label(1)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(-100)
                .padding(5, 10, 0, 0)
                .background("{fill: 'none', stroke: '#c1c1c1', corners: 3, cornerType: 'ROUND'}");

        circularGauge.range(0,
                "{\n" +
                        "    from: 0,\n" +
                        "    to: 25,\n" +
                        "    position: 'inside',\n" +
                        "    fill: 'green 0.5',\n" +
                        "    stroke: '1 #000',\n" +
                        "    startSize: 6,\n" +
                        "    endSize: 6,\n" +
                        "    radius: 80,\n" +
                        "    zIndex: 1\n" +
                        "  }");

        circularGauge.range(1,
                "{\n" +
                        "    from: 65,\n" +
                        "    to: 100,\n" +
                        "    position: 'inside',\n" +
                        "    fill: 'red 0.5',\n" +
                        "    stroke: '1 #000',\n" +
                        "    startSize: 6,\n" +
                        "    endSize: 6,\n" +
                        "    radius: 80,\n" +
                        "    zIndex: 1\n" +
                        "  }");
    }
    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }
}
