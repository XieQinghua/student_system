package com.stu.system.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import java.util.List;

public class FragmentTabAdapter implements RadioGroup.OnCheckedChangeListener {
    /**
     * 一个tab页面对应一个Fragment
     */
    private List<Fragment> fragments;
    /**
     * 用于切换tab
     */
    private RadioGroup rgs;
    /**
     * Fragment所属的Activity
     */
    private FragmentManager fragmentManager;
    /**
     * Activity中所要被替换的区域的id
     */
    private int fragmentContentId;
    /**
     * 当前Tab页面索引
     */
    private int currentTab;

    public FragmentTabAdapter(Context context, FragmentManager fragmentManager, List<Fragment> fragments, int fragmentContentId, RadioGroup rgs) {
        this.fragments = fragments;
        this.rgs = rgs;
        this.fragmentManager = fragmentManager;
        this.fragmentContentId = fragmentContentId;
        // 默认显示第一页
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        ft.addToBackStack(fragments.get(0).getClass().getName());
        ft.commit();
        rgs.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        for (int i = 0; i < rgs.getChildCount(); i++) {
            if (rgs.getChildAt(i).getId() == checkedId) {
                Fragment fragment = fragments.get(i);
                FragmentTransaction ft = obtainFragmentTransaction(i);
                getCurrentFragment().onPause();
                getCurrentFragment().onStop();
                if (fragment.isAdded()) {
                    // 启动目标tab的onStart()
                    fragment.onStart();
                    // 启动目标tab的onResume()
                    fragment.onResume();
                } else {
                    ft.addToBackStack(fragment.getClass().getName());
                    ft.add(fragmentContentId, fragment);
                }

                //显示目标tab
                showTab(i);
                ft.commit();
            }
        }
    }

    /**
     * 切换tab
     *
     * @param idx
     */
    public void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        // 更新目标tab为当前tab
        currentTab = idx;
    }

    /**
     * 获取一个带动画的FragmentTransaction
     *
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // 设置切换动画
        //ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
        return ft;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

}
