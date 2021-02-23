// IServer.aidl
package com.xpl.alipay;

// Declare any non-default types here with import statements

interface IServer {
    int callSafePay(String username, String password, float money, long timestamp);
}
