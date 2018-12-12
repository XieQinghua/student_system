package com.stu.system.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.stu.system.R;
import com.stu.system.activity.LoginActivity;
import com.stu.system.base.BaseFragment;
import com.stu.system.common.Constants;
import com.stu.system.util.DrawableUtils;
import com.stu.system.util.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_login_name)
    TextView tvLoginName;
    @BindView(R.id.tv_login_uname)
    TextView tvLoginUname;
    Unbinder unbinder;

    private View view;

    @Override
    protected View onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        //EventBus.getDefault().register(this);
        ivBack.setVisibility(View.INVISIBLE);
        tvTitle.setText(R.string.mine);

        tvVersion.setText("当前版本：" + AppUtils.getAppName() + " v" + AppUtils.getAppVersionName());
        tvLoginName.setText("当前账号：" + SPUtils.getInstance().getString(Constants.NAME, ""));
        tvLoginUname.setText("当前老师：" + SPUtils.getInstance().getString(Constants.UNAME, ""));

        final GradientDrawable checkedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.TRAN_MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.TRAN_MAIN_COLOR);
        final GradientDrawable uncheckedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.MAIN_COLOR);
        final StateListDrawable selector = DrawableUtils.getSelector(checkedShape, uncheckedShape);
        btnLogout.setBackground(selector);
        btnLogout.setOnClickListener(this);
    }

    @Override
    protected void initData() {

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
        switch (v.getId()) {
            case R.id.btn_logout:
                SPUtils.getInstance().putBoolean(Constants.IS_LOGIN, false);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}