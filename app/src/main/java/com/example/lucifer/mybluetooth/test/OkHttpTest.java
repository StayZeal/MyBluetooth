package com.example.lucifer.mybluetooth.test;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpTest {

    private OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
            .url("https://api.github.com/repos/square/okhttp/issues")
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("Accept", "application/json; q=0.5")
            .addHeader("Accept", "application/vnd.github.v3+json")
            .build();


    public void cache() {
        client = client.newBuilder()
                .cache(new Cache(null, 124))
                .build();

        Request request = new Request
                .Builder()
                .cacheControl(new CacheControl.Builder()
                        .maxAge(1, TimeUnit.DAYS)
                        .build())
                .build();
    }

    public void run() throws Exception {

        Response response = client.newCall(request).execute();// 同步请求
        client.newCall(request).enqueue(new Callback() {//异步请求
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        System.out.println("Server: " + response.header("Server"));
        System.out.println("Date: " + response.header("Date"));
        System.out.println("Vary: " + response.headers("Vary"));
    }

    public void test() {

    }
}
