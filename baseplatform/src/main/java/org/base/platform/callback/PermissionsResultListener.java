package org.base.platform.callback;

/**
 * Created by YinShengyi on 2016/12/9.
 */
public interface PermissionsResultListener {
    /**
     * 获取权限
     */
    void onPermissionGranted();

    /**
     * 权限申请被拒绝
     */
    void onPermissionDenied();
}
