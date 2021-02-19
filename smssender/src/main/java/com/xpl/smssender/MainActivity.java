package com.xpl.smssender;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    EditText edit_phone;
    EditText edit_sms;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_phone=(EditText) findViewById(R.id.edit_phone);
        edit_sms=(EditText) findViewById(R.id.edit_sms);
        button=(Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone_num=edit_phone.getText().toString().trim();
                final String phone_sms=edit_sms.getText().toString().trim();

                if ("".equals(phone_num)&&"".equals(phone_sms)){
                    Toast.makeText(MainActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("phone_num :"+phone_num, "phone_sms : "+phone_sms);

//                performCodeWithPermission("发送短信",new PermissionCallback(){
//
//                    @Override
//                    public void hasPermission() {
//                        //获取短信管理对象
//                        SmsManager smsManager=SmsManager.getDefault();
//                        smsManager.sendTextMessage(phone_num,null,phone_sms,null,null);
//                    }
//
//                    @Override
//                    public void noPermission() {
//
//                    }
//                }, Manifest.permission.SEND_SMS);
//                //获取短信管理对象
                SmsManager smsManager=SmsManager.getDefault();
                smsManager.sendTextMessage(phone_num,null,phone_sms,null,null);
            }
        });
    }

}
