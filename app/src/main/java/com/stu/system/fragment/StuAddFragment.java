package com.stu.system.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.stu.system.base.BaseFragment;
import com.stu.system.bean.ClassBean;
import com.stu.system.bean.SaveStuBean;
import com.stu.system.common.Constants;
import com.stu.system.eventbus.StuAddEvent;
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

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * 添加学生
 */
public class StuAddFragment extends BaseFragment {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_stu_name)
    EditText etStuName;
    @BindView(R.id.tv_stu_sex)
    TextView tvStuSex;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.tv_stu_class)
    TextView tvStuClass;
    @BindView(R.id.tv_stu_birthday)
    TextView tvStuBirthday;
    @BindView(R.id.et_stu_address)
    EditText etStuAddress;
    @BindView(R.id.iv_add_pic)
    ImageView ivAddPic;
    @BindView(R.id.btn_save)
    Button btnSave;
    Unbinder unbinder;

    private View view;

    private GradientDrawable checkedShape;
    private GradientDrawable uncheckedShape;
    private StateListDrawable selector;
    private List<String> sexList;
    private String url, name, sex, tel, cid, bron, address, uid;
    private List<ClassBean> classList = new ArrayList<>();
    private List<String> classNameList = new ArrayList<>();

    @Override
    protected View onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stu_add, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        //EventBus.getDefault().register(this);
        ivBack.setVisibility(View.INVISIBLE);
        tvTitle.setText(R.string.stu_add);

        checkedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.TRAN_MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.TRAN_MAIN_COLOR);
        uncheckedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.MAIN_COLOR);
        selector = DrawableUtils.getSelector(checkedShape, uncheckedShape);
        btnSave.setBackground(selector);

        etStuName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(etStuName.getText())) {
                    btnSave.setBackground(selector);
                    btnSave.setClickable(true);
                } else {
                    btnSave.setBackground(checkedShape);
                    btnSave.setClickable(false);
                }
            }
        });

        tvStuSex.setOnClickListener(this);
        tvStuClass.setOnClickListener(this);
        tvStuBirthday.setOnClickListener(this);
        ivAddPic.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        url = SPUtils.getInstance().getString(Constants.HOST, "");

        sexList = new ArrayList<>();
        sexList.add("男");
        sexList.add("女");

        classList = SPUtils.getInstance().getDataList(Constants.CLASS_LIST, ClassBean.class, "");
        for (ClassBean c : classList) {
            classNameList.add(c.getCname());
            //Log.e(TAG, "cid=" + c.getCid() + ";cname=" + c.getCname() + ";\n");
        }

        uid = SPUtils.getInstance().getString(Constants.UID, "");
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onClickEvent(View v) {
        hideKeyboard(v);
        switch (v.getId()) {
            case R.id.tv_stu_sex:
                OptionsPickerView sexPvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        sex = sexList.get(options1);
                        tvStuSex.setText(sex);
                    }
                }).setTitleColor(Constants.MAIN_COLOR)
                        .setCancelColor(Constants.MAIN_COLOR)
                        .setSubmitColor(Constants.MAIN_COLOR)
                        .build();
                sexPvOptions.setPicker(sexList);
                sexPvOptions.show();
                break;
            case R.id.tv_stu_class:
                OptionsPickerView classPvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        cid = classList.get(options1).getCid();
                        tvStuClass.setText(classNameList.get(options1));
                    }
                }).setTitleColor(Constants.MAIN_COLOR)
                        .setCancelColor(Constants.MAIN_COLOR)
                        .setSubmitColor(Constants.MAIN_COLOR)
                        .build();
                classPvOptions.setPicker(classNameList);
                classPvOptions.show();
                break;
            case R.id.tv_stu_birthday:
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        bron = sdf.format(date);
                        tvStuBirthday.setText(bron);
                    }
                }).setTitleColor(Constants.MAIN_COLOR)
                        .setCancelColor(Constants.MAIN_COLOR)
                        .setSubmitColor(Constants.MAIN_COLOR)
                        .build();
                pvTime.show();
                break;
            case R.id.iv_add_pic:
                requestPermission(Permission.CAMERA);
                break;
            case R.id.btn_save:
                saveStu();
                break;
            default:
                break;
        }
    }

    @SuppressLint("NewApi")
    private void hideKeyboard(View v) {
        try {
            ((InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity()
                                    .getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestPermission(String permissions) {
        switch (permissions) {
            case Permission.CAMERA:
                AndPermission.with(this)
                        .permission(permissions)
                        .rationale(new DefaultRationale())
                        .onGranted(new Action() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onAction(List<String> permissions) {
                                openAlbum();
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {
                                Toast.makeText(getActivity(), "相机权限获取失败", Toast.LENGTH_SHORT).show();
                                if (AndPermission.hasAlwaysDeniedPermission(getActivity(), permissions)) {
                                    new PermissionSetting(getActivity()).showSetting(permissions);
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
     * 打开相册
     */
    private List<LocalMedia> selectList = new ArrayList<>();
    private String picPath;

    private void openAlbum() {
        PictureSelector.create(StuAddFragment.this)//这里要注意！！！
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(false)// 是否预览音频
                .compress(true)
                .isCamera(true)// 是否显示拍照按钮
                .enableCrop(true)// 是否裁剪
                .withAspectRatio(4, 3)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.setOutputCameraPath("/Chinayie/App")// 自定义拍照保存路径
                .compress(true)// 是否压缩
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(240, 240)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .isGif(false)// 是否显示gif图片
                .openClickSound(true)// 是否开启点击声音
                //.selectionMedia(selectList)// 是否传入已选图片
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频
                //.recordVideoSecond()//录制视频秒数 默认60秒
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    public void saveStu() {
        name = etStuName.getText().toString().trim();
        tel = etPhoneNumber.getText().toString().trim();
        address = etStuAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "请填写学生姓名！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(sex)) {
            Toast.makeText(getActivity(), "请选择学生性别！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tel)) {
            Toast.makeText(getActivity(), "请填写联系电话！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cid)) {
            Toast.makeText(getActivity(), "请选择学生所属班级！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(bron)) {
            Toast.makeText(getActivity(), "请选择学生出生日期！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getActivity(), "请填写学生家庭住址！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(picPath)) {
            Toast.makeText(getActivity(), "请选择学生头像！", Toast.LENGTH_SHORT).show();
            return;
        }

        DialogUtil.showProgressDialog(getActivity(), "");

        MultipartBody.Part partName = MultipartBody.Part.createFormData("name", name);
        MultipartBody.Part partSex = MultipartBody.Part.createFormData("sex", sex);
        MultipartBody.Part partTel = MultipartBody.Part.createFormData("tel", tel);
        MultipartBody.Part partCid = MultipartBody.Part.createFormData("cid", cid);
        MultipartBody.Part partBron = MultipartBody.Part.createFormData("bron", bron);
        MultipartBody.Part partAddress = MultipartBody.Part.createFormData("address", address);
        MultipartBody.Part partUid = MultipartBody.Part.createFormData("uid", uid);

        File file = new File(picPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("pic1", file.getName(), requestBody);

        ApiLoader.reqSaveStu(url + Api.SAVE_STU,
                partName,
                partSex,
                partTel,
                partCid,
                partBron,
                partAddress,
                partUid,
                partFile,
                new SimpleCallback<SaveStuBean>() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onNext(SaveStuBean bean) {
                        if (bean.getCode() == 1) {
                            Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new StuAddEvent(cid));
                            btnSave.setBackground(checkedShape);
                            btnSave.setClickable(false);
                        } else {
                            Toast.makeText(getActivity(), bean.getInfo(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        DialogUtil.dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtil.dismissProgressDialog();
                        Toast.makeText(getActivity(), "网络错误，请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    picPath = selectList.get(0).getCompressPath();
                    Log.d(TAG, "原图path=" + selectList.get(0).getPath());
                    Log.d(TAG, "压缩图path=" + selectList.get(0).getCompressPath());
                    ivAddPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivAddPic.setImageURI(Uri.fromFile(new File(selectList.get(0).getCompressPath())));
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
