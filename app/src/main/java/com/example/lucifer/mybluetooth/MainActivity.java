package com.example.lucifer.mybluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.lucifer.mybluetooth.R.id.device;

public class MainActivity extends AppCompatActivity {
    private Switch aSwitch;
    private SwitchCompat aSwitchCompat;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private String[] Name={"搜索设备显示"};
    private BluetoothGatt mGatt = null;
    private BluetoothDevice device2;
    private static HashMap<String, String> map = new HashMap();
    private List<String> bluetoothLists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
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
        //实例化
        aSwitch = (Switch) findViewById(R.id.open);

        //设置Switch事件监听
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    mBluetoothAdapter.enable();//打开蓝牙
                } else {
                    mBluetoothAdapter.disable();// 关闭蓝牙
                }
            }
        });

        //搜索按钮
        Button searchBtn = (Button)findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                bluetoothLists.clear();
                mBluetoothAdapter.startDiscovery();
            }
        });

        //刷新按钮
        Button refreshBtn = (Button)findViewById(R.id.up);
        refreshBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v2){
                initListView();
            }
        });

        //扫描到的蓝牙设备

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        BlueToothReceiver bluetoothReceiver = new BlueToothReceiver();

        //注册广播接收器
        registerReceiver(bluetoothReceiver,intentFilter);


        // 若蓝牙列表数据更新后，可以用以下方法通知ListView更新显示

}

    //接收广播
    private  class BlueToothReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                device2 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                for(int i = 0; i<= bluetoothLists.size(); i++){
                    if(!bluetoothLists.contains(device2.getName()))
                        bluetoothLists.add(device2.getName());
                        Name = bluetoothLists.toArray(new String[0]);
                        map.put(device2.getName(), device2.getAddress());

                }

            }
        }

    }

    //listview
   public void initListView() {

       ArrayAdapter<String> adapter = new ArrayAdapter<String>(
               MainActivity.this, android.R.layout.simple_list_item_1, Name);
       ListView listView = (ListView) findViewById(device);
       listView.setAdapter(adapter);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           @Override
           public void onItemClick(AdapterView<?> parent, View view,
                                   int position, long id) {
               // position是当前点击的行；根据它可以获得容器中对应的值
               String name = bluetoothLists.get(position);
               String address = map.get(name);
               Intent intent = new Intent(MainActivity.this,DealActivity.class);

               intent.putExtra("extra_name", name);
               intent.putExtra("extra_address", address);

               startActivity(intent);

           }
       });
   }

}

