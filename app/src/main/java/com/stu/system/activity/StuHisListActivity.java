package com.stu.system.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.qbw.customview.RefreshLoadMoreLayout;
import com.stu.system.R;
import com.stu.system.adapter.StuHistoryAdapter;
import com.stu.system.base.BaseActivity;
import com.stu.system.bean.GetStuHisListBean;
import com.stu.system.common.Constants;
import com.stu.system.http.Api;
import com.stu.system.http.ApiLoader;
import com.stu.system.http.SimpleCallback;
import com.stu.system.util.DialogUtil;
import com.stu.system.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StuHisListActivity extends BaseActivity implements RefreshLoadMoreLayout.CallBack {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.lv_history)
    ListView lvHistory;
    @BindView(R.id.refresh_load_more)
    RefreshLoadMoreLayout refreshLoadMore;

    private String url, sid, sname;
    private List<GetStuHisListBean.ValueBean> stuHisList;
    private StuHistoryAdapter stuHistoryAdapter;

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
        refreshLoadMore.init(new RefreshLoadMoreLayout.Config(this)
                .showLastRefreshTime(getClass()).canRefresh(true));
        refreshLoadMore.setCanLoadMore(true);

        View stuHisHead = LayoutInflater.from(StuHisListActivity.this).inflate(R.layout.layout_stu_his_head, null);
        lvHistory.addHeaderView(stuHisHead);

        stuHisList = new ArrayList<>();
        stuHistoryAdapter = new StuHistoryAdapter(StuHisListActivity.this, R.layout.item_stu_history);
        stuHistoryAdapter.setData(stuHisList);
        lvHistory.setAdapter(stuHistoryAdapter);
    }

    private void initData() {
        url = SPUtils.getInstance().getString(Constants.HOST, "");
        sid = getIntent().getStringExtra("sid");
        sname = getIntent().getStringExtra("sname");
        tvTitle.setText(sname + "日志");

        getStuHisList("1");
    }

    private void getStuHisList(String pageindex) {
        DialogUtil.showProgressDialog(StuHisListActivity.this, "");

        ApiLoader.reqGetStuHisList(url + Api.GET_STU_HISLIST, sid, pageindex, new SimpleCallback<GetStuHisListBean>() {
            @Override
            public void onNext(GetStuHisListBean bean) {
                if (bean.getCode() == 1) {
                    if (bean.getValue().get(0) != null && bean.getValue().get(0).size() != 0) {
                        stuHisList = bean.getValue().get(0);
                        stuHistoryAdapter.setData(stuHisList);
                        stuHistoryAdapter.notifyDataSetChanged();
                    } else {
                        refreshLoadMore.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCompleted() {
                DialogUtil.dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                DialogUtil.dismissProgressDialog();
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText(R.string.network_error);
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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getStuHisList("1");
                refreshLoadMore.stopRefresh();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLoadMore.stopLoadMore();
            }
        }, 500);
    }
}
