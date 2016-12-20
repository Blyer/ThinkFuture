package com.ysy.thinkfuture.activity.base;

import org.base.platform.activity.BaseActivity;
import org.base.platform.bean.ResponseResult;
import org.base.platform.dialog.UnifyDialog;
import org.base.platform.utils.ActivityCollector;
import org.base.platform.utils.ToastUtils;

/**
 * Created by YinShengyi on 2016/12/4.
 */
public abstract class FutureBaseActivity extends BaseActivity {
    @Override
    protected void backgroundToFront() {
        ToastUtils.show("切入前台");
        super.backgroundToFront();
    }

    @Override
    public void processNetRequest(int id, ResponseResult result, boolean isCache) {
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
        }
    }
}
