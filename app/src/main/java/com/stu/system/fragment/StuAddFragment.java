package com.stu.system.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.stu.system.R;
import com.stu.system.base.BaseFragment;
import com.stu.system.common.Constants;
import com.stu.system.util.DrawableUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 添加学生
 */
public class StuAddFragment extends BaseFragment {
    private static final String TAG = StuAddFragment.class.getSimpleName();


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_save)
    Button btnSave;
    Unbinder unbinder;

    private View view;

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

        final GradientDrawable checkedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.TRAN_MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.TRAN_MAIN_COLOR);
        final GradientDrawable uncheckedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.MAIN_COLOR);
        final StateListDrawable selector = DrawableUtils.getSelector(checkedShape, uncheckedShape);
        btnSave.setBackground(selector);
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
