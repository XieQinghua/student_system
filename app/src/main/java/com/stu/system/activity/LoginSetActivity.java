package com.stu.system.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SizeUtils;
import com.stu.system.R;
import com.stu.system.base.BaseActivity;
import com.stu.system.common.Constants;
import com.stu.system.util.DrawableUtils;
import com.stu.system.util.LightStatusBarUtils;
import com.stu.system.util.SPUtils;
import com.stu.system.util.ToolbarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginSetActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_login_set)
    EditText etLoginSet;
    @BindView(R.id.btn_ok)
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_set);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    @SuppressLint("NewApi")
    private void initView() {
        LightStatusBarUtils.setLightStatusBar(LoginSetActivity.this, true);
        ToolbarUtil.setColor(LoginSetActivity.this, getResources().getColor(R.color.gray));

        final GradientDrawable checkedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.TRAN_MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.TRAN_MAIN_COLOR);
        final GradientDrawable uncheckedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.MAIN_COLOR);
        final StateListDrawable selector = DrawableUtils.getSelector(checkedShape, uncheckedShape);

        tvTitle.setText(getResources().getString(R.string.login_set));
        etLoginSet.setSelection(etLoginSet.getText().length());
        btnOk.setBackground(checkedShape);
        btnOk.setClickable(false);

        etLoginSet.addTextChangedListener(new TextWatcher() {
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
                if (!TextUtils.isEmpty(etLoginSet.getText())) {
                    btnOk.setBackground(selector);
                    btnOk.setClickable(true);
                } else {
                    btnOk.setBackground(checkedShape);
                    btnOk.setClickable(false);
                }
            }
        });
    }

    private void initData() {

    }

    @OnClick({R.id.btn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                String host = etLoginSet.getText().toString().trim();
                SPUtils.getInstance().putString(Constants.HOST, host);
                Toast.makeText(LoginSetActivity.this, "设置IP成功", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
