package com.ysy.thinkfuture.core;

import com.ysy.thinkfuture.activity.LoginActivity;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.ResponseResult;
import org.base.platform.utils.HttpUtils;

/**
 * Created by YinShengyi on 2017/1/6.
 */
public class LoginActivityHelper {

    private LoginActivity mActivity;
    private HttpUtils mHttpUtils;

    public LoginActivityHelper(LoginActivity activity) {
        mActivity = activity;
        mHttpUtils = new HttpUtils();
    }

    public void login(HttpRequestPackage httpRequestPackage) {
        mHttpUtils.request(httpRequestPackage, new HttpUtils.OnRequestListener() {
            @Override
            public void success(ResponseResult result) {
                if (result.getCode() == 0) {
                    mActivity.loginSucess();
                } else {
                    mActivity.loginFailed(result.getMessage());
                }
            }

            @Override
            public void failed(String reason) {
                mActivity.loginFailed(reason);
            }
        });
    }
}
