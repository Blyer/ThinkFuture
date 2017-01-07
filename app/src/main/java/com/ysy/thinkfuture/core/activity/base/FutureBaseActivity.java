package com.ysy.thinkfuture.core.activity.base;

import com.ysy.thinkfuture.utils.HandleHttpRequestResult;

import org.base.platform.activity.BaseActivity;
import org.base.platform.bean.MessageEvent;
import org.base.platform.bean.ResponseResult;
import org.base.platform.constants.MsgEventConstants;
import org.base.platform.utils.ActivityCollector;

/**
 * Created by YinShengyi on 2016/12/4.
 */
public abstract class FutureBaseActivity extends BaseActivity {

    @Override
    protected void processMessageEvent(MessageEvent event) {
        super.processMessageEvent(event);
        switch (event.id) {
            case MsgEventConstants.NET_REQUEST_RESULT:
                if (ActivityCollector.getCurrentActivity() == this) {
                    ResponseResult result = (ResponseResult) event.data;
                    HandleHttpRequestResult.handleResult(mActivity, result);
                }
                break;
        }
    }


}
