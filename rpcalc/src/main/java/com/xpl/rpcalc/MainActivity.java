package com.xpl.rpcalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private EditText et_name;
    private RadioGroup rg_sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 人品计算器：通过输入姓名和性别，然后点击计算
         * 跳转到第二个页面显示人品值
         */
        // 开发步骤：
        // 1.找到控件，并设置事件
        // 2.在点击事件当中，获取输入的信息
        // 3.将输入的信息设置到intent对象
        // 4.利用startactivity方法，将intent对象的内容传递给activity2
        // 5.activity2获取到intent对象传递的内容并计算显示结果
        et_name = (EditText) findViewById(R.id.et_name);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
    }

    public void click(View view) {
        String name = et_name.getText().toString().trim();
        int id = rg_sex.getCheckedRadioButtonId();

        int sex = 0;
        if (id == R.id.rb_male){
            sex=Sex.MALL;
        }
        if (id == R.id.rb_female){
            sex=Sex.FEMAIL;
        }
        if (id == R.id.rb_unknow){
            sex=Sex.UNKNOW;
        }

        Intent intent = new Intent();
        intent.putExtra("name",name);
        intent.putExtra("sex",sex);
        intent.setAction("com.glandroid.rpcalc.openCalc");
        startActivity(intent);
    }
}
