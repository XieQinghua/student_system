package com.stu.system.http;

/**
 * 接口
 */
public class Api {

    /**
     * 登录
     */
    public static String LOGIN = "/Api/Login";
    /**
     * 保存学生
     */
    public static final String SAVE_STU = "/Api/SaveStu";
    /**
     * 获得学生管理页面
     */
    public static final String GET_STU_MAN = "/Api/GetStuMan";
    /**
     * 获得某个班级学生列表
     */
    public static final String GET_CLASS_STU = "/Api/GetClassStu";
    /**
     * 获得动作列表
     */
    public static final String GET_ACTION_LIST = "/Api/GetActionList";
    /**
     * 添加学生上课日志
     */
    public static final String SAVE_HISTORY = "/Api/SaveHistory";
    /**
     * 获得某学生日志
     */
    public static final String GET_STU_HISLIST = "/Api/GetStuHisList";

    /**
     * 删除某学生日志
     */
    public static final String DEL_HIS = "/Api/DelHis";

    /**
     * 获取连接状态
     */
    public static final String GET_CONN = "/tv/getconn";
}
