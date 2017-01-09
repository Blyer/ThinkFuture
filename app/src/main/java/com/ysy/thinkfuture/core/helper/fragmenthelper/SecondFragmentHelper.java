package com.ysy.thinkfuture.core.helper.fragmenthelper;

import com.ysy.thinkfuture.core.fragment.SecondFragment;
import com.ysy.thinkfuture.core.helper.base.BaseHelper;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.ResponseResult;
import org.base.platform.utils.HttpUtils;
import org.base.platform.utils.JsonUtils;

/**
 * Created by YinShengyi on 2017/1/9.
 */
public class SecondFragmentHelper extends BaseHelper {
    private SecondFragment mFragment;

    public SecondFragmentHelper(SecondFragment fragment) {
        super();
        mFragment = fragment;
    }

    public void getList(HttpRequestPackage httpRequestPackage, boolean showLoadingDialog) {
        mHttpUtils.addRequest(httpRequestPackage, new HttpUtils.OnRequestListener() {
            @Override
            public void success(ResponseResult result) {
                if (result.getCode() == 0) {
                    mFragment.getListSuccess(JsonUtils.jsonToList(result.getData(), String.class));
                } else {
                    mFragment.getListFailed();
                }
            }

            @Override
            public void failed(String reason) {
                mFragment.getListFailed();
            }
        }, showLoadingDialog);
    }
}
