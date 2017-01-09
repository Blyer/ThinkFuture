package com.ysy.thinkfuture.core.helper.activityhelper;

import com.ysy.thinkfuture.core.activity.SingleTypeListActivity;
import com.ysy.thinkfuture.core.helper.base.BaseHelper;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.ResponseResult;
import org.base.platform.utils.HttpUtils;
import org.base.platform.utils.JsonUtils;

/**
 * Created by Blyer on 2017-01-06.
 */
public class SingleTypeListHelper extends BaseHelper {
    private SingleTypeListActivity mActivity;

    public SingleTypeListHelper(SingleTypeListActivity activity) {
        super();
        mActivity = activity;
    }

    public void getList(HttpRequestPackage httpRequestPackage, boolean showLoadingDialog) {
        mHttpUtils.addRequest(httpRequestPackage, new HttpUtils.OnRequestListener() {
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
        }, showLoadingDialog);
    }

}
