package com.ysy.thinkfuture.core.activity.helper;

import com.ysy.thinkfuture.core.activity.LoginActivity;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.ResponseResult;
import org.base.platform.callback.OnRequestCallback;
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
        mHttpUtils.request(httpRequestPackage, new OnRequestCallback() {
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

    public void getUserInfo(HttpRequestPackage httpRequestPackage) {
        mHttpUtils.request(httpRequestPackage, new OnRequestCallback() {
            @Override
            public void success(ResponseResult result) {
                if (result.getCode() == 0) {
                    mActivity.getUserInfoSuccess(result.getData());
                } else {
                    mActivity.getUserInfoFailed(result.getMessage());
                }
            }

            @Override
            public void failed(String reason) {
                mActivity.getUserInfoFailed(reason);
            }
        });
    }
}
