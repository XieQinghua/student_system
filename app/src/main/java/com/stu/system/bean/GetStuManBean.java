package com.stu.system.bean;

import java.util.List;

/**
 * http://fuck.biegral.com/Api/GetStuMan?uid=0072be31-9e49-483c-81d0-f3068e2f38c7
 */
public class GetStuManBean extends BaseBean {

    private List<List<ValueBean>> value;

    public List<List<ValueBean>> getValue() {
        return value;
    }

    public void setValue(List<List<ValueBean>> value) {
        this.value = value;
    }

    public static class ValueBean {
        /**
         * cid : 9039ec59-267b-47fd-96a8-3a1fa7e16e47
         * cname : 新的测试班级2
         */

        private String cid;
        private String cname;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }
    }
}
