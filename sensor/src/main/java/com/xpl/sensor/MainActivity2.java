package com.xpl.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 传感器
 */
public class MainActivity2 extends AppCompatActivity {

    SensorManager mSensorManager;

    MyListener myListener;
    private TextView tv_angle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();

        // 获取传感器的服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 得到角度传感器
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        myListener = new MyListener();
        mSensorManager.registerListener(myListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void findView() {
        tv_angle=(TextView) findViewById(R.id.tv_angle);
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
            float angle = event.values[0];
            System.out.println("angle:" + angle);
//            Toast.makeText(MainActivity2.this,"angle:" + angle,Toast.LENGTH_SHORT).show();
            tv_angle.setText("angle:" + angle);
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
