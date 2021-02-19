package com.xpl.phone;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText et_phone;
    private Button btn_dail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_phone = findViewById(R.id.et_phone);
        btn_dail = findViewById(R.id.btn_dail);
        btn_dail.setOnClickListener(new MyButtonOnClickListener());
    }

    private class MyButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            callPhone();
        }
    }

    private void callPhone() {
        //获取EditText控件输入的号码
        String phone = et_phone.getText().toString().trim();

        if (null!=phone){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel://"+phone));
            startActivity(intent);
        }
    }
}
