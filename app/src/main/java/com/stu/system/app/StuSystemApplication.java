package com.stu.system.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.stu.system.http.Api;
import com.stu.system.http.ApiManager;

public class StuSystemApplication extends Application {
    private static final String TAG = StuSystemApplication.class.getSimpleName();
    public static StuSystemApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        // 初始化http请求工具
        ApiManager.init(Api.HOST);
        // 初始化工具类类库
        Utils.init(this);
        // 初始化Fresco图片加载库
        ImagePipelineConfig frescoConfig = ImagePipelineConfig.newBuilder(getApplicationContext()).setDownsampleEnabled(true).build();
        Fresco.initialize(this, frescoConfig);
    }

    public static StuSystemApplication getInstance() {
        return app;
    }
}