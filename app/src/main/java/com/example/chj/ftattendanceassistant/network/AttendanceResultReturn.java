package com.example.chj.ftattendanceassistant.network;

import java.util.Date;

/**
 * Created by chenghj on 2019/4/9.
 * 该bean为加班信息类
 */

public class AttendanceResultReturn {

    private boolean success;
    private String msg;
    private int msgtype;

    private AttendanceResultBean result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMsgtype(){
        return msgtype;
    }

    public void setMsgtype(int msgtype){
        this.msgtype = msgtype;
    }

    public AttendanceResultBean getResult() {
        return result;
    }

    public void setResult(AttendanceResultBean result) {
        this.result = result;
    }


    //具体加班信息
    public static class AttendanceResultBean{
        private String username;
        private String KQname;
        private String KQproject;
        private String KQtype;
        private String KQduration;//加班
        private String KQdaytime;//出差或者请假
        private Date   infotime;
        private String approvalStatus;
        private String KQremark;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getKQname() {
            return KQname;
        }

        public void setKQname(String KQname) {
            this.KQname = KQname;
        }

        public String getKQproject() {
            return KQproject;
        }

        public void setKQproject(String KQproject) {
            this.KQproject = KQproject;
        }

        public String getKQtype() {
            return KQtype;
        }

        public void setKQtype(String KQtype) {
            this.KQtype = KQtype;
        }

        public String getKQduration() {
            return KQduration;
        }

        public void setKQduration(String KQduration) {
            this.KQduration = KQduration;
        }

        public String getKQdaytime() {
            return KQdaytime;
        }

        public void setKQdaytime(String KQdaytime) {
            this.KQdaytime = KQdaytime;
        }

        public Date getInfotime() {
            return infotime;
        }

        public void setInfotime(Date infotime) {
            this.infotime = infotime;
        }

        public String getApprovalStatus() {
            return approvalStatus;
        }

        public void setApprovalStatus(String approvalStatus) {
            this.approvalStatus = approvalStatus;
        }

        public String getKQremark() {
            return KQremark;
        }

        public void setKQremark(String KQremark) {
            this.KQremark = KQremark;
        }
    }
}
