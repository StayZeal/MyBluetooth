package com.example.lucifer.mybluetooth.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucifer.mybluetooth.MyApplication;
import com.example.lucifer.mybluetooth.R;

public class SetIPActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ip);

        editText = (EditText) findViewById(R.id.ip);


    }

    public void set(View view) {

        String ip = editText.getText().toString();

        if (TextUtils.isEmpty(ip)) {
            Toast.makeText(this, "请输入IP", Toast.LENGTH_SHORT).show();
            return;
        }

        MyApplication.IP = ip;

        finish();

    }
}
