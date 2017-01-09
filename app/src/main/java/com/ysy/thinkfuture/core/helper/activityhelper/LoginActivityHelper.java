package com.ysy.thinkfuture.core.helper.activityhelper;

import com.ysy.thinkfuture.core.activity.LoginActivity;
import com.ysy.thinkfuture.core.helper.base.BaseHelper;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.ResponseResult;
import org.base.platform.utils.HttpUtils;

/**
 * Created by YinShengyi on 2017/1/6.
 */
public class LoginActivityHelper extends BaseHelper {

    private LoginActivity mActivity;

    public LoginActivityHelper(LoginActivity activity) {
        super();
        mActivity = activity;
    }

    public void login(HttpRequestPackage httpRequestPackage, boolean showLoadingDialog) {
        mHttpUtils.addRequest(httpRequestPackage, new HttpUtils.OnRequestListener() {
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
        }, showLoadingDialog);
    }

    public void getUserInfo(HttpRequestPackage httpRequestPackage, boolean showLoadingDialog) {
        mHttpUtils.addRequest(httpRequestPackage, new HttpUtils.OnRequestListener() {
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
        }, showLoadingDialog);
    }

}
