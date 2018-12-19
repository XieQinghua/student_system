package com.stu.system.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.SizeUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.stu.system.R;
import com.stu.system.adapter.FullyGridLayoutManager;
import com.stu.system.adapter.GridImageAdapter;
import com.stu.system.base.BaseActivity;
import com.stu.system.bean.GetActionListBean;
import com.stu.system.bean.SaveHistoryBean;
import com.stu.system.common.Constants;
import com.stu.system.http.Api;
import com.stu.system.http.ApiLoader;
import com.stu.system.http.SimpleCallback;
import com.stu.system.permission.DefaultRationale;
import com.stu.system.permission.PermissionSetting;
import com.stu.system.util.DialogUtil;
import com.stu.system.util.DrawableUtils;
import com.stu.system.util.SPUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SaveHistoryActivity extends BaseActivity {

    public final static int CHOOSE_PIC = 100;
    public final static int CHOOSE_VIDEO = 200;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.tv_action_title)
    TextView tvActionTitle;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.rv_pic)
    RecyclerView rvPic;
    @BindView(R.id.rv_video)
    RecyclerView rvVideo;

    private GridImageAdapter picAdapter, videoAdapter;

    private String url, sid, sname, actionDate, actionName, actionTitle, action, actionInfo, uid;

    private List<GetActionListBean.ValueBean> actionBeanList = new ArrayList<>();
    private List<String> actionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_history);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    @SuppressLint("NewApi")
    private void initView() {
        final GradientDrawable checkedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.TRAN_MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.TRAN_MAIN_COLOR);
        final GradientDrawable uncheckedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.MAIN_COLOR);
        final StateListDrawable selector = DrawableUtils.getSelector(checkedShape, uncheckedShape);
        btnSave.setBackground(selector);

        //初始化上传图片组件
        FullyGridLayoutManager picManager = new FullyGridLayoutManager(SaveHistoryActivity.this, 4, GridLayoutManager.VERTICAL, false);
        rvPic.setLayoutManager(picManager);
        picAdapter = new GridImageAdapter(SaveHistoryActivity.this, onAddPicClickListener);
        picAdapter.setList(selectPicList);
        picAdapter.setSelectMax(3);
        rvPic.setAdapter(picAdapter);
        picAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectPicList.size() > 0) {
                    LocalMedia media = selectPicList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            PictureSelector.create(SaveHistoryActivity.this).themeStyle(R.style.picture_white_style).openExternalPreview(position, selectPicList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(SaveHistoryActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(SaveHistoryActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });

        //初始化上传视频组件
        FullyGridLayoutManager videoManager = new FullyGridLayoutManager(SaveHistoryActivity.this, 4, GridLayoutManager.VERTICAL, false);
        rvVideo.setLayoutManager(videoManager);
        videoAdapter = new GridImageAdapter(SaveHistoryActivity.this, onAddVideoClickListener);
        videoAdapter.setList(selectVideoList);
        videoAdapter.setSelectMax(1);
        rvVideo.setAdapter(videoAdapter);
        videoAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectVideoList.size() > 0) {
                    LocalMedia media = selectVideoList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 2:
                            // 预览视频
                            PictureSelector.create(SaveHistoryActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(SaveHistoryActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            requestPermission(Permission.CAMERA, "pic");
        }
    };

    private GridImageAdapter.onAddPicClickListener onAddVideoClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            requestPermission(Permission.CAMERA, "video");
        }
    };

    private void initData() {
        url = SPUtils.getInstance().getString(Constants.HOST, "");
        sid = getIntent().getStringExtra("sid");
        sname = getIntent().getStringExtra("sname");
        tvTitle.setText("为" + sname + "添加日志");

        uid = SPUtils.getInstance().getString(Constants.UID, "");

        getActionList();
    }

    private void getActionList() {
        ApiLoader.reqGetActionList(url + Api.GET_ACTION_LIST, new SimpleCallback<GetActionListBean>() {
            @Override
            public void onNext(GetActionListBean bean) {
                if (bean.getCode() == 1) {
                    if (bean.getValue().get(0) != null && bean.getValue().get(0).size() != 0) {
                        actionBeanList = bean.getValue().get(0);
                        for (GetActionListBean.ValueBean b : actionBeanList) {
                            actionList.add(b.getAname());
                        }
                    }
                }
            }
        });
    }

    @OnClick({R.id.tv_date, R.id.tv_action, R.id.btn_save})
    public void onClick(View view) {
        hideKeyboard(view);
        switch (view.getId()) {
            case R.id.tv_date:
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                TimePickerView pvDate = new TimePickerBuilder(SaveHistoryActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        actionDate = sdf.format(date);
                        tvDate.setText(actionDate);
                        if (!TextUtils.isEmpty(actionName)) {
                            tvActionTitle.setText(actionDate + " " + actionName);
                            actionTitle = actionDate + actionName;
                        }
                    }
                })
                        //.setType(new boolean[]{true, true, true, true, true, false})
                        .setTitleColor(Constants.MAIN_COLOR)
                        .setCancelColor(Constants.MAIN_COLOR)
                        .setSubmitColor(Constants.MAIN_COLOR)
                        .build();
                pvDate.show();
                break;
            case R.id.tv_action:
                OptionsPickerView opvAction = new OptionsPickerBuilder(SaveHistoryActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        actionName = actionList.get(options1);
                        action = actionBeanList.get(options1).getAid();
                        tvAction.setText(actionName);
                        if (!TextUtils.isEmpty(actionDate)) {
                            tvActionTitle.setText(actionDate + " " + actionName);
                            actionTitle = actionDate + actionName;
                        }
                    }
                }).setTitleColor(Constants.MAIN_COLOR)
                        .setCancelColor(Constants.MAIN_COLOR)
                        .setSubmitColor(Constants.MAIN_COLOR)
                        .build();
                opvAction.setPicker(actionList);
                opvAction.show();
                break;
            case R.id.btn_save:
                saveHistory();
                break;
            default:
                break;
        }
    }

    public void saveHistory() {
        actionInfo = etInfo.getText().toString().trim();

        if (TextUtils.isEmpty(actionDate)) {
            Toast.makeText(SaveHistoryActivity.this, "请选择日期！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(actionName)) {
            Toast.makeText(SaveHistoryActivity.this, "请选择动作！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (picPathList.size() == 0) {
            Toast.makeText(SaveHistoryActivity.this, "请选择图片！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(videoPath)) {
            Toast.makeText(SaveHistoryActivity.this, "请选择视频！", Toast.LENGTH_SHORT).show();
            return;
        }

        DialogUtil.showProgressDialog(SaveHistoryActivity.this, "");

        MultipartBody.Part partSid = MultipartBody.Part.createFormData("sid", sid);
        MultipartBody.Part partDate = MultipartBody.Part.createFormData("date", actionDate);
        MultipartBody.Part partTitle = MultipartBody.Part.createFormData("title", actionTitle);
        MultipartBody.Part partAction = MultipartBody.Part.createFormData("action", action);
        MultipartBody.Part partInfo = MultipartBody.Part.createFormData("info", actionInfo);
        MultipartBody.Part partImgcount = MultipartBody.Part.createFormData("imgcount", picPathList.size() + "");
        MultipartBody.Part partUid = MultipartBody.Part.createFormData("uid", uid);

        //图片参数
        List<MultipartBody.Part> picPartFileList = new ArrayList<>();
        for (int i = 0; i < picPathList.size(); i++) {
            File file = new File(picPathList.get(i));
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            MultipartBody.Part picPartFile = MultipartBody.Part.createFormData("mFile0" + (i + 1) + "; filename=" + file.getName(), file.getName(), requestBody);
            picPartFileList.add(picPartFile);
        }
        //视频参数
        File videoFile = new File(videoPath);
        RequestBody videoRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), videoFile);
        MultipartBody.Part videoPartFile = MultipartBody.Part.createFormData("mFile04; filename=" + videoFile.getName(), videoFile.getName(), videoRequestBody);
        //视频缩略图参数
        File videoThumbFile = new File(videoThumbPath);
        RequestBody videoThumbRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), videoThumbFile);
        MultipartBody.Part videoThumbPartFile = MultipartBody.Part.createFormData("mFile05; filename=" + videoThumbFile.getName(), videoThumbFile.getName(), videoThumbRequestBody);

        ApiLoader.reqSaveHistory(url + Api.SAVE_HISTORY,
                partSid,
                partDate,
                partTitle,
                partAction,
                partInfo,
                partImgcount,
                partUid,
                picPartFileList,
                videoPartFile,
                videoThumbPartFile,
                new SimpleCallback<SaveHistoryBean>() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onNext(SaveHistoryBean bean) {
                        if (bean.getCode() == 1) {
                            Toast.makeText(SaveHistoryActivity.this, "添加日志成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(SaveHistoryActivity.this, bean.getInfo(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        DialogUtil.dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtil.dismissProgressDialog();
                        Toast.makeText(SaveHistoryActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void hideKeyboard(View v) {
        try {
            ((InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(SaveHistoryActivity.this
                                    .getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestPermission(String permissions, final String type) {
        switch (permissions) {
            case Permission.CAMERA:
                AndPermission.with(this)
                        .permission(permissions)
                        .rationale(new DefaultRationale())
                        .onGranted(new Action() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onAction(List<String> permissions) {
                                switch (type) {
                                    case "pic":
                                        openAlbumGetPic();
                                        break;
                                    case "video":
                                        openAlbumGetVideo();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                Toast.makeText(SaveHistoryActivity.this, "相机权限获取失败", Toast.LENGTH_SHORT).show();
                                if (AndPermission.hasAlwaysDeniedPermission(SaveHistoryActivity.this, permissions)) {
                                    new PermissionSetting(SaveHistoryActivity.this).showSetting(permissions);
                                }
                            }
                        })
                        .start();
                break;
            default:
                break;
        }
    }

    /**
     * 打开相册获取图片
     */
    private List<LocalMedia> selectPicList = new ArrayList<>();
    private List<String> picPathList = new ArrayList<>();
    private String picPath;

    private void openAlbumGetPic() {
        Log.d(TAG, "获取图片=======");
        PictureSelector.create(SaveHistoryActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles
                .maxSelectNum(3)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(false)// 是否预览音频
                .compress(true)//
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.setOutputCameraPath("/Chinayie/App")// 自定义拍照保存路径
                .compress(true)// 是否压缩
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(240, 240)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .isGif(false)// 是否显示gif图片
                .openClickSound(true)// 是否开启点击声音
                .selectionMedia(selectPicList)// 是否传入已选图片
                .previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频
                //.recordVideoSecond()//录制视频秒数 默认60秒
                .forResult(CHOOSE_PIC);//结果回调onActivityResult code
    }

    /**
     * 打开相册获取视频
     */
    private List<LocalMedia> selectVideoList = new ArrayList<>();
    private String videoPath;
    private String videoThumbPath;

    private void openAlbumGetVideo() {
        Log.d(TAG, "获取视频=======");
        PictureSelector.create(SaveHistoryActivity.this)
                .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true)// 是否预览音频
                .compress(true)// 是否压缩
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.setOutputCameraPath("/Chinayie/App")// 自定义拍照保存路径
                .compress(true)// 是否压缩
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(240, 240)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .isGif(false)// 是否显示gif图片
                .openClickSound(true)// 是否开启点击声音
                .selectionMedia(selectVideoList)// 是否传入已选图片
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频
                //.recordVideoSecond()//录制视频秒数 默认60秒
                .forResult(CHOOSE_VIDEO);//结果回调onActivityResult code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PIC:
                    // 图片选择
                    selectPicList = PictureSelector.obtainMultipleResult(data);
                    picPath = selectPicList.get(0).getPath();
                    for (LocalMedia lm : selectPicList) {
                        //Log.e(TAG, "原图path=" + lm.getPath() + ";\n压缩图path=" + lm.getCompressPath());
                        picPathList.add(lm.getCompressPath());
                    }
                    picAdapter.setList(selectPicList);
                    picAdapter.notifyDataSetChanged();
                    break;
                case CHOOSE_VIDEO:
                    // 视频选择
                    selectVideoList = PictureSelector.obtainMultipleResult(data);
                    videoPath = selectVideoList.get(0).getPath();
                    Log.e(TAG, "视频path=" + videoPath);
                    saveBitmap(getVideoThumb(videoPath), Constants.VIDEO_THUMB_IMG + "2018");
                    videoThumbPath = Constants.VIDEO_THUMB_IMG + "2018.png";
                    Log.e(TAG, "视频缩略图path=" + videoThumbPath);
                    //for (LocalMedia lm : selectVideoList) {
                    //    Log.e(TAG, "视频path=" + lm.getPath() + ";\n压缩视频path=" + lm.getCompressPath());
                    //}
                    videoAdapter.setList(selectVideoList);
                    videoAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获取视频文件截图
     *
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    public static Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }

    /**
     * 保存bitmap到本地
     *
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Bitmap mBitmap, String path) {
        File filePic;
        try {
            filePic = new File(path + ".png");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filePic.getAbsolutePath();
    }
}
