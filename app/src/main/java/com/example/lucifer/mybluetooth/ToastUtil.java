package com.example.lucifer.mybluetooth;


import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;

public class ToastUtil {

    Thread thread;
    HandlerThread handlerThread;

    String s = new String("ddd");

    public static void main(String[] args) {

    }

   void test() throws IOException {
       thread.start();

       Handler handler = new Handler(handlerThread.getLooper()) ;
       handler.post(new Runnable() {
           @Override
           public void run() {
               //do something
           }
       });

       Socket  socket = null ;
       socket.shutdownOutput();
    }

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void show(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
    }
}
