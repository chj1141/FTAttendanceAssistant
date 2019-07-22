package com.example.chj.ftattendanceassistant.network;

import com.example.chj.ftattendanceassistant.utils.KQInfo;

import java.util.List;

/**
 * Created by chenghj on 2019/6/30.
 */

public class EmployeeInfoResult {
    private boolean success;
    private String msg;
    private int msgtype;

    private List<EmployeeInfo> arraylist;

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

    public List<EmployeeInfo> getArraylist() {
        return arraylist;
    }

    public void setArraylist(List<EmployeeInfo> arraylist) {
        this.arraylist = arraylist;
    }

    public static class EmployeeInfo{
        private String department;
        private String group;
        private String name;

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
