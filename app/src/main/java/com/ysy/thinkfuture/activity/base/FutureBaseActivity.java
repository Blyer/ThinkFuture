package com.ysy.thinkfuture.activity.base;

import org.base.platform.activity.BaseActivity;
import org.base.platform.utils.ToastUtils;

/**
 * Created by YinShengyi on 2016/12/4.
 */
public abstract class FutureBaseActivity extends BaseActivity {
    @Override
    protected void backgroundToFront() {
        ToastUtils.show("切入前台");
        super.backgroundToFront();
    }
}
