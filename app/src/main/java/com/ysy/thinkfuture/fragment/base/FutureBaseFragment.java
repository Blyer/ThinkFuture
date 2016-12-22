package com.ysy.thinkfuture.fragment.base;

import com.ysy.thinkfuture.utils.HandleHttpRequestResult;

import org.base.platform.bean.ResponseResult;
import org.base.platform.fragment.BaseFragment;

/**
 * Created by Blyer on 2016-12-11.
 */
public abstract class FutureBaseFragment extends BaseFragment {
    @Override
    public void processNetRequest(int id, ResponseResult result, boolean isCache) {
        HandleHttpRequestResult.handleResult(mActivity, result);
    }
}
