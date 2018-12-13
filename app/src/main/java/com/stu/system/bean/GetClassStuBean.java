package com.stu.system.bean;

import java.util.List;

/**
 * http://fuck.biegral.com/Api/GetClassStu?cid=9039ec59-267b-47fd-96a8-3a1fa7e16e47
 */
public class GetClassStuBean extends BaseBean {

    private List<List<ValueBean>> value;

    public List<List<ValueBean>> getValue() {
        return value;
    }

    public void setValue(List<List<ValueBean>> value) {
        this.value = value;
    }

    public static class ValueBean {
        /**
         * sid : b1aac8bc-300e-47a5-baad-c8d13776dd3f
         * sname : 测试数据01
         */

        private String sid;
        private String sname;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }
    }
}
