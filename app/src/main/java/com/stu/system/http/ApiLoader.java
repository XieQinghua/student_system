package com.stu.system.http;

import com.stu.system.bean.GetActionListBean;
import com.stu.system.bean.GetClassStuBean;
import com.stu.system.bean.GetStuHisListBean;
import com.stu.system.bean.GetStuManBean;
import com.stu.system.bean.LoginBean;
import com.stu.system.bean.SaveHistoryBean;
import com.stu.system.bean.SaveStuBean;

import java.util.Map;

public class ApiLoader {

    private static ApiService apiService;

    private ApiLoader() {
        throw new AssertionError();
    }

    private static ApiService getApiService() {
        if (apiService == null) {
            apiService = ApiManager.getInstance().getApiService(ApiService.class);
        }
        return apiService;
    }

    public static void reqLogin(String url, String name, String pass, SimpleCallback<LoginBean> callback) {
        ApiObserver.subscribe(getApiService().login(url, name, pass), callback);
    }

    public static void reqSaveStu(String url, Map<String, String> mapParams, SimpleCallback<SaveStuBean> callback) {
        ApiObserver.subscribe(getApiService().saveStu(url, mapParams), callback);
    }

    public static void reqGetStuMan(String url, String uid, SimpleCallback<GetStuManBean> callback) {
        ApiObserver.subscribe(getApiService().getStuMan(url, uid), callback);
    }

    public static void reqGetClassStu(String url, String cid, SimpleCallback<GetClassStuBean> callback) {
        ApiObserver.subscribe(getApiService().getClassStu(url, cid), callback);
    }

    public static void reqGetActionList(String url, SimpleCallback<GetActionListBean> callback) {
        ApiObserver.subscribe(getApiService().getActionList(url), callback);
    }

    public static void reqSaveHistory(String url, Map<String, String> mapParams, SimpleCallback<SaveHistoryBean> callback) {
        ApiObserver.subscribe(getApiService().saveHistory(url, mapParams), callback);
    }

    public static void reqGetStuHisList(String url, String sid, String pageindex, SimpleCallback<GetStuHisListBean> callback) {
        ApiObserver.subscribe(getApiService().getStuHisList(url, sid, pageindex), callback);
    }
}
