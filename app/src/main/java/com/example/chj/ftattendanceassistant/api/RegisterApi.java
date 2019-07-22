package com.example.chj.ftattendanceassistant.api;

import com.example.chj.ftattendanceassistant.network.ResultReturn;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by chenghj on 2018/8/15.
 */

public interface RegisterApi {
    @FormUrlEncoded
    @POST("/FTAttendanceAssistant/register.php")
    Observable<ResultReturn> register(@Field("username") String username, @Field("password") String password,
                                      @Field("fullname") String fullname,@Field("phonenumber") String phonenumber);
}
