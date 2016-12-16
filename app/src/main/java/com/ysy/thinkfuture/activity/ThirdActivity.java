package com.ysy.thinkfuture.activity;

import android.app.FragmentTransaction;
import android.content.Intent;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.activity.base.FutureBaseActivity;
import com.ysy.thinkfuture.fragment.FirstFragment;

import org.base.platform.bean.ResponseResult;

public class ThirdActivity extends FutureBaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_third;
    }

    @Override
    protected void resolveIntent(Intent intent) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        initFileCacheUtils();
        FirstFragment fragment = (FirstFragment) FirstFragment.get();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.rl_container, fragment);
        transaction.commit();
    }

    @Override
    public void processNetRequest(int id, ResponseResult result, boolean isCache) {

    }

    @Override
    protected int getStatusBarColor() {
        return getResources().getColor(R.color.blue_1);
    }
}
