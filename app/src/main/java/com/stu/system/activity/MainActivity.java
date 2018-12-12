package com.stu.system.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.stu.system.R;
import com.stu.system.adapter.FragmentTabAdapter;
import com.stu.system.base.BaseActivity;
import com.stu.system.fragment.MineFragment;
import com.stu.system.fragment.StuAddFragment;
import com.stu.system.fragment.StuManageFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rg)
    RadioGroup rg;

    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragments = new ArrayList<Fragment>();
        fragments.add(new StuManageFragment());
        fragments.add(new StuAddFragment());
        fragments.add(new MineFragment());

        new FragmentTabAdapter(MainActivity.this,
                getSupportFragmentManager(),
                fragments,
                R.id.content, rg);
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
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
