package com.ysy.thinkfuture.core.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.core.activity.base.FutureBaseActivity;
import com.ysy.thinkfuture.core.fragment.SecondFragment;

import org.base.platform.utils.StatusBarUtils;

public class SecondActivity extends FutureBaseActivity implements View.OnClickListener {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_second;
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
        initFragment();
    }

    @Override
    protected void begin() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private void initFragment() {
        SecondFragment fragment = (SecondFragment) SecondFragment.get();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_container, fragment);
        transaction.commit();
    }
}
