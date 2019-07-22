package com.example.chj.ftattendanceassistant.network;

/**
 * Created by chenghj on 2018/8/15.
 * 该bean为个人信息类
 */

public class ResultReturn {
    /**
     * success : true
     * msg : Login success
     * result : {"username":"linghc,"phonenumber":"123"
     *           "fullname":"令狐冲","department":"华山派"
     *           "groupname":"剑宗","post":"恒山派掌门"}
     */
    private boolean success;
    private String msg;
    private int msgtype;

    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean{

        private String username;
        private String fullname;
        private String phonenumber;
        private String department;
        private String groupname;
        private String post;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }
    }
}
