package com.xpl.ipcall;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPhone;

    //持久化技术
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPhone=(EditText) findViewById(R.id.editTextPhone);
        sp= getSharedPreferences("config",MODE_PRIVATE);

        //取出
        String ipNumber = sp.getString("ipnumber","");
        editTextPhone.setText(ipNumber);
    }


    public void save(View view) {
        String ipNumber = editTextPhone.getText().toString().trim();

        //存入
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ipnumber",ipNumber);
        editor.commit();
        Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
    }
}
