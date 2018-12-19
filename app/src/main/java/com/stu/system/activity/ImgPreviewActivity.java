package com.stu.system.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.dialog.CustomDialog;
import com.luck.picture.lib.photoview.OnViewTapListener;
import com.luck.picture.lib.photoview.PhotoView;
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

public class ImgPreviewActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.preview_image)
    PhotoView previewImage;

    private String imgTitle, imgName, imgUrl;

    private LoadDataThread loadDataThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_preview);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        LightStatusBarUtils.setLightStatusBar(ImgPreviewActivity.this, false);
        ToolbarUtil.setColor(ImgPreviewActivity.this, getResources().getColor(R.color.text_main_color));

        imgTitle = getIntent().getStringExtra("imgTitle");
        imgName = getIntent().getStringExtra("imgName");
        imgUrl = getIntent().getStringExtra("imgUrl");
        tvTitle.setText(imgTitle);
        tvSave.setVisibility(View.VISIBLE);
        Log.e(TAG, "imgName====" + imgName);
        Log.e(TAG, "imgUrl====" + imgUrl);
    }

    private void initData() {
        DialogUtil.showProgressDialog(this, "");
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(ImgPreviewActivity.this)
                .asBitmap()
                .load(imgUrl)
                .apply(options)
                .into(new SimpleTarget<Bitmap>(480, 800) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        DialogUtil.dismissProgressDialog();
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        DialogUtil.dismissProgressDialog();
                        previewImage.setImageBitmap(resource);
                    }
                });

        previewImage.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
                overridePendingTransition(0, R.anim.fade_out);
            }
        });
    }

    @OnClick({R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                requestPermission(Permission.WRITE_EXTERNAL_STORAGE);
                break;
            default:
                break;
        }
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
                                showDownLoadDialog(imgUrl);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                Toast.makeText(ImgPreviewActivity.this, "读取内存卡权限被拒绝", Toast.LENGTH_SHORT).show();
                                if (AndPermission.hasAlwaysDeniedPermission(ImgPreviewActivity.this, permissions)) {
                                    new PermissionSetting(ImgPreviewActivity.this).showSetting(permissions);
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
        final CustomDialog dialog = new CustomDialog(ImgPreviewActivity.this,
                ScreenUtils.getScreenWidth(ImgPreviewActivity.this) * 3 / 4,
                ScreenUtils.getScreenHeight(ImgPreviewActivity.this) / 5,
                R.layout.picture_base_dialog, R.style.Theme_dialog);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_commit = (Button) dialog.findViewById(R.id.btn_commit);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
        tv_title.setText("温馨提示");
        tv_content.setText("是否保存图片至手机？");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showProgressDialog(ImgPreviewActivity.this, "");
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
            String path = PictureFileUtils.createDir(ImgPreviewActivity.this,
                    imgName + ".jpg", Constants.HISTORY_IMG);
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
            Toast.makeText(ImgPreviewActivity.this, "图片保存失败！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ImgPreviewActivity.this,
                            "图片保存成功至：" + "\n" + path, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "图片保存地址：" + path);
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
