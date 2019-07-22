package com.example.chj.ftattendanceassistant.network;

import com.example.chj.ftattendanceassistant.activity.MainActivity;
import com.example.chj.ftattendanceassistant.utils.KQInfo;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenghj on 2018/8/15.
 */

public class UrlContainer {
    //根据申请域名切换 或者 自己搭建服务器
    //public static String BASE_URL = "http://192.168.21.108:8009";
    public static String BASE_URL = "http://25ud643815.qicp.vip:37034";

    public static Subscription postAttendanceData(KQInfo kqInfo, Observer<AttendanceResultReturn> mObserver,int operationType){
        //获取时长
        String duration = kqInfo.getKQduration();
        /*if (kqInfo.getKQduration().contains("天")){
            String str[] = kqInfo.getKQduration().split("天");
            double temp = Double.parseDouble(str[0]);
            duration = String.valueOf(temp*8);
        }else{
            String str[] = kqInfo.getKQduration().split("时");
            double temp = Double.parseDouble(str[0]);
            duration = String.valueOf(temp);
        }*/
        return NetWork.getAttendanceDataPostApi()
                .postAttendanceData(MainActivity.username,kqInfo.getKQname(),kqInfo.getKQproject(),kqInfo.getKQtype(),duration,"未提交","","无",operationType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }


}
