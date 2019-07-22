package com.example.chj.ftattendanceassistant.network;

import java.util.List;

/**
 * Created by chenghj on 2019/7/1.
 */

public class EmployeeDataResult {
    private boolean success;
    private String msg;
    private int msgtype;

    private List<EmployeeData> arraylist;

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

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public List<EmployeeData> getArraylist() {
        return arraylist;
    }

    public void setArraylist(List<EmployeeData> arraylist) {
        this.arraylist = arraylist;
    }

    public class EmployeeData{
        private String strdate;
        private String durationsum;

        public String getStrdate() {
            return strdate;
        }

        public void setStrdate(String strdate) {
            this.strdate = strdate;
        }

        public String getDurationsum() {
            return durationsum;
        }

        public void setDurationsum(String durationsum) {
            this.durationsum = durationsum;
        }
    }
}
