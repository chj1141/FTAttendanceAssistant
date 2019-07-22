package com.example.chj.ftattendanceassistant.network;

import java.util.List;

/**
 * Created by chenghj on 2019/7/5.
 */

public class UserKQinfoResult {
    private boolean success;
    private String msg;
    private int msgtype;

    private List<TimeString> arraylistOvertime;
    private List<TimeString> arraylistLeave;
    private List<TimeString> arraylistBusinesstravel;

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

    public List<TimeString> getArraylistOvertime() {
        return arraylistOvertime;
    }

    public void setArraylistOvertime(List<TimeString> arraylistOvertime) {
        this.arraylistOvertime = arraylistOvertime;
    }

    public List<TimeString> getArraylistLeave() {
        return arraylistLeave;
    }

    public void setArraylistLeave(List<TimeString> arraylistLeave) {
        this.arraylistLeave = arraylistLeave;
    }

    public List<TimeString> getArraylistBusinesstravel() {
        return arraylistBusinesstravel;
    }

    public void setArraylistBusinesstravel(List<TimeString> arraylistBusinesstravel) {
        this.arraylistBusinesstravel = arraylistBusinesstravel;
    }

    public class TimeString{
        private String perweek;
        private String permonth;
        private String peryear;

        public String getPerweek() {
            return perweek;
        }

        public void setPerweek(String perweek) {
            this.perweek = perweek;
        }

        public String getPermonth() {
            return permonth;
        }

        public void setPermonth(String permonth) {
            this.permonth = permonth;
        }

        public String getPeryear() {
            return peryear;
        }

        public void setPeryear(String peryear) {
            this.peryear = peryear;
        }
    }


}
