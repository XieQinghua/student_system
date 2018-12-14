package com.stu.system.http;


import com.stu.system.bean.GetActionListBean;
import com.stu.system.bean.GetClassStuBean;
import com.stu.system.bean.GetStuHisListBean;
import com.stu.system.bean.GetStuManBean;
import com.stu.system.bean.LoginBean;
import com.stu.system.bean.SaveHistoryBean;
import com.stu.system.bean.SaveStuBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
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

    @FormUrlEncoded
    @POST()
    Observable<SaveStuBean> saveStu(@Url String url,
                                    @FieldMap Map<String, String> mapParams);

    @GET()
    Observable<GetStuManBean> getStuMan(@Url String url,
                                        @Query("uid") String uid);

    @GET()
    Observable<GetClassStuBean> getClassStu(@Url String url,
                                            @Query("cid") String cid);

    @GET()
    Observable<GetActionListBean> getActionList(@Url String url);

    @FormUrlEncoded
    @POST()
    Observable<SaveHistoryBean> saveHistory(@Url String url,
                                            @FieldMap Map<String, String> mapParams);

    @GET()
    Observable<GetStuHisListBean> getStuHisList(@Url String url,
                                                @Query("sid") String sid,
                                                @Query("pageindex") String pageindex);

}