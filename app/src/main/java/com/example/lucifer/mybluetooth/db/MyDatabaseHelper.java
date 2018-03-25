package com.example.lucifer.mybluetooth.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Lucifer on 2017/4/28.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_ACCELERATION = "create table ACCELERATION("
            +"date text, "
            +"time integer, "
            +"acceleration real, "
            + "PRIMARY KEY(date,time)"+ ")";

    public static final String CREATE_ANGLE = "create table ANGLE("
            +"date text, "
            +"time integer, "
            +"angle_x real, "
            +"angle_y real, "
            +"angle_z real, "
            + "PRIMARY KEY(date,time)"+ ")";

    public static final String CREATE_LIDU = "create table lidu("
            +"date text, "
            +"time integer, "
            +"lidu real, "
            + "PRIMARY KEY(date,time)"+ ")";

    private Context mContext;


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_ACCELERATION);
        db.execSQL(CREATE_ANGLE);
        db.execSQL(CREATE_LIDU);
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
