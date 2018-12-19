package com.stu.system.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.stu.system.R;
import com.stu.system.adapter.FragmentTabAdapter;
import com.stu.system.base.BaseActivity;
import com.stu.system.eventbus.ShowStuManClassEvent;
import com.stu.system.fragment.MineFragment;
import com.stu.system.fragment.StuAddFragment;
import com.stu.system.fragment.StuManFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.rb_stu_mange)
    RadioButton rbStuMange;
    @BindView(R.id.rb_stu_add)
    RadioButton rbStuAdd;
    @BindView(R.id.rb_mine)
    RadioButton rbMine;

    private ArrayList<Fragment> fragments;
    private StuManFragment myStuManFragment;
    private StuAddFragment myStuAddFragment;
    private MineFragment myMineFragment;
    private FragmentTabAdapter myFragmentTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        myStuManFragment = new StuManFragment();
        myStuAddFragment = new StuAddFragment();
        myMineFragment = new MineFragment();
        fragments = new ArrayList<Fragment>();
        fragments.add(myStuManFragment);
        fragments.add(myStuAddFragment);
        fragments.add(myMineFragment);

        myFragmentTabAdapter = new FragmentTabAdapter(MainActivity.this,
                getSupportFragmentManager(),
                fragments,
                R.id.content, rg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowStuManClassEvent(ShowStuManClassEvent event) {
        rbStuMange.setChecked(true);
        myFragmentTabAdapter.showTab(0);
    }


    /**
     * 主页监听按两次返回键退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                ActivityUtils.finishAllActivities();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
