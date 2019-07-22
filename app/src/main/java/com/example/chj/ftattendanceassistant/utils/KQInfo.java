package com.example.chj.ftattendanceassistant.utils;

/**
 * Created by chenghj on 2018/12/30.
 */

import com.example.chj.ftattendanceassistant.fragment.ReportFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 考勤信息类
 * 姓名，考勤类型，时长，项目，审批情况，昵称（本人或者代报）,提交时间
 */
public class KQInfo implements Serializable {
    //全局静态用
    public static List<KQInfo> KQinfoList = new ArrayList<KQInfo>();

    int KQtypeImageId;
    String KQtype;
    String KQproject;
    String KQname;
    String KQduration;
    String InfoTime;
    String ApprovalStatus;
    String KQremark;
    String username;

    public int getKQtypeImageId() {
        return KQtypeImageId;
    }

    public void setKQtypeImageId(int KQtypeImageId) {
        this.KQtypeImageId = KQtypeImageId;
    }

    public String getKQtype() {
        return KQtype;
    }

    public void setKQtype(String KQtype) {
        this.KQtype = KQtype;
    }

    public String getKQproject() {
        return KQproject;
    }

    public void setKQproject(String KQproject) {
        this.KQproject = KQproject;
    }

    public String getKQname() {
        return KQname;
    }

    public void setKQname(String KQname) {
        this.KQname = KQname;
    }

    public String getKQduration() {
        return KQduration;
    }

    public void setKQduration(String KQduration) {
        this.KQduration = KQduration;
    }

    public String getInfoTime() {
        return InfoTime;
    }

    public void setInfoTime(String infoTime) {
        InfoTime = infoTime;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public String getKQremark() {
        return KQremark;
    }

    public void setKQremark(String KQremark) {
        this.KQremark = KQremark;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
