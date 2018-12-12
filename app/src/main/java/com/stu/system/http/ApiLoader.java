package com.stu.system.http;


import com.stu.system.LoginBean;

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

    public static void reqLogin(String name, String pass, SimpleCallback<LoginBean> callback) {
        ApiObserver.subscribe(getApiService().login(name, pass), callback);
    }
}