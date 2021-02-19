package com.xpl.fileupload;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xpl.fileupload.loopj.android.http.AsyncHttpClient;
import com.xpl.fileupload.loopj.android.http.AsyncHttpResponseHandler;
import com.xpl.fileupload.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.ed_text);

        File file = new File(getCacheDir(), "upload.txt");
        Log.d("TAG", "onCreate: " + getCacheDir());
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("upload");
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void upload(View view) {
        String path = editText.getText().toString().trim();
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(path);
        if (file.exists() && file.length() > 0) {
            try {
//                FileInputStream fis=new FileInputStream(file);
//                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
//                String info = br.readLine();
//                Log.d("TAG", "upload: " + info);

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("file", file);
                client.post("http://192.168.110.74/text", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "文件不存在或者内容为空", Toast.LENGTH_SHORT).show();
        }

    }
}
