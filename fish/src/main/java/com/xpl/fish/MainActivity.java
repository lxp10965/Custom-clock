package com.xpl.fish;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xpl.alipay.IServer;


public class MainActivity extends AppCompatActivity {

    private MyConn conn = null;
    private IServer iService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        intent.setAction("com.xpl.alipay"); //远程启动服务
        intent.setPackage("com.xpl.alipay"); //5.0后必须用显式调用
        conn = new MyConn();
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    /**
     * 调用远程服务的方法，支付2元
     *
     * @param view
     */
    public void click(View view) {
        try {
            int resultCode = iService.callSafePay("zhangsan", "1234", 2000.00f, System.currentTimeMillis());
            switch (resultCode) {
                case 200:
                    Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                    break;
                case 404:
                    Toast.makeText(this, "银行卡余额不足", Toast.LENGTH_SHORT).show();
                    break;
                case 300:
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            System.out.println("成功绑定到远程alipay服务上");
            iService = IServer.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
