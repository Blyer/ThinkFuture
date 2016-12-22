package com.ysy.thinkfuture.utils;

import org.base.platform.activity.BaseActivity;
import org.base.platform.bean.ResponseResult;
import org.base.platform.dialog.UnifyDialog;
import org.base.platform.utils.ActivityCollector;
import org.base.platform.utils.ToastUtils;

/**
 * Created by YinShengyi on 2016/12/22.
 */
public class HandleHttpRequestResult {
    public static void handleResult(BaseActivity mActivity, ResponseResult result) {
        if (result.getCode() == 1) {
            UnifyDialog dialog = new UnifyDialog(mActivity, "", "账号在另一台设备上登录了", "退出");
            dialog.setOnRightBtnClickListener(new UnifyDialog.OnRightBtnClickListener() {
                @Override
                public void onRightBtnClick() {
                    ActivityCollector.finishAll();
                }
            });
            dialog.show();
            return;
        } else if (result.getCode() == 4002) {
            ToastUtils.show(result.getMessage());
        }
    }
}
