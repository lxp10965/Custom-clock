package com.xpl.musicplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

    public static final String mp3dir = "/sdcard/Music/";

    private ListView lv;

    List<String> mp3List;
    private IMusicService iMusicSever;
    private MyConn conn;

    MusicPlayerService musicPlayerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        Log.d("TAG", "mp3dir: " + mp3dir);
        performCodeWithPermission("读取SD卡的权限", new PermissionCallback() {

            @Override
            public void hasPermission() {
                initPlayList();
            }

            @Override
            public void noPermission() {

            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE);

        Intent intent = new Intent(this, MusicPlayerService.class);
        startService(intent);//启动服务
        conn = new MyConn();
        bindService(intent, conn, BIND_AUTO_CREATE);

        Log.d(TAG, "onCreate: conn = " + conn);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (iMusicSever != null) {
                    iMusicSever.callPlay(mp3List, position);
                } else {
                    Toast.makeText(MainActivity.this, "还没有绑定服务哦", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /**
     * 初始化播放列表
     */
    private void initPlayList() {
        File file = new File(mp3dir);
        File[] files = file.listFiles();
        mp3List = new ArrayList<>();
        for (File f : files) {
            if (f.getName().endsWith(".ogg")) {
                mp3List.add(f.getAbsolutePath());
                Log.d("TAG", "initPlayList: " + f.getAbsolutePath());
            }
        }
        lv.setAdapter(new MusicListAdpater());
    }

    //菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 当菜单每个Item被点击时回调
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_setting) {
            //启动一个设置界面
            Intent intent = new Intent(this, SettingAcitity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.item_exit) {
            if (iMusicSever != null) {
                iMusicSever.callStop();//音乐暂停
            }
            if (conn != null) {
                unbindService(conn);//解绑
                conn = null;
            }
            Intent intent = new Intent(this, MusicPlayerService.class);
            stopService(intent);
            finish();//结束当前活动
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 当进程别销毁时
     */
    @Override
    protected void onDestroy() {
        if (conn != null) {
            unbindService(conn);
            conn = null;
        }
        if (MusicPlayerService.playingStatus == MusicPlayerService.MUSIC_STOP) {
            Intent intent = new Intent(this, MusicPlayerService.class);
            stopService(intent);
        }
        super.onDestroy();
    }

    private class MusicListAdpater extends BaseAdapter {

        @Override
        public int getCount() {
            return mp3List.size();
        }


        @Override
        public Object getItem(int position) {
            return mp3List.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(MainActivity.this, R.layout.item_music, null);
            TextView textView = view.findViewById(R.id.tv_item_name);
            String path = mp3List.get(position);
            textView.setText(path.substring(path.lastIndexOf("/") + 1));
            return view;
        }
    }

    private class MyConn implements ServiceConnection {
        private static final String TAG = "MyConn";

        /**
         * 连接成功的时候调用
         *
         * @param name    已连接的服务的具体组件名
         * @param service 服务通信通道的对象
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMusicSever = (IMusicService) service;
            Log.d(TAG, "onServiceConnected: " + iMusicSever.toString());
        }

        /**
         * 断开连接时         *
         *
         * @param name 连接丢失的服务的具体组件名
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    }
}
