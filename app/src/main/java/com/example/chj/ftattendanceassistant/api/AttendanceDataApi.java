package com.example.chj.ftattendanceassistant.api;

import com.example.chj.ftattendanceassistant.network.AttendResultReturn;
import com.example.chj.ftattendanceassistant.network.AttendanceResultReturn;
import com.example.chj.ftattendanceassistant.network.EmployeeDataResult;
import com.example.chj.ftattendanceassistant.network.EmployeeInfoResult;
import com.example.chj.ftattendanceassistant.network.ResultReturn;
import com.example.chj.ftattendanceassistant.network.UrlContainer;
import com.example.chj.ftattendanceassistant.network.UserKQinfoResult;
import com.example.chj.ftattendanceassistant.utils.KQInfo;

import java.util.Date;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import rx.Observer;

/**
 * Created by chenghj on 2019/4/8.
 */

public class AttendanceDataApi {
    public interface AttendanceDataPostApi{
        @FormUrlEncoded
        @POST("/FTAttendanceAssistant/post_attendance_data.php")
        Observable<AttendanceResultReturn> postAttendanceData(@Field("username") String username, @Field("KQname") String KQname,
                                                          @Field("KQproject") String KQproject, @Field("KQtype") String KQtype,
                                                          @Field("KQduration") String KQduration, @Field("approvalStatus") String approvalStatus,
                                                          @Field("KQdaytime") String KQdaytime, @Field("KQremark") String KQremark,
                                                          @Field("OperationType") int OperationType);//0:插入或者更新，1：删除

    }

    public interface AttendanceDataGetApi{
        @FormUrlEncoded
        @POST("/FTAttendanceAssistant/get_attendance_data.php")
        Observable<AttendResultReturn> getAttendanceData(@Field("OperationType") String OperationType, @Field("Department") String department,
                                                         @Field("Username") String username);

    }

    //用于管理员审批
    public interface AttendanceDataApprovalApi{
        @FormUrlEncoded
        @POST("/FTAttendanceAssistant/approval_attendance_data.php")
        Observable<AttendanceResultReturn> postAttendanceData(@Field("KQname") String KQname, @Field("approvalStatus") String approvalStatus,
                                                              @Field("OperationType") int OperationType);
    }
    //请求部门人员信息
    public interface EmployeeInfoRequestApi{
        @FormUrlEncoded
        @POST("/FTAttendanceAssistant/employee_info_request.php")
        Observable<EmployeeInfoResult> getEmployeeInfo(@Field("OperationType") String OperationType,@Field("Department") String department,
                                                       @Field("Group") String group, @Field("KQname") String KQname,
                                                       @Field("KQtype") String KQtype);
    }
    //请求部门人员统计信息
    public interface EmployeeStatisticsDataRequestApi{
        @FormUrlEncoded
        @POST("/FTAttendanceAssistant/employee_info_request.php")
        Observable<EmployeeDataResult> getEmployeeStatisticsData(@Field("OperationType") String OperationType, @Field("Department") String department,
                                                                 @Field("Group") String group, @Field("KQname") String name,
                                                                 @Field("KQtype") String KQtype, @Field("infotime") String infotime);
    }

    //请求某个人的个人考勤信息
    public interface UserKQinfoGetApi{
        @FormUrlEncoded
        @POST("/FTAttendanceAssistant/userKQinfo_get.php")
        Observable<UserKQinfoResult> getUserKQinfoResult(@Field("OperationType") String OperationType,@Field("KQname") String name);
    }
}
