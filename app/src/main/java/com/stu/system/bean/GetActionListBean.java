package com.stu.system.bean;

import java.util.List;

/**
 * http://fuck.biegral.com/Api/GetActionList
 */
public class GetActionListBean extends BaseBean {

    private List<List<ValueBean>> value;

    public List<List<ValueBean>> getValue() {
        return value;
    }

    public void setValue(List<List<ValueBean>> value) {
        this.value = value;
    }

    public static class ValueBean {
        /**
         * aid : aebe83d6-1518-4fd1-bb39-6aba784b2551
         * aname : 上课
         */

        private String aid;
        private String aname;

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getAname() {
            return aname;
        }

        public void setAname(String aname) {
            this.aname = aname;
        }
    }
}
