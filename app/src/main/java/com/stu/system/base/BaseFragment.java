package com.stu.system.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stu.system.R;
import com.stu.system.common.Constants;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected String TAG = getClass().getSimpleName();
    protected Activity context;
    protected LayoutInflater inflater;
    private View mView;

    private long clickTime = 0;
    private int tempViewId;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        //缓存view 防止fragment复用的时候重复执行onCreateView加载布局
        if (mView == null) {
            mView = onFragmentCreated(inflater, container, savedInstanceState);
            initView();
            initData();
            setListener();
        } else {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        return mView;
    }

    /**
     * 加载布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * @Description:findviewById
     */
    protected abstract void initView();

    /**
     * @Description:设置数据
     */
    protected abstract void initData();

    /**
     * @Description:设置监听
     */
    protected abstract void setListener();

    /**
     * @param v
     * @Description: 点击事件的封装
     */
    protected abstract void onClickEvent(View v);


    @Override
    public void onClick(View v) {
        if ((System.currentTimeMillis() - clickTime) > Constants.CLICK_INTERVAL) {
            clickTime = System.currentTimeMillis();
        } else {
            if (tempViewId == v.getId()) {
                return;
            }
        }
        tempViewId = v.getId();
        onClickEvent(v);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
    }

    /**
     * @param clazz void
     * @MethodName:goActivity
     * @Description: 跳转到Activity
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}