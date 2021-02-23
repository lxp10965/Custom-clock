package com.xpl.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 传感器
 */
public class MainActivity extends AppCompatActivity {

    SensorManager mSensorManager;

    MyListener myListener;
    private TextView tv_angle;
    private TextView tv_light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();

        // 获取传感器的服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 得到角度传感器
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        myListener = new MyListener();
        mSensorManager.registerListener(myListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void findView() {
        tv_angle=(TextView) findViewById(R.id.tv_angle);
        tv_light=(TextView) findViewById(R.id.tv_light);
    }

    private class MyListener implements SensorEventListener {
        /**
         * Called when there is a new sensor event.  Note that "on changed"
         * is somewhat of a misnomer, as this will also be called if we have a
         * new reading from a sensor with the exact same sensor values (but a
         * newer timestamp).
         * 当有一个新的传感器事件时调用
         */
        @Override
        public void onSensorChanged(SensorEvent event) {
            float light = event.values[0];
            System.out.println("light:" + light);
//            Toast.makeText(MainActivity.this,"angle:" + angle,Toast.LENGTH_SHORT).show();
            tv_light.setText("light:" + light);
        }

        /**
         * Called when the accuracy of the registered sensor has changed.  Unlike
         * onSensorChanged(), this is only called when this accuracy value changes.
         * 当所注册传感器的精度发生变化时调用
         */
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    @Override
    protected void onDestroy() {
        mSensorManager.unregisterListener(myListener);
        super.onDestroy();
    }
}
