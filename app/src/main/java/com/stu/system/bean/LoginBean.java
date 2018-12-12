package com.stu.system.bean;

import java.io.Serializable;
import java.util.List;

public class LoginBean implements Serializable{

    /**
     * code : 1
     * info : 登录成功！
     * value : [{"uid":"0072be31-9e49-483c-81d0-f3068e2f38c7","uname":"月亮老师"}]
     */

    private int code;
    private String info;
    private List<ValueBean> value;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<ValueBean> getValue() {
        return value;
    }

    public void setValue(List<ValueBean> value) {
        this.value = value;
    }

    public static class ValueBean {
        /**
         * uid : 0072be31-9e49-483c-81d0-f3068e2f38c7
         * uname : 月亮老师
         */

        private String uid;
        private String uname;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }
    }
}
