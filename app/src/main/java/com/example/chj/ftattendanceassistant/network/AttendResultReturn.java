package com.example.chj.ftattendanceassistant.network;

import com.example.chj.ftattendanceassistant.utils.KQInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenghj on 2019/6/19.
 *
 */

public class AttendResultReturn {

    private boolean success;
    private String msg;
    private int msgtype;
    private List<KQInfo> arraylist;

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public List<KQInfo> getArraylist() {
        return arraylist;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public void setArraylist(List<KQInfo> arraylist) {
        this.arraylist = arraylist;
    }

}
