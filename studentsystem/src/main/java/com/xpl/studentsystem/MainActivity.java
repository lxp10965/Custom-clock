package com.xpl.studentsystem;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xpl.studentsystem.db.StudentDao;
import com.xpl.studentsystem.domain.StudentInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ListView lv_info_list;
    private Button bt_add;
    private EditText ed_id;
    private EditText ed_name;
    private EditText ed_phone;
    //    List<StudentInfo> studentInfos;//学生信息  假数据
    private StudentDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1.找到控件
        // 2.模拟操作，添加假数据进行显示
        // 3.去除假数据，添加真实数据
        // 4.将真实数据写入到数据库中
        // 5.将数据库上传到服务器

        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delall:
                dao.deleteAll();
                Toast.makeText(this, "删除所有数据", Toast.LENGTH_SHORT).show();
                lv_info_list.setAdapter(new MyAdapter());
                break;
            case R.id.item_save:
//                Toast.makeText(this,"上传数据到服务器",Toast.LENGTH_SHORT).show();
                uploadDBToSever();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 上传数据到服务器
     */
    private void uploadDBToSever() {
        new Thread() {
            @Override
            public void run() {
                File file = new File("/data/data/com.xpl.studentsystem/databases/student.db");
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                //第一个参数要与Servlet中的一致
                builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                MultipartBody multipartBody = builder.build();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .header("Authorization", "Client-ID " + UUID.randomUUID())
                        .url("http://192.168.110.74/text/")
                        .post(multipartBody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
//                        Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: "+e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: ");

                    }
                });
            }
        }.start();
    }

    private void initData() {
//        studentInfos = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            StudentInfo studentInfo = new StudentInfo();
//            studentInfo.setId(i);
//            studentInfo.setName("ss" + i);
//            studentInfo.setPhone("123456" + i);
//            studentInfos.add(studentInfo);
//        }

        lv_info_list.setAdapter(new MyAdapter());
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        lv_info_list = (ListView) findViewById(R.id.lv_info_list);
        ed_id = (EditText) findViewById(R.id.ed_id);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        bt_add = (Button) findViewById(R.id.bt_add);

        dao = new StudentDao(MainActivity.this);//操作数据库对象

        bt_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                addStudent();
        }
    }

    private void addStudent() {
        String id = ed_id.getText().toString().trim();
        String name = ed_name.getText().toString().trim();
        String phone = ed_phone.getText().toString().trim();
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "数据不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setId(Integer.valueOf(id));
            studentInfo.setName(name);
            studentInfo.setPhone(phone);

//        studentInfos.add(studentInfo); //添加数据到集合中

            boolean result = dao.add(studentInfo); //添加一条数据到数据库中
            //数据添加成功就刷新Adapter
            if (result) {
                lv_info_list.setAdapter(new MyAdapter());
            }
        }
    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dao.getTotalCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(MainActivity.this, R.layout.item, null);
            TextView tv_id = (TextView) view.findViewById(R.id.tv_id);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            ImageView iv_del = (ImageView) view.findViewById(R.id.iv_del);

//            tv_id.setText(studentInfos.get(position).getId() + "");
//            tv_name.setText(studentInfos.get(position).getName());
//            tv_phone.setText(studentInfos.get(position).getPhone());

            //从数据库中获取数据
            final Map<String, String> map = dao.getStudentInfo(position);
            Log.d(TAG, "getView: " + map);
            tv_id.setText(String.valueOf(map.get("studentid")));
            tv_name.setText(map.get("name"));
            tv_phone.setText(map.get("phone"));

            iv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    studentInfos.remove(position);
                    dao.delete(map.get("studentid"));   //删除数据库中一条数据
                    lv_info_list.setAdapter(new MyAdapter());//刷新适配器
                }
            });
            return view;
        }
    }
}
