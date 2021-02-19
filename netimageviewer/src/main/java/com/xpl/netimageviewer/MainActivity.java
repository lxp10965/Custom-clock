package com.xpl.netimageviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int LOAD_ERROR = 2;//加载图片失败
    private int currentPosition;//当前显示的图片
    private static final int LOAD_IMAGE = 1;//成功加载缓存中的图片

    ImageView imageView;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case LOAD_IMAGE:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    imageView.setImageBitmap(bitmap);
                    break;
                case LOAD_ERROR:
                    Toast.makeText(MainActivity.this,"网络异常，获取图片失败",Toast.LENGTH_SHORT).show();
                    break;

            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.iv);

        //连接服务器获取资源
        loadAllImagePath();
    }

    public void loadAllImagePath() {
        Log.d(TAG, "loadAllImagePath: ");
        //不要再主线程做耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "run1: ");
                // 浏览器发送一个get请求就可以把服务器的数据获取出来
                // 用代码模拟一个http的get请求
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://192.168.110.74/gaga.html");
                    connection = (HttpURLConnection) url.openConnection();//获取HTTP请求
                    connection.setRequestMethod("GET");//从服务器请求获取数据
                    connection.setConnectTimeout(8000);// 连接超时
                    connection.setReadTimeout(8000);//读取超时
                    int code = connection.getResponseCode();
                    Log.d(TAG, "code: " + code);
                    if (code == 200) {// 返回成功
                        InputStream is = connection.getInputStream();
                        File file = new File(getCacheDir(), "info.txt");
                        Log.d(TAG, "getCacheDir: " + getCacheDir());
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        is.close();
                        fos.close();

                        beginLoadImage();

                    } else if (code == 404) {
                        Message msg = Message.obtain();
                        msg.what = LOAD_ERROR;
                        msg.obj = "获取html失败，返回码：" + code;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = LOAD_ERROR;
                        msg.obj = "服务器异常，返回码：" + code;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }).start();
    }

    ArrayList<String> arrayList;

    /**
     * 取出图片地址
     */
    private void beginLoadImage() {
        try {
            arrayList = new ArrayList<>();
            File file = new File(getCacheDir(), "info.txt");
            FileInputStream fis = new FileInputStream(file);
            BufferedReader bf = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = bf.readLine()) != null) {
                arrayList.add(line);
            }
            fis.close();
            bf.close();

            loadImageByPath(arrayList.get(currentPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过路径获取图片，缓存中没有就重网络中获取
     *
     * @param path
     */
    private void loadImageByPath(final String path) {
        new Thread() {
            @Override
            public void run() {
                //图片输出到缓存中的地址
                File file = new File(getCacheDir(), path.replace("/", "") + ".jpg");
                if (file.exists() && file.length() > 0) {
                    Log.d(TAG, "run: 取出缓存中的图片");
                    Message msg = Message.obtain();//返回一个消息对象
                    msg.what = LOAD_IMAGE;
                    msg.obj = BitmapFactory.decodeFile(file.getAbsolutePath());//绝对路径
                    handler.sendMessage(msg);
                } else {
                    Log.d(TAG, "run: 缓存中没有，从网络中获取");
                    try {
                        URL url = new URL(path);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        //设置请求方式
                        conn.setRequestMethod("GET");
                        //返回请求状态码
                        int code = conn.getResponseCode();
                        if (200 == code) {
                            //获取输入流
                            InputStream cis = conn.getInputStream();
                            //用位图对象取出，放到内存中
                            Bitmap bitmap = BitmapFactory.decodeStream(cis);
                            //放到缓存中
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            //记得关闭流
                            fos.close();
                            cis.close();

                            //更新UI
                            Message message = Message.obtain();
                            message.what = LOAD_IMAGE;
                            message.obj = BitmapFactory.decodeFile(file.getAbsolutePath());
                            handler.sendMessage(message);
                        } else if (code == 404) {
                            Message message = Message.obtain();
                            message.what = LOAD_ERROR;
                            message.obj = "获取图片失败" + code;
                            handler.sendMessage(message);
                        } else {
                            Message message = Message.obtain();
                            message.what = LOAD_ERROR;
                            message.obj = "获取图片失败" + code;
                            handler.sendMessage(message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Message message = Message.obtain();
                        message.what = LOAD_ERROR;
                        message.obj = "获取图片失败";
                        handler.sendMessage(message);
                    }
                }

            }
        }.start();
    }


    /**
     * 下一张
     *
     * @param view
     */
    public void next(View view) {
        currentPosition++;
        if (currentPosition == arrayList.size()) {
            currentPosition = 0;
        }
        loadImageByPath(arrayList.get(currentPosition));
    }

    /**
     * 上一张图片
     *
     * @param view
     */
    public void pre(View view) {
        currentPosition--;
        if (currentPosition<0)
            currentPosition=arrayList.size()-1;
        loadImageByPath(arrayList.get(currentPosition));
    }
}
