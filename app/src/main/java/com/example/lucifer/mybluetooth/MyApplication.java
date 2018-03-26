package com.example.lucifer.mybluetooth;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {


    private static Context mContext;

//    public static String IP = "192.168.1.106";
    public static String IP = "192.168.3.26";
    public static String url = "http://" + IP;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();


        ToastUtil.init(mContext);
    }

    public static Context getContext() {
        return mContext;
    }
}
