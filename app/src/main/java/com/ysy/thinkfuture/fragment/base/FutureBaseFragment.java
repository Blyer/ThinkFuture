package com.ysy.thinkfuture.fragment.base;

import com.ysy.thinkfuture.utils.HandleHttpRequestResult;

import org.base.platform.bean.MessageEvent;
import org.base.platform.bean.ResponseResult;
import org.base.platform.constants.MsgEventConstants;
import org.base.platform.fragment.BaseFragment;

/**
 * Created by Blyer on 2016-12-11.
 */
public abstract class FutureBaseFragment extends BaseFragment {

    @Override
    protected void processMessageEvent(MessageEvent event) {
        super.processMessageEvent(event);
        switch (event.id) {
            case MsgEventConstants.NET_REQUEST_RESULT:
                ResponseResult result = (ResponseResult) event.data;
                HandleHttpRequestResult.handleResult(mActivity, result);
                break;
        }
    }
}
