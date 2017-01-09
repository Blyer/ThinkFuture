package com.ysy.thinkfuture.core.activity;

import android.support.v4.app.FragmentTransaction;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.core.activity.base.FutureBaseActivity;
import com.ysy.thinkfuture.core.fragment.FirstFragment;

import org.base.platform.utils.StatusBarUtils;

public class ThirdActivity extends FutureBaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_third;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        StatusBarUtils.compat(this, getResources().getColor(org.base.platform.R.color.blue_1));
        initFileCacheUtils();
        FirstFragment fragment = (FirstFragment) FirstFragment.get();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rl_container, fragment);
        transaction.commit();
    }

    @Override
    protected void begin() {

    }

}
