package com.example.lucifer.mybluetooth.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


/**
 * 接收扫描蓝牙设备列表广播
 */
public class BTScanReceiver extends BroadcastReceiver {

    private final String TAG = BTScanReceiver.class.getSimpleName();
    IntentFilter intentFilter;

    public BTScanReceiver() {
        intentFilter = new IntentFilter();
        /**
         * 接收这三个广播
         */
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
    }




    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        switch (action) {
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                if (onStateChangeListener != null)
                    onStateChangeListener.onStart();
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                if (onStateChangeListener != null)
                    onStateChangeListener.onFinish();
                break;
            case BluetoothDevice.ACTION_FOUND:
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i(TAG, "device name:" + device.getName() + " " + device.getAddress());
                if (onStateChangeListener != null)
                    onStateChangeListener.onFound(device);
                break;
        }
    }

    public IntentFilter getIntentFilter() {
        return intentFilter;
    }

    private OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public interface OnStateChangeListener {
        /**
         * 开始扫描
         */
        void onStart();

        /**
         * 发现蓝牙设备
         *
         * @param device 发现的设备
         */
        void onFound(BluetoothDevice device);

        /**
         * 扫描结束
         */
        void onFinish();

    }


}

