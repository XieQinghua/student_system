package com.stu.system.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.luck.picture.lib.dialog.CustomDialog;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.stu.system.R;
import com.stu.system.base.BaseActivity;
import com.stu.system.common.Constants;
import com.stu.system.permission.DefaultRationale;
import com.stu.system.permission.PermissionSetting;
import com.stu.system.util.DialogUtil;
import com.stu.system.util.LightStatusBarUtils;
import com.stu.system.util.ToolbarUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoPlayActivity extends BaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.video_view)
    VideoView videoView;
    @BindView(R.id.iv_play)
    ImageView ivPlay;

    private MediaController mMediaController;
    private int mPositionWhenPaused = -1;

    private String videoName, videoUrl;
    private LoadDataThread loadDataThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        LightStatusBarUtils.setLightStatusBar(VideoPlayActivity.this, false);
        ToolbarUtil.setColor(VideoPlayActivity.this, getResources().getColor(R.color.text_main_color));

        videoName = getIntent().getStringExtra("videoName");
        videoUrl = getIntent().getStringExtra("videoUrl");
        tvSave.setVisibility(View.VISIBLE);
        Log.e(TAG, "videoName====" + videoName);
        Log.e(TAG, "videoUrl====" + videoUrl);

        mMediaController = new MediaController(this);
        videoView.setOnCompletionListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setMediaController(mMediaController);
    }

    private void initData() {

    }

    @Override
    public void onStart() {
        // Play Video
        videoView.setVideoPath(videoUrl);
        videoView.start();
        super.onStart();
    }

    @Override
    public void onPause() {
        // Stop video when the activity is pause.
        mPositionWhenPaused = videoView.getCurrentPosition();
        videoView.stopPlayback();
        super.onPause();
    }

    @Override
    public void onResume() {
        // Resume video player
        if (mPositionWhenPaused >= 0) {
            videoView.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMediaController = null;
        videoView = null;
        ivPlay = null;
        super.onDestroy();
    }

    @Override
    public boolean onError(MediaPlayer player, int arg1, int arg2) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (null != ivPlay) {
            ivPlay.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.tv_save, R.id.iv_play})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                requestPermission(Permission.WRITE_EXTERNAL_STORAGE);
                break;
            case R.id.iv_play:
                videoView.start();
                ivPlay.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new ContextWrapper(newBase) {
            @Override
            public Object getSystemService(String name) {
                if (Context.AUDIO_SERVICE.equals(name)) {
                    return getApplicationContext().getSystemService(name);
                }
                return super.getSystemService(name);
            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // video started
                    videoView.setBackgroundColor(Color.TRANSPARENT);
                    return true;
                }
                return false;
            }
        });
    }

    private void requestPermission(String permissions) {
        switch (permissions) {
            case Permission.WRITE_EXTERNAL_STORAGE:
                AndPermission.with(this)
                        .permission(permissions)
                        .rationale(new DefaultRationale())
                        .onGranted(new Action() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onAction(List<String> permissions) {
                                showDownLoadDialog(videoUrl);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                Toast.makeText(VideoPlayActivity.this, "读取内存卡权限被拒绝", Toast.LENGTH_SHORT).show();
                                if (AndPermission.hasAlwaysDeniedPermission(VideoPlayActivity.this, permissions)) {
                                    new PermissionSetting(VideoPlayActivity.this).showSetting(permissions);
                                }
                            }
                        })
                        .start();
                break;
            default:
                break;
        }
    }

    public class LoadDataThread extends Thread {
        private String url;

        public LoadDataThread(String url) {
            super();
            this.url = url;
        }

        @Override
        public void run() {
            try {
                showLoadingImage(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showDownLoadDialog(final String path) {
        final CustomDialog dialog = new CustomDialog(VideoPlayActivity.this,
                ScreenUtils.getScreenWidth(VideoPlayActivity.this) * 3 / 4,
                ScreenUtils.getScreenHeight(VideoPlayActivity.this) / 4,
                R.layout.picture_base_dialog, R.style.Theme_dialog);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_commit = (Button) dialog.findViewById(R.id.btn_commit);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
        tv_title.setText("温馨提示");
        tv_content.setText("是否保存视频至手机？");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showProgressDialog(VideoPlayActivity.this, "");
                loadDataThread = new LoadDataThread(path);
                loadDataThread.start();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * @param url 图片链接
     */
    public void showLoadingImage(String url) {
        try {
            URL u = new URL(url);
            String path = PictureFileUtils.createDir(VideoPlayActivity.this,
                    videoName + ".mp4", Constants.HISTORY_VIDEO);
            byte[] buffer = new byte[1024 * 8];
            int read;
            int ava = 0;
            long start = System.currentTimeMillis();
            BufferedInputStream bin;
            bin = new BufferedInputStream(u.openStream());
            BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path));
            while ((read = bin.read(buffer)) > -1) {
                bout.write(buffer, 0, read);
                ava += read;
                long speed = ava / (System.currentTimeMillis() - start);
            }
            bout.flush();
            bout.close();

            Message message = handler.obtainMessage();
            message.what = 200;
            message.obj = path;
            handler.sendMessage(message);

            //更新图库
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File(path));
            intent.setData(uri);
            sendBroadcast(intent);
        } catch (IOException e) {
            Toast.makeText(VideoPlayActivity.this, "视频保存失败！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    String path = (String) msg.obj;
                    Toast.makeText(VideoPlayActivity.this,
                            "视频保存成功至：" + "\n" + path, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "视频保存地址：" + path);
                    DialogUtil.dismissProgressDialog();
                    break;
            }
        }
    };

    @Override
    public void back(View view) {
        super.back(view);
        finish();
        overridePendingTransition(0, R.anim.fade_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(0, R.anim.fade_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
