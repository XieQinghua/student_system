package com.stu.system.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.stu.system.R;
import com.stu.system.adapter.ClassStuAdapter;
import com.stu.system.base.BaseFragment;
import com.stu.system.bean.GetClassStuBean;
import com.stu.system.common.Constants;
import com.stu.system.http.Api;
import com.stu.system.http.ApiLoader;
import com.stu.system.http.SimpleCallback;
import com.stu.system.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class StuManClassFragment extends BaseFragment {

    @BindView(R.id.gv_class_stu)
    GridView gvClassStu;
    Unbinder unbinder;
    private View view;

    protected List<GetClassStuBean.ValueBean> stuList = new ArrayList<>();
    private ClassStuAdapter adapter;
    private String url, cid;

    public StuManClassFragment(String cid) {
        this.cid = cid;
    }

    @Override
    protected View onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stu_man_class, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        //EventBus.getDefault().register(this);
        url = SPUtils.getInstance().getString(Constants.HOST, "");
        adapter = new ClassStuAdapter(getActivity(), R.layout.item_class_stu);
        gvClassStu.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        getClassStu();
    }

    private void getClassStu() {
        ApiLoader.reqGetClassStu(url + Api.GET_CLASS_STU, cid, new SimpleCallback<GetClassStuBean>() {
            @Override
            public void onNext(GetClassStuBean bean) {
                if (bean.getCode() == 1) {
                    if (bean.getValue().get(0) != null && bean.getValue().get(0).size() != 0) {
                        stuList = bean.getValue().get(0);
                        adapter.setData(stuList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
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