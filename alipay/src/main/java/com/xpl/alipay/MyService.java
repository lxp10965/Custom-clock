package com.xpl.alipay;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;



public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        Log.d("TAG", "onCreate: ");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("TAG", "onDestroy: ");
        super.onDestroy();
    }

    private class MyBinder extends IServer.Stub {

        @Override
        public int callSafePay(String username, String password, float money, long timestamp) throws RemoteException {
            return safePay(username, password, money, timestamp);
        }
    }

    /**
     * 安全支付的服务方法
     *
     * @param username
     *          用户名
     * @param password
     *          密码
     * @param money
     *          钱
     * @param timestamp
     *          时间戳
     * @return
     */
    public int safePay(String username, String password, float money, long timestamp) {
        System.out.println("加密的username");
        System.out.println("加密的password");
        System.out.println("提交数据到支付宝的服务器");
        if (money > 5000) {
            return 404;// 支付超过了当日限额
        }
        if ("zhangsan".equals(username) && "123".equals(password)) {
            return 200;
        } else {
            return 300;
        }
    }
}
