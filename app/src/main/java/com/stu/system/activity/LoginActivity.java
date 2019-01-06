package com.stu.system.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SizeUtils;
import com.stu.system.R;
import com.stu.system.base.BaseActivity;
import com.stu.system.bean.BaseBean;
import com.stu.system.bean.LoginBean;
import com.stu.system.common.Constants;
import com.stu.system.http.Api;
import com.stu.system.http.ApiLoader;
import com.stu.system.http.SimpleCallback;
import com.stu.system.util.DialogUtil;
import com.stu.system.util.DrawableUtils;
import com.stu.system.util.LightStatusBarUtils;
import com.stu.system.util.SPUtils;
import com.stu.system.util.ToolbarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_eye)
    CheckBox cbEye;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_login_set)
    TextView tvLoginSet;
    @BindView(R.id.tv_scanning)
    TextView tvScanning;

    private int startIndex;
    private int sumErrorTime;

    private static final int API_ERROR = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case API_ERROR: {
                    int errorTime = (int) msg.obj;
                    sumErrorTime += errorTime;
                    //Log.e(TAG, "sumErrorTime=" + sumErrorTime);
                    if (sumErrorTime == 254) {
                        DialogUtil.dismissProgressDialog();
                        Toast.makeText(LoginActivity.this,
                                "未扫描到服务器地址，请重试",
                                Toast.LENGTH_SHORT).show();
                        sumErrorTime = 0;
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    @SuppressLint("NewApi")
    private void initView() {
        LightStatusBarUtils.setLightStatusBar(LoginActivity.this, true);
        ToolbarUtil.setColor(LoginActivity.this, getResources().getColor(R.color.main_bg));

        final GradientDrawable checkedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.TRAN_MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.TRAN_MAIN_COLOR);
        final GradientDrawable uncheckedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.MAIN_COLOR);
        final StateListDrawable selector = DrawableUtils.getSelector(checkedShape, uncheckedShape);

        btnLogin.setBackground(checkedShape);
        btnLogin.setClickable(false);

        cbEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //显示密文
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().length());
                } else {
                    //显示明文
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().length());
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
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
                if (!TextUtils.isEmpty(etName.getText()) && !TextUtils.isEmpty(editable.toString())) {
                    btnLogin.setBackground(selector);
                    btnLogin.setClickable(true);
                } else {
                    btnLogin.setBackground(checkedShape);
                    btnLogin.setClickable(false);
                }
            }
        });
    }

    private void initData() {

    }

    @OnClick({R.id.btn_login, R.id.tv_login_set, R.id.tv_scanning})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_login_set:
                startActivity(LoginSetActivity.class);
                break;
            case R.id.tv_scanning:
                scanning();
                break;
            default:
                break;
        }
    }

    private void login() {
        if (TextUtils.isEmpty(SPUtils.getInstance().getString(Constants.HOST, ""))) {
            Toast.makeText(LoginActivity.this, "请设置本地服务器IP", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = SPUtils.getInstance().getString(Constants.HOST, "");
        final String name = etName.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        //点击登录按键隐藏键盘
        ((InputMethodManager) etPassword.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(LoginActivity.this
                                .getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

        //登录操作
        DialogUtil.showProgressDialog(this, "");

        ApiLoader.reqLogin(url + Api.LOGIN, name, password, new SimpleCallback<LoginBean>() {
            @Override
            public void onNext(LoginBean loginBean) {
                if (loginBean != null) {
                    if (loginBean.getCode() == 1) {
                        //保存token
                        SPUtils.getInstance().putString(Constants.NAME, name);
                        SPUtils.getInstance().putString(Constants.UID, loginBean.getValue().get(0).getUid());
                        SPUtils.getInstance().putString(Constants.UNAME, loginBean.getValue().get(0).getUname());
                        SPUtils.getInstance().putBoolean(Constants.IS_LOGIN, true);
                        startActivity(MainActivity.class);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, loginBean.getInfo(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCompleted() {
                DialogUtil.dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                DialogUtil.dismissProgressDialog();
                Toast.makeText(LoginActivity.this, "请检查IP是否设置正确", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;

    private void scanning() {
        //获取wifi服务
        mWifiManager = (WifiManager) LoginActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }

        mWifiInfo = mWifiManager.getConnectionInfo();
        String IPAddress = getWIFILocalIpAdress(mWifiInfo);
        //String IPAddress = NetworkUtils.getIPAddress(true);
        String ipFront = IPAddress.substring(0, IPAddress.lastIndexOf(".") + 1);
        String ipBack = IPAddress.substring(IPAddress.lastIndexOf(".") + 1, IPAddress.length());
        startIndex = Integer.parseInt(ipBack);
        Log.e(TAG, "ip=" + IPAddress);
        //Log.e(TAG, "ipFront=" + ipFront);
        //Log.e(TAG, "ipBack=" + ipBack);

        DialogUtil.showProgressDialog(LoginActivity.this, "扫描服务器地址中\n请耐心等候···");

        for (int i = 0; i < 255; i++) {
            if ((i + 1) != startIndex) {
                getConn(ipFront, i + 1);
            }
        }
    }

    private void getConn(final String ipFront, final int index) {
        final String apiAddress = "http://" + ipFront + index;
        Log.e(TAG, "apiAddress=" + apiAddress);

        ApiLoader.reqGetConn(apiAddress + Api.GET_CONN, new SimpleCallback<BaseBean>() {
            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean != null) {
                    if (baseBean.getCode() == 999) {
                        SPUtils.getInstance().putString(Constants.HOST, apiAddress);
                        Intent intent = new Intent(LoginActivity.this, LoginSetActivity.class);
                        intent.putExtra(Constants.HOST, apiAddress);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCompleted() {
                DialogUtil.dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                Message msg = new Message();
                msg.what = API_ERROR;
                msg.obj = 1;
                mHandler.sendMessage(msg);
            }
        });
    }

    public String getWIFILocalIpAdress(WifiInfo wifiInfo) {
        int ipAddress = wifiInfo.getIpAddress();
        return formatIpAddress(ipAddress);
    }

    private static String formatIpAddress(int ipAdress) {
        return (ipAdress & 0xFF) + "." +
                ((ipAdress >> 8) & 0xFF) + "." +
                ((ipAdress >> 16) & 0xFF) + "." +
                (ipAdress >> 24 & 0xFF);
    }
}