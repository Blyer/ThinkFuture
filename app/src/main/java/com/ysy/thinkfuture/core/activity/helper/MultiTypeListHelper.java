package com.ysy.thinkfuture.core.activity.helper;

import com.ysy.thinkfuture.core.activity.MultiTypeListActivity;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.ResponseResult;
import org.base.platform.callback.OnRequestCallback;
import org.base.platform.utils.HttpUtils;
import org.base.platform.utils.JsonUtils;

/**
 * Created by Blyer on 2017-01-06.
 */
public class MultiTypeListHelper {
    private HttpUtils mHttpUtils;
    private MultiTypeListActivity mActivity;

    public MultiTypeListHelper(MultiTypeListActivity activity) {
        mActivity = activity;
        mHttpUtils = new HttpUtils();
    }

    public void getList(HttpRequestPackage httpRequestPackage) {
        mHttpUtils.request(httpRequestPackage, new OnRequestCallback() {
            @Override
            public void success(ResponseResult result) {
                if (result.getCode() == 0) {
                    mActivity.getListSuccess(JsonUtils.jsonToList(result.getData(), String.class));
                } else {
                    mActivity.getListFailed();
                }
            }

            @Override
            public void failed(String reason) {
                mActivity.getListFailed();
            }
        });
    }

}
