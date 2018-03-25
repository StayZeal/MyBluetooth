package com.example.lucifer.mybluetooth.http;

import com.example.lucifer.mybluetooth.MyApplication;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    private static volatile Retrofit sInstance;

    private RetrofitUtil() {
    }

    /*超时设置*/
    private static final int DEFAULT_TIMEOUT = 10;

    public static Retrofit getInstance() {
        if (sInstance == null) {
            synchronized (RetrofitUtil.class) {
                OkHttpClient.Builder httpClient = getOkHttp();
//                addInterceptor(httpClient);
                sInstance = new Retrofit.Builder()
                        .baseUrl(MyApplication.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                        .client(httpClient.build())
                        .build();
            }
        }
        return sInstance;
    }

    public static OkHttpClient.Builder getOkHttp() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30000, TimeUnit.SECONDS);
        return builder;
    }
}