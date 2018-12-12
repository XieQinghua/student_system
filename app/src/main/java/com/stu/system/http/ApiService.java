package com.stu.system.http;


import com.stu.system.bean.LoginBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {

    /**
     * 登录
     *
     * @param url  动态api地址
     * @param name
     * @param pass
     * @return
     */
    @FormUrlEncoded
    @POST()
    Observable<LoginBean> login(@Url String url,
                                @Field("name") String name,
                                @Field("pass") String pass);

}