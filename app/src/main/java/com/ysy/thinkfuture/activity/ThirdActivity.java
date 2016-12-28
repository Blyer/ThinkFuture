package com.ysy.thinkfuture.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

import com.apkfuns.logutils.LogUtils;
import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.activity.base.FutureBaseActivity;
import com.ysy.thinkfuture.fragment.FirstFragment;

import org.base.platform.bean.MessageEvent;
import org.base.platform.bean.ResponseResult;
import org.base.platform.utils.StatusBarCompat;

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
        StatusBarCompat.compat(this, getResources().getColor(org.base.platform.R.color.blue_1));
        initFileCacheUtils();
        FirstFragment fragment = (FirstFragment) FirstFragment.get();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rl_container, fragment);
        transaction.commit();
    }

    @Override
    protected void processMessageEvent(MessageEvent event) {
        LogUtils.e(event.data.toString());
    }

    @Override
    public void processNetRequest(int id, ResponseResult result, boolean isCache) {
        super.processNetRequest(id, result, isCache);
    }

}
