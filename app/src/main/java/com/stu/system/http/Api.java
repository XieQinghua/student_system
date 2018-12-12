package com.stu.system.http;

/**
 * 接口
 */
public class Api {
    public static final String HOST = "http://fuck.biegral.com";
    //public static final String HOST = SPUtils.getInstance().getString(Constants.HOST, "");

    /**
     * 登录
     */
    public static final String LOGIN = HOST + "/Api/Login";
    /**
     * 保存学生
     */
    public static final String SAVE_STU = HOST + "/Api/SaveStu";
    /**
     * 获得学生管理页面
     */
    public static final String GET_STU_MAN = HOST + "/Api/GetStuMan";
    /**
     * 获得某个班级学生列表
     */
    public static final String GET_CLASS_STU = HOST + "/Api/GetClassStu";
    /**
     * 获得动作列表
     */
    public static final String GET_ACTION_LIST = HOST + "/Api/GetActionList";
    /**
     * 添加学生上课日志
     */
    public static final String SAVE_HISTORY = HOST + "/Api/SaveHistory";
    /**
     * 获得某学生日志
     */
    public static final String GET_STU_HISLIST = HOST + "/Api/GetStuHisList";
}
