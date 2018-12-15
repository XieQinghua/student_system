package com.stu.system.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.stu.system.R;
import com.stu.system.adapter.FragmentTabAdapter;
import com.stu.system.base.BaseFragment;
import com.stu.system.bean.ClassBean;
import com.stu.system.bean.GetStuManBean;
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
import butterknife.Unbinder;

/**
 * 学生管理
 */
public class StuManFragment extends BaseFragment {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rg_class)
    RadioGroup rgClass;
    @BindView(R.id.ll_class_content)
    LinearLayout llClassContent;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    Unbinder unbinder;

    private View view;

    private List<Fragment> fragments = new ArrayList<>();
    public static FragmentTabAdapter tabAdapter;

    private String url;

    @Override
    protected View onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stu_manage, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        //EventBus.getDefault().register(this);
        ivBack.setVisibility(View.INVISIBLE);
        tvTitle.setText(R.string.stu_manage);

        url = SPUtils.getInstance().getString(Constants.HOST, "");
        //Log.e(TAG, "url=" + url);
    }

    @Override
    protected void initData() {
        getStuMan();
    }

    private void getStuMan() {
        DialogUtil.showProgressDialog(getActivity(), "");

        String uid = SPUtils.getInstance().getString(Constants.UID, "");
        ApiLoader.reqGetStuMan(url + Api.GET_STU_MAN, uid, new SimpleCallback<GetStuManBean>() {
            @Override
            public void onNext(GetStuManBean bean) {
                if (bean.getCode() == 1) {
                    if (bean.getValue().get(0) != null && bean.getValue().get(0).size() != 0) {
                        List<ClassBean> classList = bean.getValue().get(0);

                        SPUtils.getInstance().putDataList(Constants.CLASS_LIST, classList);

                        for (int i = 0; i < classList.size(); i++) {
                            RadioButton rb = (RadioButton) getLayoutInflater().inflate(R.layout.item_tab_radiobutton, null).findViewById(R.id.rb);
                            rb.setId(10000 + i);
                            rgClass.addView(rb);
                            rb.setText(classList.get(i).getCname());
                            ViewGroup.LayoutParams para = rb.getLayoutParams();
                            para.width = SizeUtils.dp2px(120);
                            para.height = SizeUtils.dp2px(45);
                            rb.setLayoutParams(para);

                            if (i == 0) {
                                rgClass.check(rb.getId());
                            }

                            fragments.add(new StuManClassFragment(classList.get(i).getCid()));
                        }
                        rgClass.setVisibility(View.VISIBLE);
                        tabAdapter = new FragmentTabAdapter(getActivity(), getActivity().getSupportFragmentManager(),
                                fragments, R.id.ll_class_content, rgClass);
                    } else {
                        rgClass.setVisibility(View.GONE);
                        llClassContent.setVisibility(View.GONE);
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
                rgClass.setVisibility(View.GONE);
                llClassContent.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
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