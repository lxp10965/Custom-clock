package com.xpl.alipay;

import android.os.RemoteException;

public interface MyBinder {
    int callSafePay(String username, String password, float money, long timestamp) throws RemoteException;
}
