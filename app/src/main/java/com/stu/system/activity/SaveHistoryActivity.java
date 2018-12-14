package com.stu.system.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.SizeUtils;
import com.stu.system.R;
import com.stu.system.base.BaseActivity;
import com.stu.system.bean.GetActionListBean;
import com.stu.system.common.Constants;
import com.stu.system.http.Api;
import com.stu.system.http.ApiLoader;
import com.stu.system.http.SimpleCallback;
import com.stu.system.util.DrawableUtils;
import com.stu.system.util.LightStatusBarUtils;
import com.stu.system.util.SPUtils;
import com.stu.system.util.ToolbarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaveHistoryActivity extends BaseActivity {

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
    @BindView(R.id.iv_add_pic)
    ImageView ivAddPic;
    @BindView(R.id.btn_save)
    Button btnSave;

    private String url, sid, sname, actionDate, actionName, title;

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
        LightStatusBarUtils.setLightStatusBar(SaveHistoryActivity.this, true);
        ToolbarUtil.setColor(SaveHistoryActivity.this, getResources().getColor(R.color.blue));

        final GradientDrawable checkedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.TRAN_MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.TRAN_MAIN_COLOR);
        final GradientDrawable uncheckedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.MAIN_COLOR);
        final StateListDrawable selector = DrawableUtils.getSelector(checkedShape, uncheckedShape);
        btnSave.setBackground(selector);
    }

    private void initData() {
        url = SPUtils.getInstance().getString(Constants.HOST, "");
        sid = getIntent().getStringExtra("sid");
        sname = getIntent().getStringExtra("sname");
        tvTitle.setText("为" + sname + "添加日志");

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
        switch (view.getId()) {
            case R.id.tv_date:
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                TimePickerView pvDate = new TimePickerBuilder(SaveHistoryActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        actionDate = sdf.format(date);
                        tvDate.setText(actionDate);
                        if (!TextUtils.isEmpty(actionName)) {
                            tvActionTitle.setText(actionDate + actionName);
                            title = actionDate + actionName;
                        }
                    }
                }).setType(new boolean[]{true, true, true, true, true, false})
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
                        tvAction.setText(actionName);
                        if (!TextUtils.isEmpty(actionDate)) {
                            tvActionTitle.setText(actionDate + actionName);
                            title = actionDate + actionName;
                        }
                    }
                }).setTitleColor(Constants.MAIN_COLOR)
                        .setCancelColor(Constants.MAIN_COLOR)
                        .setSubmitColor(Constants.MAIN_COLOR)
                        .build();
                opvAction.setPicker(actionList);
                opvAction.show();
                break;
            default:
                break;
        }
    }
}
