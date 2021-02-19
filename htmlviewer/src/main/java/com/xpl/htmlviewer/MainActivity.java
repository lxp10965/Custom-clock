package com.xpl.htmlviewer;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xpl.htmlviewer.utils.StreamUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText ed_net;
    private Button btn_net;
    private TextView tv_net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取控件
        ed_net = (EditText) findViewById(R.id.ed_net);
        btn_net = (Button) findViewById(R.id.btn_net);
        tv_net = (TextView) findViewById(R.id.tv_net);

        btn_net.setOnClickListener(this);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String tv_obj = (String) msg.obj.toString();
            tv_net.setText(tv_obj);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_net:
                viewHTMLSource();
                break;
        }
    }

    /**
     * 查看网页源码，访问网络的操作：1.声明权限，2.开启子线程
     *
     * @param view
     */
    private void viewHTMLSource() {
        final String url_str = ed_net.getText().toString().trim();
        if (TextUtils.isEmpty(url_str)) {
            Toast.makeText(this, "网址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(url_str);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    InputStream is = conn.getInputStream();
//                    BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
//                    StringBuffer sb= new StringBuffer();
//                    String len;
//                    while ((len = bfr.readLine())!=null){
//                        sb.append(len);
//                    }

                    //解析编码
                    int code = conn.getResponseCode();
                    if (200 == code) {
                        String text=StreamUtils.readStream(is);
                        Message msg = Message.obtain();
                        msg.obj = text;
                        msg.what = 100;
                        handler.sendMessage(msg);
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
