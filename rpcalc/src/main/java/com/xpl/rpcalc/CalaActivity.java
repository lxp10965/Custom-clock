package com.xpl.rpcalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

public class CalaActivity extends Activity {

    static final String TAG = "CalaActivity";
    private TextView tv_name;
    private TextView tv_result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_result = (TextView) findViewById(R.id.tv_result);

        Intent intent = getIntent();
        int sex = intent.getIntExtra("sex", 0);
        String name = intent.getStringExtra("name");
        byte[] bytes;
        switch (sex) {
            case Sex.MALL:
                bytes = name.getBytes(); //返回生成的字节数组b
                showArray(bytes, Sex.MALL);
                break;
            case Sex.FEMAIL:
                bytes = name.getBytes(); //返回生成的字节数组b
                showArray(bytes, Sex.FEMAIL);
                break;
            case Sex.UNKNOW:
                bytes = name.getBytes(); //返回生成的字节数组b
                showArray(bytes, Sex.UNKNOW);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sex);
        }
        int score = 0;
        for (byte b : bytes) {
            score = score + b & 0xff; //  b & 0xff 的含义 : 将 byte 类型转换成 int 类型
            Log.d(TAG, "onCreate: "+score);
        }
        score = Math.abs(score) % 100;
        tv_name.setText(name);
        tv_result.setText("人品值为：" + score);
    }

    private void showArray(byte[] bytes, int sex) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] += sex;
            Log.d("TAG", "showArray: " + bytes[i]);
        }
        System.out.println();
    }
}
