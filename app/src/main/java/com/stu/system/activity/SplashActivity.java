package com.stu.system.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.stu.system.R;
import com.stu.system.base.BaseActivity;
import com.stu.system.common.Constants;
import com.stu.system.util.SPUtils;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends BaseActivity {
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止启动页面(SplashActivity)被多次启动
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) &&
                    action != null &&
                    action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (SPUtils.getInstance().getBoolean(Constants.IS_LOGIN, false)) {
                    startActivity(MainActivity.class);
                    finish();
                } else {
                    startActivity(LoginActivity.class);
                    finish();
                }
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 2000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //在启动页按底部返回按钮取消延时操作
            handler.removeCallbacks(runnable);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
