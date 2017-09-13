package com.example.lucifer.mybluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 连接蓝牙
 * 球拍蓝牙名:iimpleBLEPeripheral
 */
public class DealActivity extends AppCompatActivity {

    private static final String TAG = DealActivity.class.getSimpleName();
    public String address;
    private MyService mBluetoothLeService;
    private TextView try1;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    public ArrayList<BluetoothGattCharacteristic> characteristics;// 周边的characteristics的ArrayList数组
    private boolean run_state = false;

    static int x;
    static int y;
    private MyDatabaseHelper dbHelper;//数据库帮助类
    int b = 1;//压力次数
    int c = 1;//加速度次数
    int d = 1;//角度次数
    public int temp0;//记录运动时间为几分钟
    String day;//提取本机当前时间
    public int cal;//计算出的消耗卡路里
    public double acceleration11;//数据库中查出的最大加速度


    int i = 0;
    float p1_max;
    float p1_min;
    float p1_time;
    float p2_max;
    float p2_min;
    float p2_time;
    int test = 0;
    int hit_count = 0;
    private float g = (float) 9.8;
    int time1 = 0;

    private float a_xx;
    private float a_yy;
    private float a_zz;
    private float jiaodu_x;
    private float jiaodu_y;
    private float jiaodu_z;

    private ProgressDialog progressDialog;
    private Chronometer timer;
    private Button button;
    private Button button3;
    private Button button2;
    private int flag = 0;
    private long mRecordTime;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((MyService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("DealActivity", "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(address);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    //广播接收
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (MyService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.i(TAG, "处于连接状态");
            } else if (MyService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.i(TAG, "处于未连接状态");
                Intent intent2 = new Intent(DealActivity.this, MainActivity.class);
                startActivity(intent2);
                Toast.makeText(DealActivity.this, "连接断开请重试", Toast.LENGTH_SHORT).show();
            } else if (MyService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                Log.i(TAG, "已连接到服务");
                progressDialog.dismiss();
                characteristics = getCharacteristic(); // 获取所有的characteristic
                setNotifyReceive(characteristics); // 给这些特性加通知
            } else if (MyService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.i(TAG, "数据可获得");

                if (run_state) {
                    byte[] data = intent
                            .getByteArrayExtra(mBluetoothLeService.PINGPANG_DATA);
                    test = test + 1;
                    Log.i(TAG, "111=test2=" + test);
                    if (data != null && data.length > 0) {
                        final StringBuilder stringBuilder = new StringBuilder(
                                data.length);
                        for (byte byteChar : data)      //将数据data中的byteChar数据遍历得出
                            stringBuilder.append(String.format("%02X ", byteChar));//将得到的byteChar格式化为16进制，超过两位则正常输出，不足两位前面补0
                        String str = stringBuilder.toString().trim();
                        Log.i(TAG, "111=" + str);
                        String[] date = str.split(" ");//将字符串分割为字符串数组

                        switch (Integer.parseInt(date[2])) {
                            case 53:
                                Log.i(TAG, "111=写压力");
                                hit_count = hit_count + 1;
                                try1.setText("击球次数：      " + hit_count);

                                p1_max = realValue(date[6], date[5]);
                                p1_min = realValue(date[8], date[7]);
                                p1_time = realValue(date[10], date[9]);

                                /*Tb_pressure tb_pressure = new Tb_pressure(
                                        Pressure_DAO.getMaxId() + 1, account_id, time,
                                        1, p1_max, p1_min, p1_time,0);
                                Pressure_DAO.add(tb_pressure);*/

                                p2_max = realValue(date[12], date[11]);
                                p2_min = realValue(date[14], date[13]);
                                p2_time = realValue(date[16], date[15]);

                                /*Tb_pressure tb_pressure1 = new Tb_pressure(
                                        Pressure_DAO.getMaxId() + 1, account_id, time,
                                        2, p2_max, p2_min, p2_time,0);
                                Pressure_DAO.add(tb_pressure1);*/

                                int lidu = (int) (p1_max + p1_min + p2_max + p2_min) / 20;
                               /* ProgressBar1.setProgress(lidu);
                                li_view.setText("" + lidu);*/
                                SQLiteDatabase lidutable = dbHelper.getWritableDatabase();

                                ContentValues values = new ContentValues();
                                values.put("date", day + "开始");
                                values.put("time", b);
                                b++;
                                values.put("lidu", lidu);
                                lidutable.insert("lidu", null, values);
                                values.clear();

                                Log.i(TAG, "压力数据可存储");

                                break;
                            case 51:
                                a_xx = realValue(date[6], date[5]);
                                a_yy = realValue(date[8], date[7]);
                                a_zz = realValue(date[10], date[9]);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        SQLiteDatabase acctable = dbHelper.getWritableDatabase();


                                        float a_x = a_xx / 32768 * 16 * g;
                                        float a_y = a_yy / 32768 * 16 * g;
                                        float a_z = a_zz / 32768 * 16 * g;
                                        float join_data = (float) Math.sqrt(a_x * a_x + a_y * a_y
                                                + a_z * a_z);//单位为 m/(s*s)

                                        ContentValues values2 = new ContentValues();
                                        values2.put("date", day + " 开始");
                                        values2.put("time", c);
                                        c++;
                                        values2.put("acceleration", join_data);
                                        acctable.insert("ACCELERATION", null, values2);
                                        values2.clear();

                                        Log.i(TAG, "可存储加速度数据");
                                    }
                                }).start();

                                break;

//							 System.out.println("111=写加速度,a_x="+a_x+",a_y="+a_y+",a_z="+a_z);
//							  Tb_a tb_a = new Tb_a(Tb_a_DAO.getMaxId() + 1,
//							  account_id, time, a_x, a_y, a_z,join_data);
//							  Tb_a_DAO.add(tb_a);
//							break;
                            case 52:
                                jiaodu_x = realValue(date[6], date[5]);
                                jiaodu_y = realValue(date[8], date[7]);
                                jiaodu_z = realValue(date[10], date[9]);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteDatabase angletable = dbHelper.getWritableDatabase();

                                        float w_x = jiaodu_x / 32768 * 2000;
                                        float w_y = jiaodu_y / 32768 * 2000;
                                        float w_z = jiaodu_z / 32768 * 2000;

                                        ContentValues values3 = new ContentValues();
                                        values3.put("date", day + " 开始");
                                        values3.put("time", d);
                                        d++;
                                        values3.put("angle_x", w_x);
                                        values3.put("angle_y", w_y);
                                        values3.put("angle_z", w_z);
                                        angletable.insert("ANGLE", null, values3);
                                        values3.clear();

                                        Log.i(TAG, "可存储角度数据");
                                    }
                                }).start();
//
//							 Tb_w tb_w = new
//							 Tb_w(Tb_w_DAO.getMaxId()+1,account_id,time,w_x,w_y,w_z);
//							  Tb_w_DAO.add(tb_w);
//							break;
                            default:
                                //		System.out.println("111=同步数据");
                               /* Tb_init_data tb_init_data = new Tb_init_data(
                                        Tb_init_data_DAO.getMaxId() + 1,time, String.valueOf(account_id),date[2],date[3],date[4],date[5],
                                        date[6],date[7],date[8],date[9],date[10],date[11],date[12],date[13],date[14],
                                        date[15],date[16],date[17]);
                                Tb_init_data_DAO.add(tb_init_data);*/
                                break;
                        }

                    }
                }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        dbHelper = new MyDatabaseHelper(this, "BlueTooth.db", null, 2);//建立数据库


        progressDialog = new ProgressDialog(DealActivity.this);
        progressDialog.setTitle("蓝牙连接");
        progressDialog.setMessage("正在连接，请稍后");
        progressDialog.setCancelable(true);
        progressDialog.show();

        timer = (Chronometer) findViewById(R.id.timer);
        //开始计时按钮
        button = (Button) findViewById(R.id.start);
        button.setOnClickListener(mStartListener);
        //停止计时按钮
        button2 = (Button) findViewById(R.id.stop);
        button2.setOnClickListener(mStopListener);
        //暂停计时按钮
        button3 = (Button) findViewById(R.id.again);
        button3.setOnClickListener(mAgainListener);

        Intent intent = getIntent();
        address = intent.getStringExtra("extra_address");


        Intent gattServiceIntent = new Intent(this, MyService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);


        Button button = (Button) findViewById(R.id.button);//查看结果按钮
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (time1 == 1) {

                    // SQLiteDatabase db = dbHelper.getReadableDatabase();
                    //Cursor cursor = db.query("ACCELERATION",new String[]{"date","acceleration"}, "date = ?", day,"acceleration", null, "");
                    //if(cursor.moveToNext())
                    //{
                    //  acceleration11 = cursor.getDouble(cursor.getColumnIndex("acceleration"));
                    //Log.e("DealActivity","加速度LLLLLLLOG"+acceleration11);
                    //}


                    Intent intent = new Intent(DealActivity.this, CountActivity.class);

                    temp0 = Integer.parseInt(timer.getText().toString().split(":")[0]);
                    Log.i(TAG, "现在的计时为" + temp0 + "分");
                    cal = temp0 * 6;//每小时消耗360千卡，则每分钟约消耗6千卡

                    intent.putExtra("cal_data", " " + cal + " ");
                    intent.putExtra("acceleration_data", " " + acceleration11 + " ");

                    startActivity(intent);
                } else {
                    Toast.makeText(DealActivity.this, "请先停止计时", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //开始计时方法
    View.OnClickListener mStartListener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.i(TAG, "点击开始按钮");
            test = 0;
            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
            time1 = 0;
            hit_count = 0;
            temp0 = 0;

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            day = formatter.format(curDate);

            dbHelper.getReadableDatabase();

            try1 = (TextView) findViewById(R.id.cishu);

            button.setEnabled(false);
            button2.setEnabled(true);
            button3.setEnabled(true);
            run_state = true;

        }
    };
    //结束计时方法
    View.OnClickListener mStopListener = new View.OnClickListener() {
        public void onClick(View v) {
            timer.stop();
            run_state = false;
            b = 1;
            c = 1;
            d = 1;
            time1 = 1;

            button.setEnabled(true);
            button2.setEnabled(false);
            button3.setEnabled(false);
        }
    };
    //暂停继续
    View.OnClickListener mAgainListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (flag == 0) {
                timer.stop();
                mRecordTime = SystemClock.elapsedRealtime();
                flag++;
                button3.setText("继续");
                run_state = false;
            } else {
                if (mRecordTime != 0) {
                    timer.setBase(timer.getBase() + (SystemClock.elapsedRealtime() - mRecordTime));
                } else {
                    timer.setBase(SystemClock.elapsedRealtime());
                }
                timer.start();
                flag--;
                button3.setText("暂停");
                run_state = true;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(address);
            Log.i(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    // 返回的是一个arraylist，里面存的是characteristic
    public ArrayList<BluetoothGattCharacteristic> getCharacteristic() {
        ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();// 初始化charas
        List<BluetoothGattService> services = mBluetoothLeService
                .getSupportedGattServices();
        for (int i = 0; i < services.size(); i++) {
            BluetoothGattService gattService = services.get(i);// 循环获取每一个服务，
            if (gattService.getUuid().toString()
                    .equals(SampleGattAttributes.SERVICE)) {
                List<BluetoothGattCharacteristic> characteristics = gattService
                        .getCharacteristics();// 获取服务的特性
                for (BluetoothGattCharacteristic bluetoothGattCharacteristic : characteristics) {
                    charas.add(bluetoothGattCharacteristic);// 把每个特性加到charas中
                }
            }
        }
        return charas;
    }

    public void setNotifyReceive(
            ArrayList<BluetoothGattCharacteristic> characteristics) {
        System.out.println("111=SIZE=" + characteristics.size());
        if (characteristics != null && characteristics.size() > 0) {
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                if (characteristic.getUuid().toString()
                        .equals(SampleGattAttributes.HEART_RATE_MEASUREMENT)) {
                    // System.out.println("111=写通知");
                    mBluetoothLeService.setCharacteristicNotification(
                            characteristic, true);
                    mBluetoothLeService.readCharacteristic(characteristic);
                }
            }
        }
    }

    /*
     * 获取Int
	 */

    private static float realValue(String high, String low) {
        x = Integer.parseInt(high, 16);
        y = Integer.parseInt(low, 16);
        return (x << 8 | y);
        // return x * 256 + y;

    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(MyService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(MyService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(MyService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

}
