package com.xpl.qqlogin;

import android.content.SharedPreferences;
import android.net.wifi.aware.DiscoverySession;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {

    private static final int GET_SERVER_RESOPENSE = 1;
    private static final int GET_ERROR = 2;
    private EditText ed_qq_num;
    private EditText ed_passwd;
    private Button bt_login;
    private Button bt_read;
    private CheckBox cb_remember;
    private SharedPreferences sp;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case GET_SERVER_RESOPENSE:
                    Toast.makeText(MainActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case GET_ERROR:
                    Toast.makeText(MainActivity.this, "登录失败，服务器或者网络出错", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp=getSharedPreferences("config",MODE_PRIVATE);
        ed_qq_num = (EditText) findViewById(R.id.ed_qq_num);
        ed_passwd = (EditText) findViewById(R.id.ed_passwd);
        cb_remember = (CheckBox) findViewById(R.id.cb_remember);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_read = (Button) findViewById(R.id.bt_read);

        boolean status = sp.getBoolean("status",false);
        cb_remember.setChecked(status);

        restoreInfo();

        cb_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //// 去设置EditText的内容
                }

                SharedPreferences.Editor editor=sp.edit();
                editor.putBoolean("status",isChecked);

                editor.commit(); //提交提交数据，类似关闭流 事务
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String qq = ed_qq_num.getText().toString().trim();
                final String passwd = ed_passwd.getText().toString().trim();
                if (TextUtils.isEmpty(qq) || TextUtils.isEmpty(passwd)) {
                    Toast.makeText(MainActivity.this, "账号密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (cb_remember.isChecked()) {
//                        Log.d("TAG", "勾选上了");
                        //创建文件操作对象
//                        try {
//                            File file = new File(MainActivity.this.getFilesDir(), "info.txt");
//                            FileOutputStream fos = new FileOutputStream(file);
//                            String info = qq+"##"+passwd;
//                            fos.write(info.getBytes());
//                            fos.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }


                        //使用SharedPreferences储存数据

                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("qq",qq);
                        editor.putString("password",passwd);
                        editor.commit();


                    }
                    if (qq.equals("10000") && passwd.equals("123456")) {
                        Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                    }
                    bt_login.setEnabled(false);
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                                String path = "http://192.168.1.130:8080/WebServer/servlet/LoginServlet?username=" + qq + "&password=" + passwd;
                                URL url = new URL(path);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                conn.setConnectTimeout(5000);
                                int code = conn.getResponseCode();
                                if (code == 200) {
                                    InputStream is = conn.getInputStream();
                                    String result = StreamUtils.readStream(is);
                                    Message msg = Message.obtain();
                                    msg.what = GET_SERVER_RESOPENSE;
                                    msg.obj = result;
                                    handler.sendMessage(msg);
                                } else {
                                    Message msg = Message.obtain();
                                    msg.what = GET_ERROR;
                                    handler.sendMessage(msg);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Message msg = Message.obtain();
                                msg.what = GET_ERROR;
                                handler.sendMessage(msg);
                            }
                        }
                    }.start();
                }

            }
        });
    }

    private void restoreInfo() {
//        File f=new File(this.getFilesDir(),"info.txt");
//        try {
//            FileInputStream fis=new FileInputStream(f);
//            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
//
//            String info = br.readLine();
//            String qq = info.split("##")[0];
//            String pwd = info.split("##")[1];
//            ed_qq_num.setText(qq);
//            ed_passwd.setText(pwd);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //从SharedPreferences中读取数据
        String qq = sp.getString("qq","");
        String passwd = sp.getString("password","");
        ed_qq_num.setText(qq);
        ed_passwd.setText(passwd);
    }

    public void click(View view) {
        Toast.makeText(this,"dianji",Toast.LENGTH_SHORT).show();
        try {
            InputStream is = this.getAssets().open("info.xml");
            // 解析info.xml文件
            // 1.得到xml文件的解析器
            XmlPullParser parser = Xml.newPullParser();

            //2.设置输入流和编码
            parser.setInput(is,"utf-8");

            //3.解析xml文件，获取当前的事件类型
            int type = parser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT){
                System.out.println(parser.getEventType()+"===="+parser.getName()+"===="+parser.getText());
//                Log.i("TAG=="+parser.getEventType(), "click: " +parser.getName()+"===="+parser.getText());
                type=parser.next();

            }

            //关闭输入流
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 在任何线程当中都可以调用弹出吐司方法
     * @param result
     */
    private void showToastInAnyThread(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
