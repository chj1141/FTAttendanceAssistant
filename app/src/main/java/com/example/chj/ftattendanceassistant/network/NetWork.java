package com.example.chj.ftattendanceassistant.network;

import com.example.chj.ftattendanceassistant.api.AttendanceDataApi;
import com.example.chj.ftattendanceassistant.api.LoginApi;
import com.example.chj.ftattendanceassistant.api.RegisterApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chenghj on 2018/8/15.
 */

public class NetWork {

    //登陆
    private static LoginApi sLoginApi;
    //注册
    private static RegisterApi sRegisterApi;
    //提交考勤信息
    private static AttendanceDataApi.AttendanceDataPostApi sAttendanceDataPostApi;
    private static AttendanceDataApi.AttendanceDataGetApi sAttendanceDataGetApi;
    private static AttendanceDataApi.AttendanceDataApprovalApi sAttendanceDataApprovalApi;
    private static AttendanceDataApi.EmployeeInfoRequestApi sEmployeeInfoRequestApi;
    private static AttendanceDataApi.EmployeeStatisticsDataRequestApi sEmployeeStatisticsDataRequestApi;
    private static AttendanceDataApi.UserKQinfoGetApi sUserKQinfoGetApi;

    private static OkHttpClient sOkHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static Gson gson = new GsonBuilder() .setLenient() .create();

    public static LoginApi getLoginApi(){
        if (sLoginApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl(UrlContainer.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    //.addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sLoginApi = retrofit.create(LoginApi.class);
        }
        return sLoginApi;
    }

    public static RegisterApi getRegisiterApi(){
        if (sRegisterApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl(UrlContainer.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sRegisterApi = retrofit.create(RegisterApi.class);
        }
        return sRegisterApi;
    }

    public static AttendanceDataApi.AttendanceDataPostApi getAttendanceDataPostApi(){
        if (sAttendanceDataPostApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl(UrlContainer.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sAttendanceDataPostApi = retrofit.create(AttendanceDataApi.AttendanceDataPostApi.class);
        }
        return sAttendanceDataPostApi;
    }

    public static AttendanceDataApi.AttendanceDataGetApi getAttendanceDataGetApi(){
        if (sAttendanceDataGetApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl(UrlContainer.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sAttendanceDataGetApi = retrofit.create(AttendanceDataApi.AttendanceDataGetApi.class);
        }
        return sAttendanceDataGetApi;
    }

    public static AttendanceDataApi.AttendanceDataApprovalApi setApprovalStatusApi(){
        if (sAttendanceDataApprovalApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl(UrlContainer.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sAttendanceDataApprovalApi = retrofit.create(AttendanceDataApi.AttendanceDataApprovalApi.class);
        }
        return sAttendanceDataApprovalApi;
    }

    public static AttendanceDataApi.EmployeeInfoRequestApi getEmployeeInfoRequestApi(){
        if (sEmployeeInfoRequestApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl(UrlContainer.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sEmployeeInfoRequestApi = retrofit.create(AttendanceDataApi.EmployeeInfoRequestApi.class);
        }
        return sEmployeeInfoRequestApi;
    }

    public static AttendanceDataApi.EmployeeStatisticsDataRequestApi getEmployeeStatisticsDataRequestApi(){
        if (sEmployeeStatisticsDataRequestApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl(UrlContainer.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sEmployeeStatisticsDataRequestApi = retrofit.create(AttendanceDataApi.EmployeeStatisticsDataRequestApi.class);
        }
        return sEmployeeStatisticsDataRequestApi;
    }

    public static AttendanceDataApi.UserKQinfoGetApi getUserKQinfoGetApi(){
        if (sUserKQinfoGetApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl(UrlContainer.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sUserKQinfoGetApi = retrofit.create(AttendanceDataApi.UserKQinfoGetApi.class);
        }
        return sUserKQinfoGetApi;
    }


}
