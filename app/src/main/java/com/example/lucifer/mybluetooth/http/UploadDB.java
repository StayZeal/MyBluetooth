package com.example.lucifer.mybluetooth.http;


import android.util.Log;

import com.amitshekhar.server.RequestHandler;
import com.example.lucifer.mybluetooth.MyApplication;
import com.example.lucifer.mybluetooth.ToastUtil;
import com.example.lucifer.mybluetooth.bean.ApiResult;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UploadDB {


    public static final int TYPE_INSERT_ACCELERATION = 1;
    public static final int TYPE_INSERT_ANGLE = 2;
    public static final int TYPE_INSERT_LIDU = 3;

    public static final String INSERT_ACCELERATION = "ACCELERATION";
    public static final String INSERT_ANGLE = "ANGLE";
    public static final String INSERT_LIDU = "lidu";
    private static final String TAG = "UploadDB";

    private static WebApiInterface mWebApiInterface;

    public static Disposable upload(int type, WebApiInterface mWebApiInterface) {

        if (mWebApiInterface == null) {
            throw new RuntimeException("WebApiInterface 不能为null");
        }

        UploadDB.mWebApiInterface = mWebApiInterface;


        return uploadAcc(type);
    }

    private static Disposable uploadAcc(int type) {


        String table = null;
        switch (type) {
            case 1:
                table = INSERT_ACCELERATION;
                break;
            case 2:
                table = INSERT_ANGLE;
                break;
            case 3:
                table = INSERT_LIDU;
                break;
        }

        final String tableName = table;


        return getData(type)
                .flatMap(new Function<String, Observable<ApiResult>>() {
                    @Override
                    public Observable<ApiResult> apply(String data) throws Exception {
                        return mWebApiInterface.upload(tableName, data);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult>() {
                    @Override
                    public void accept(ApiResult apiResult) throws Exception {

                        Log.i(TAG, tableName + "上传成功:" );
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                        ToastUtil.show("上传失败，请检查服务器是否启动");
                    }
                });

    }


    private static Observable<String> getData(int type) {

        String table = null;
        switch (type) {
            case 1:
                table = INSERT_ACCELERATION;
                break;
            case 2:
                table = INSERT_ANGLE;
                break;
            case 3:
                table = INSERT_LIDU;
                break;
        }

        final String tableName = table;
        //RxJava 进行网络请求
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                RequestHandler requestHandler = new RequestHandler(MyApplication.getContext());

                requestHandler.getDBListResponse();
                requestHandler.openDatabase(GlobalConfig.DATABASE_NAME);

                String route = "?tableName=" + tableName;
                /**
                 * 读取数据库，用Gson封装成Json字符串
                 */
                String data = requestHandler.getAllDataFromTheTableResponse(route);

                Log.i(TAG, "table:" + tableName + ": " + data);

                e.onNext(data);
                e.onComplete();

            }
        });


    }
}
