package com.ysy.thinkfuture.core.fragment.helper;

import android.os.Handler;
import android.os.Message;

import com.ysy.thinkfuture.core.fragment.FirstFragment;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.ResponseResult;
import org.base.platform.utils.HttpUtils;

/**
 * Created by Blyer on 2017-01-06.
 */
public class FirstFragmentHelper {
    private static final int MSG_REQUEST_FINISHED = 1000;
    private HttpUtils mHttpUtils;
    private FirstFragment mFragment;
    private Handler mHandler;

    public FirstFragmentHelper(FirstFragment fragment) {
        mFragment = fragment;
        mHttpUtils = new HttpUtils();
        mHandler = new Handler() {
            int requestNum = 0;

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_REQUEST_FINISHED:
                        ++requestNum;
                        if (requestNum == 3) {
                            mFragment.closeLoadingDialog();
                            requestNum = 0;
                        }
                        break;
                }
            }
        };
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

            @Override
            public void finish() {
                mHandler.sendEmptyMessage(MSG_REQUEST_FINISHED);
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

            @Override
            public void finish() {
                mHandler.sendEmptyMessage(MSG_REQUEST_FINISHED);
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

            @Override
            public void finish() {
                mHandler.sendEmptyMessage(MSG_REQUEST_FINISHED);
            }
        });
    }
}
