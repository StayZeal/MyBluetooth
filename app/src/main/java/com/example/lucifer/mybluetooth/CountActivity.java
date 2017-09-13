package com.example.lucifer.mybluetooth;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CountActivity extends AppCompatActivity {
    private TextView time1;//运动卡路里显示
    private TextView acceleration1;//最大加速度显示text

    private String cal;
    private String acceleration;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        dbHelper = new MyDatabaseHelper(this,"BlueTooth.db",null,2);

        Button queryButton = (Button)findViewById(R.id.button6);
        queryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("ACCELERATION",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        String date = cursor.getString(cursor.getColumnIndex("date"));
                        int time = cursor.getInt(cursor.getColumnIndex("time"));
                        double acceleration = cursor.getDouble(cursor.getColumnIndex("acceleration"));



                        Log.e("CountActivity","时间为"+date);
                        Log.e("CountActivity","第"+time+"次");
                        Log.e("CountActivity","加速度为："+acceleration);


                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });

            Intent intent = getIntent();
            cal = intent.getStringExtra("cal_data");
  //          acceleration = intent.getStringExtra("acceleration_data");

            time1=(TextView)findViewById(R.id.time_text);
            time1.setText("消耗能量：      "+cal+"千卡");

   //         acceleration1=(TextView)findViewById(R.id.acceleration_text);
     //       acceleration1.setText("最大加速度：      "+acceleration+"m/s*s");

    }
}



