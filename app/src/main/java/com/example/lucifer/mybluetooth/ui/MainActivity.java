package com.example.lucifer.mybluetooth.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


import com.example.lucifer.mybluetooth.receiver.BTScanReceiver;
import com.example.lucifer.mybluetooth.adapter.BaseRvAdapter;
import com.example.lucifer.mybluetooth.adapter.MyAdapter;
import com.example.lucifer.mybluetooth.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.lucifer.mybluetooth.R.id.device;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Switch openBluetooth;
    private SwitchCompat aSwitchCompat;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private String[] Name = {"搜索设备显示"};
    private BluetoothGatt mGatt = null;
    private BluetoothDevice device2;
    private static HashMap<String, String> map = new HashMap();
    private List<BluetoothDevice> bluetoothLists = new ArrayList<>();
    private MyAdapter devicesAdapter;
    private RecyclerView devicesRv;
    private BTScanReceiver bluetoothReceiver;
    private Button searchBtn;
    private Button refreshBtn;



    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        check();
        initReceiver();


        //实例化
        openBluetooth = (Switch) findViewById(R.id.open);
        openBluetooth.setChecked(mBluetoothAdapter.isEnabled());

        //设置Switch事件监听
        openBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    mBluetoothAdapter.enable();//打开蓝牙
                } else {
                    mBluetoothAdapter.disable();// 关闭蓝牙
                }
            }
        });

        //扫描到的蓝牙设备
        //搜索按钮
        searchBtn = (Button) findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startSearch();
                checkBluetoothPermission();
            }
        });

        //刷新按钮
        refreshBtn = (Button) findViewById(R.id.up);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                mBluetoothAdapter.cancelDiscovery();
                bluetoothLists.clear();
                mBluetoothAdapter.startDiscovery();
             /*   Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
                for (Iterator iterator = devices.iterator(); iterator.hasNext(); ) {
                    BluetoothDevice device = (BluetoothDevice) iterator.next();
                    bluetoothLists.add(device);
                }*/
            }
        });


        devicesAdapter = new MyAdapter();
        devicesRv = (RecyclerView) findViewById(device);
        devicesRv.setLayoutManager(new LinearLayoutManager(this));
        devicesRv.setAdapter(devicesAdapter);

        devicesAdapter.setData(bluetoothLists);

        devicesAdapter.setOnItemClickListener(new BaseRvAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                // position是当前点击的行；根据它可以获得容器中对应的值
                String name = bluetoothLists.get(position).getName();
//               String address = map.get(name);
                String address = bluetoothLists.get(position).getAddress();
                Intent intent = new Intent(MainActivity.this, DealActivity.class);

                intent.putExtra("extra_name", name);
                intent.putExtra("extra_address", address);

                startActivity(intent);
            }
        });
    }

    private void initReceiver() {

        //注册广播接收器
        bluetoothReceiver = new BTScanReceiver();
        bluetoothReceiver.setOnStateChangeListener(new BTScanReceiver.OnStateChangeListener() {
            @Override
            public void onStart() {
                Log.i(TAG, "扫描蓝牙开始");
            }

            @Override
            public void onFound(BluetoothDevice device) {
                addData(device);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "扫描蓝牙完成");

            }
        });
        registerReceiver(bluetoothReceiver, bluetoothReceiver.getIntentFilter());
    }

    private void check() {
        //不支持BLE则直接退出
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "该手机不支持此设备", Toast.LENGTH_SHORT).show();
            finish();
        }

        //获取蓝牙适配器
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //不支持蓝牙则直接退出
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


    }

    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        100);
            } else {
                //具有权限
                startSearch();
            }
        } else {
            //系统不高于6.0直接执行
            startSearch();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意权限
                startSearch();
            } else {
                // 权限拒绝
                // 下面的方法最好写一个跳转，可以直接跳转到权限设置页面，方便用户
                Toast.makeText(this, "用户拒绝权限", Toast.LENGTH_SHORT);
                Log.i(TAG, "用户拒绝权限");
            }

        }


    }


    private void startSearch() {
        bluetoothLists.clear();

        BluetoothLeScanner bluetoothLeScanner;
        if (Build.VERSION.SDK_INT >= 23) {
            bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            bluetoothLeScanner.startScan(new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    Log.i(TAG, "onScanResult--->" + callbackType);
                    if (Build.VERSION.SDK_INT >= 23 && callbackType == ScanSettings.CALLBACK_TYPE_FIRST_MATCH) {
                        BluetoothDevice device = result.getDevice();
                        Log.i(TAG, device.getName() + " " + device.getAddress());
                    }

                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    Log.i(TAG, "onBatchScanResults--->");
                    for (ScanResult result : results) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            BluetoothDevice device = result.getDevice();
                            Log.i(TAG, device.getName() + " " + device.getAddress());
                        }
                    }
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                }
            });


            bluetoothLeScanner.stopScan(new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    super.onBatchScanResults(results);
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                }
            });
        } else {
            mBluetoothAdapter.startDiscovery();
        }
    }


    private void addData(BluetoothDevice device) {
        bluetoothLists.add(device);
        devicesAdapter.notifyDataSetChanged();

    }

}

