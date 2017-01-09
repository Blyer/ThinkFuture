package com.ysy.thinkfuture.core.helper.fragmenthelper;

import com.ysy.thinkfuture.core.fragment.FirstFragment;
import com.ysy.thinkfuture.core.helper.base.BaseHelper;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.ResponseResult;
import org.base.platform.utils.HttpUtils;

/**
 * Created by Blyer on 2017-01-06.
 */
public class FirstFragmentHelper extends BaseHelper {
    private FirstFragment mFragment;

    public FirstFragmentHelper(FirstFragment fragment) {
        super();
        mFragment = fragment;
    }

    public void getCustomerDetail(HttpRequestPackage httpRequestPackage, boolean showLoadingDialog) {
        mHttpUtils.addRequest(httpRequestPackage, new HttpUtils.OnRequestListener() {
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
        }, showLoadingDialog);
    }

    public void getCustomerSource(HttpRequestPackage httpRequestPackage, boolean showLoadingDialog) {
        mHttpUtils.addRequest(httpRequestPackage, new HttpUtils.OnRequestListener() {
            @Override
            public void success(ResponseResult result) {
                if (result.getCode() == 0) {
                    mFragment.getCustomerSourceSuccess(result.getData());
                }
            }

            @Override
            public void failed(String reason) {

            }
        }, showLoadingDialog);
    }

    public void getCustomerGrade(HttpRequestPackage httpRequestPackage, boolean showLoadingDialog) {
        mHttpUtils.addRequest(httpRequestPackage, new HttpUtils.OnRequestListener() {
            @Override
            public void success(ResponseResult result) {
                if (result.getCode() == 0) {
                    mFragment.getCustomerGradeSuccess(result.getData());
                }
            }

            @Override
            public void failed(String reason) {

            }
        }, showLoadingDialog);
    }

}
