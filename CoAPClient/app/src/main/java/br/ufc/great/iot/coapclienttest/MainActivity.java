package br.ufc.great.iot.coapclienttest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.ws4d.coap.Constants;
import org.ws4d.coap.interfaces.CoapChannelManager;
import org.ws4d.coap.interfaces.CoapClient;
import org.ws4d.coap.interfaces.CoapClientChannel;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SimpleCoapClient client;
    private SensorManager mSensorManager;
    private Sensor mLuminosity;

    private TextView tvLumi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLumi = (TextView) findViewById(R.id.lum_value);

        client = SimpleCoapClient.getInstance();
        client.connect(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLuminosity = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLuminosity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lum = event.values[0];
        Log.d("Luminosity", lum + "");
        tvLumi.setText(lum + "");
        client.updateLuminosity(lum);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
