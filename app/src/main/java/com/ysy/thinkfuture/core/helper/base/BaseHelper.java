package com.ysy.thinkfuture.core.helper.base;

import org.base.platform.utils.HttpUtils;

/**
 * Created by YinShengyi on 2017/1/9.
 */
public class BaseHelper {

    protected HttpUtils mHttpUtils;

    public BaseHelper() {
        mHttpUtils = new HttpUtils();
    }

    public void request() {
        mHttpUtils.request();
    }
}
