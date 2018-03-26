package com.example.lucifer.mybluetooth.http;


import com.example.lucifer.mybluetooth.bean.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WebApiInterface {


    @FormUrlEncoded
    @POST("/")
    Observable<ApiResult> upload(@Field("table_name") String tableName, @Field("db_data") String data);


}
