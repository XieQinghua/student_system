package com.stu.system.http;

import com.stu.system.bean.GetActionListBean;
import com.stu.system.bean.GetClassStuBean;
import com.stu.system.bean.GetStuHisListBean;
import com.stu.system.bean.GetStuManBean;
import com.stu.system.bean.LoginBean;
import com.stu.system.bean.SaveHistoryBean;
import com.stu.system.bean.SaveStuBean;

import java.util.List;

import okhttp3.MultipartBody;

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

    public static void reqSaveStu(String url,
                                  MultipartBody.Part name,
                                  MultipartBody.Part sex,
                                  MultipartBody.Part tel,
                                  MultipartBody.Part cid,
                                  MultipartBody.Part bron,
                                  MultipartBody.Part address,
                                  MultipartBody.Part uid,
                                  MultipartBody.Part file,
                                  SimpleCallback<SaveStuBean> callback) {
        ApiObserver.subscribe(getApiService().saveStu(url, name, sex, tel, cid, bron, address, uid, file), callback);
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

    public static void reqSaveHistory(String url,
                                      MultipartBody.Part sid,
                                      MultipartBody.Part date,
                                      MultipartBody.Part title,
                                      MultipartBody.Part action,
                                      MultipartBody.Part info,
                                      MultipartBody.Part imgcount,
                                      MultipartBody.Part uid,
                                      List<MultipartBody.Part> picPartFileList,
                                      MultipartBody.Part videoFile,
                                      MultipartBody.Part videoThumbFile,
                                      SimpleCallback<SaveHistoryBean> callback) {
        if (picPartFileList.size() == 1) {
            ApiObserver.subscribe(getApiService().saveHistory(url, sid, date, title, action, info, imgcount, uid, picPartFileList.get(0), videoFile, videoThumbFile), callback);
        } else if (picPartFileList.size() == 2) {
            ApiObserver.subscribe(getApiService().saveHistory(url, sid, date, title, action, info, imgcount, uid, picPartFileList.get(0), picPartFileList.get(1), videoFile, videoThumbFile), callback);
        } else if (picPartFileList.size() == 3) {
            ApiObserver.subscribe(getApiService().saveHistory(url, sid, date, title, action, info, imgcount, uid, picPartFileList.get(0), picPartFileList.get(1), picPartFileList.get(2), videoFile, videoThumbFile), callback);
        }
    }

    public static void reqGetStuHisList(String url, String sid, String pageindex, SimpleCallback<GetStuHisListBean> callback) {
        ApiObserver.subscribe(getApiService().getStuHisList(url, sid, pageindex), callback);
    }
}
