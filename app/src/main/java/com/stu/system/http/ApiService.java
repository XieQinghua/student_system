package com.stu.system.http;


import com.stu.system.bean.BaseBean;
import com.stu.system.bean.GetActionListBean;
import com.stu.system.bean.GetClassStuBean;
import com.stu.system.bean.GetStuHisListBean;
import com.stu.system.bean.GetStuManBean;
import com.stu.system.bean.LoginBean;
import com.stu.system.bean.SaveHistoryBean;
import com.stu.system.bean.SaveStuBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @Multipart
    @POST()
    Observable<SaveStuBean> saveStu(@Url String url,
                                    @Part MultipartBody.Part name,
                                    @Part MultipartBody.Part sex,
                                    @Part MultipartBody.Part tel,
                                    @Part MultipartBody.Part cid,
                                    @Part MultipartBody.Part bron,
                                    @Part MultipartBody.Part address,
                                    @Part MultipartBody.Part uid,
                                    @Part MultipartBody.Part file);

    @GET()
    Observable<GetStuManBean> getStuMan(@Url String url,
                                        @Query("uid") String uid);

    @GET()
    Observable<GetClassStuBean> getClassStu(@Url String url,
                                            @Query("cid") String cid);

    @GET()
    Observable<GetActionListBean> getActionList(@Url String url);

    @Multipart
    @POST()
    Observable<SaveHistoryBean> saveHistory(@Url String url,
                                            @Part MultipartBody.Part sid,
                                            @Part MultipartBody.Part date,
                                            @Part MultipartBody.Part title,
                                            @Part MultipartBody.Part action,
                                            @Part MultipartBody.Part info,
                                            @Part MultipartBody.Part imgcount,
                                            @Part MultipartBody.Part uid,
                                            @Part MultipartBody.Part picPartFile1,
                                            @Part MultipartBody.Part videoFile,
                                            @Part MultipartBody.Part videoThumbFile);

    @Multipart
    @POST()
    Observable<SaveHistoryBean> saveHistory(@Url String url,
                                            @Part MultipartBody.Part sid,
                                            @Part MultipartBody.Part date,
                                            @Part MultipartBody.Part title,
                                            @Part MultipartBody.Part action,
                                            @Part MultipartBody.Part info,
                                            @Part MultipartBody.Part imgcount,
                                            @Part MultipartBody.Part uid,
                                            @Part MultipartBody.Part picPartFile1,
                                            @Part MultipartBody.Part picPartFile2,
                                            @Part MultipartBody.Part videoFile,
                                            @Part MultipartBody.Part videoThumbFile);

    @Multipart
    @POST()
    Observable<SaveHistoryBean> saveHistory(@Url String url,
                                            @Part MultipartBody.Part sid,
                                            @Part MultipartBody.Part date,
                                            @Part MultipartBody.Part title,
                                            @Part MultipartBody.Part action,
                                            @Part MultipartBody.Part info,
                                            @Part MultipartBody.Part imgcount,
                                            @Part MultipartBody.Part uid,
                                            @Part MultipartBody.Part picPartFile1,
                                            @Part MultipartBody.Part picPartFile2,
                                            @Part MultipartBody.Part picPartFile3,
                                            @Part MultipartBody.Part videoFile,
                                            @Part MultipartBody.Part videoThumbFile);


    @GET()
    Observable<GetStuHisListBean> getStuHisList(@Url String url,
                                                @Query("sid") String sid,
                                                @Query("pageindex") String pageindex);

    @GET()
    Observable<BaseBean> delHis(@Url String url,
                                @Query("hid") String hid);

    @GET()
    Observable<BaseBean> getConn(@Url String url);

}