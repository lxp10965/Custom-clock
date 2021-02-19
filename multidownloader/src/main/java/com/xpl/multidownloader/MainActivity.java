package com.xpl.multidownloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText ed_url;
    private Button bt_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_url=(EditText) findViewById(R.id.ed_url);
        bt_down=(Button) findViewById(R.id.bt_down);

        bt_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = ed_url.getText().toString().trim();
//                String path = "http://192.168.110.74/text/PCQQ2020.exe";
                if (TextUtils.isEmpty(path)){
                    Toast.makeText(MainActivity.this,"地址不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                /**
                 * 用第三方控件xutils进行下载
                 */
                HttpUtils http = new HttpUtils();
                http.download(path, "/data/data/com.xpl.multidownloader/cache/" + getDownloadFileName(path)
                        , true, new RequestCallBack<File>() {
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        Toast.makeText(MainActivity.this,"下载成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(MainActivity.this,"下载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
//                        显示进度
                        Log.d(TAG, "onLoading: "+current);
                        super.onLoading(total, current, isUploading);
                    }
                });
            }
        });
    }

    private String getDownloadFileName(String path) {
        //取最后一个斜杠 / 后面的内容作为名称
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
