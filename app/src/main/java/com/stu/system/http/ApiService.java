package com.stu.system.http;

import com.stu.system.LoginBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/*****************************
 * @Copyright(c) 2014-2018
 * 长沙市希尚网络科技有限公司 All Rights Reserved.
 * @Author：xieqinghua@jubaozan.com
 * @Date：2018/8/9
 * @Description：接口方法
 *****************************/
public interface ApiService {

    /**
     * 登录
     *
     * @param name
     * @param pass
     * @return
     */
    @FormUrlEncoded
    @POST(Api.LOGIN)
    Observable<LoginBean> login(@Field("name") String name,
                                @Field("pass") String pass);

}