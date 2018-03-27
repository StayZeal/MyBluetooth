package com.example.lucifer.mybluetooth.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lucifer.mybluetooth.MyApplication;
import com.example.lucifer.mybluetooth.R;
import com.example.lucifer.mybluetooth.http.RetrofitUtil;
import com.example.lucifer.mybluetooth.http.UploadDB;
import com.example.lucifer.mybluetooth.http.WebApiInterface;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {
    protected Activity thisActivity = null;
    //    CompositeSubscription compositeSubscription;
    CompositeDisposable compositeSubscription;

    protected WebApiInterface mWebApi;

    public WebApiInterface getWebApi() {
        return mWebApi;
    }

    protected void initWebApi() {
//        mWebApi = RetrofitUtil.getHttpsInstance().create(WebApiInterface.class);
        mWebApi = RetrofitUtil.getInstance().create(WebApiInterface.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        thisActivity = this;

        if (getIntent() != null) {
            getParams(getIntent());
        }
        initView();
        getData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (TextUtils.isEmpty(MyApplication.IP)) {
            Toast.makeText(this, "请设置服务器IP", Toast.LENGTH_SHORT).show();
        }

        Disposable disposable;

        //上传数据
        switch (item.getItemId()) {
            case R.id.menu_1:
                initWebApi();
                //上传表ACCELERATION
                disposable = UploadDB.upload(UploadDB.TYPE_INSERT_ACCELERATION, mWebApi);
                addSubscription(disposable);

                return true;
            case R.id.menu_2:
                initWebApi();
                disposable = UploadDB.upload(UploadDB.TYPE_INSERT_ANGLE, mWebApi);
                addSubscription(disposable);
                return true;
            case R.id.menu_3:
                initWebApi();
                disposable = UploadDB.upload(UploadDB.TYPE_INSERT_LIDU, mWebApi);
                addSubscription(disposable);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 在initView()之前被调用
     *
     * @param intent
     */
    protected void getParams(Intent intent) {
    }

    protected abstract int getLayoutID();

    protected abstract void initView();

    protected void getData() {
    }


    public void addSubscription(Disposable subscription) {
        if (subscription == null) {
            return;
        }
        if (compositeSubscription == null) {
//            compositeSubscription = new CompositeSubscription();
            compositeSubscription = new CompositeDisposable();
        }
        compositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null) {
            compositeSubscription.clear();
        }
    }
}
