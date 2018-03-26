package com.example.lucifer.mybluetooth.http;

import com.example.lucifer.mybluetooth.MyApplication;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
                addLog(httpClient);
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


    private static void addLog(OkHttpClient.Builder okHttpClient) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        // add your other interceptors …
        // add logging as last interceptor
        okHttpClient.addInterceptor(logging);  // <-- this is the import
    }
}