package com.stu.system.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.stu.system.R;
import com.stu.system.base.BaseActivity;
import com.stu.system.bean.GetStuHisListBean;
import com.stu.system.common.Constants;
import com.stu.system.http.Api;
import com.stu.system.http.ApiLoader;
import com.stu.system.http.SimpleCallback;
import com.stu.system.util.DrawableUtils;
import com.stu.system.util.LightStatusBarUtils;
import com.stu.system.util.SPUtils;
import com.stu.system.util.ToolbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StuHisListActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    private String url, sid, sname;
    private List<GetStuHisListBean.ValueBean> stuHisList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_his_list);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    @SuppressLint("NewApi")
    private void initView() {
        LightStatusBarUtils.setLightStatusBar(StuHisListActivity.this, true);
        ToolbarUtil.setColor(StuHisListActivity.this, getResources().getColor(R.color.blue));

        final GradientDrawable checkedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.TRAN_MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.TRAN_MAIN_COLOR);
        final GradientDrawable uncheckedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.MAIN_COLOR);
        final StateListDrawable selector = DrawableUtils.getSelector(checkedShape, uncheckedShape);
    }

    private void initData() {
        url = SPUtils.getInstance().getString(Constants.HOST, "");
        sid = getIntent().getStringExtra("sid");
        sname = getIntent().getStringExtra("sname");
        tvTitle.setText(sname + "日志");

        getStuHisList("1");
    }

    private void getStuHisList(String pageindex) {
        ApiLoader.reqGetStuHisList(url + Api.GET_STU_HISLIST, sid, pageindex, new SimpleCallback<GetStuHisListBean>() {
            @Override
            public void onNext(GetStuHisListBean bean) {
                if (bean.getCode() == 1) {
                    if (bean.getValue().get(0) != null && bean.getValue().get(0).size() != 0) {
                        stuHisList = bean.getValue().get(0);
                        //TODO 获得某学生日志列表
                    }
                }
            }
        });
    }


    @OnClick({})
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }
}
