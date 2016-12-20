package com.ysy.thinkfuture.fragment.base;

import org.base.platform.bean.ResponseResult;
import org.base.platform.dialog.UnifyDialog;
import org.base.platform.fragment.BaseFragment;
import org.base.platform.utils.ActivityCollector;

/**
 * Created by Blyer on 2016-12-11.
 */
public abstract class FutureBaseFragment extends BaseFragment {
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
        }
    }
}
