package com.ysy.thinkfuture.core.fragment.helper;

import com.ysy.thinkfuture.constants.HttpRequestCode;
import com.ysy.thinkfuture.core.fragment.FirstFragment;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.ResponseResult;
import org.base.platform.utils.HttpUtils;

/**
 * Created by Blyer on 2017-01-06.
 */
public class FirstFragmentHelper {
    private HttpUtils mHttpUtils;
    private FirstFragment mFragment;

    public FirstFragmentHelper(FirstFragment fragment) {
        mFragment = fragment;
        mHttpUtils = new HttpUtils();
    }

    public void getCustomerDetail(HttpRequestPackage httpRequestPackage) {
        mHttpUtils.request(httpRequestPackage, new HttpUtils.OnRequestListener() {
            @Override
            public void success(ResponseResult result) {
                if (result.getCode() == 0) {
                    mFragment.getCustomerDetailSuccess(result.getData());
                } else {
                    mFragment.getCustomerDetailFailed(result.getMessage());
                }
            }

            @Override
            public void failed(String reason) {
                mFragment.getCustomerDetailFailed(reason);
            }
        });
    }

    public void getCustomerSource(HttpRequestPackage httpRequestPackage) {
        mHttpUtils.request(httpRequestPackage, new HttpUtils.OnRequestListener() {
            @Override
            public void success(ResponseResult result) {
                if (result.getCode() == 0) {
                    mFragment.getCustomerSourceSuccess(result.getData());
                }
            }

            @Override
            public void failed(String reason) {

            }
        });
    }

    public void getCustomerGrade(HttpRequestPackage httpRequestPackage) {
        mHttpUtils.request(httpRequestPackage, new HttpUtils.OnRequestListener() {
            @Override
            public void success(ResponseResult result) {
                if (result.getCode() == 0) {
                    mFragment.getCustomerGradeSuccess(result.getData());
                }
            }

            @Override
            public void failed(String reason) {

            }
        });
    }
}
