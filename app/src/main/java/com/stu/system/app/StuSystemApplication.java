package com.stu.system.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.stu.system.common.Constants;
import com.stu.system.http.ApiManager;
import com.tencent.bugly.crashreport.CrashReport;

public class StuSystemApplication extends Application {
    private static final String TAG = StuSystemApplication.class.getSimpleName();
    public static StuSystemApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        // 初始化http请求工具
        ApiManager.init("http://fuck.biegral.com");
        // 初始化工具类类库
        Utils.init(this);
        // 初始化Fresco图片加载库
        ImagePipelineConfig frescoConfig = ImagePipelineConfig.newBuilder(getApplicationContext()).setDownsampleEnabled(true).build();
        Fresco.initialize(this, frescoConfig);
        // 初始化bugly
        CrashReport.initCrashReport(this, Constants.BUGLY_ID, true);
        //CrashReport.testJavaCrash();
    }

    public static StuSystemApplication getInstance() {
        return app;
    }
}
