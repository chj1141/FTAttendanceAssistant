package com.example.chj.ftattendanceassistant.api;

import com.example.chj.ftattendanceassistant.network.ResultReturn;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by chenghj on 2018/8/15.
 */

public interface LoginApi {
    @FormUrlEncoded
    @POST("/FTAttendanceAssistant/login.php")
    Observable<ResultReturn> login(@Field("OperationType") String Operationtype,
            @Field("username") String username, @Field("password") String password,
    @Field("passwordNew") String passwordNew);
}
