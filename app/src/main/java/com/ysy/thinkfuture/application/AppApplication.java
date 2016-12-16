package com.ysy.thinkfuture.application;

import com.ysy.thinkfuture.BuildConfig;

import org.base.platform.application.BaseApplication;

/**
 * Created by YinShengyi on 2016/11/25.
 */
public class AppApplication extends BaseApplication {
    @Override
    protected boolean isRelease() {
        return "release".equals(BuildConfig.BUILD_TYPE);
    }
}
