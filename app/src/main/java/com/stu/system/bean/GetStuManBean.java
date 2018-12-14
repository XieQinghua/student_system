package com.stu.system.bean;

import java.util.List;

/**
 * http://fuck.biegral.com/Api/GetStuMan?uid=0072be31-9e49-483c-81d0-f3068e2f38c7
 */
public class GetStuManBean extends BaseBean {

    private List<List<ClassBean>> value;

    public List<List<ClassBean>> getValue() {
        return value;
    }

    public void setValue(List<List<ClassBean>> value) {
        this.value = value;
    }
}
