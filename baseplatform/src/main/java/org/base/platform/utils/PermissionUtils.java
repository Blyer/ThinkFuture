package org.base.platform.utils;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.base.platform.activity.BaseActivity;
import org.base.platform.dialog.UnifyDialog;

/**
 * Created by YinShengyi on 2016/12/2.
 */
public class PermissionUtils {

    /**
     * 申请权限前判断是否需要声明
     */
    public static void requestEachPermissions(final BaseActivity activity, String desc, final String[] permissions, final int requestCode) {
        if (shouldShowRequestPermissionRationale(activity, permissions)) {// 需要再次声明
            UnifyDialog dialog = new UnifyDialog(activity, "", desc, "知道了");
            dialog.setOnRightBtnClickListener(new UnifyDialog.OnRightBtnClickListener() {
                @Override
                public void onRightBtnClick() {
                    ActivityCompat.requestPermissions(activity, permissions, requestCode);
                }
            });
            dialog.show();
        } else {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    /**
     * 再次申请权限时，是否需要声明
     */
    private static boolean shouldShowRequestPermissionRationale(BaseActivity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检察每个权限是否申请
     *
     * @return true 需要申请权限,false 已申请权限
     */
    public static boolean checkEachSelfPermission(BaseActivity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查回调结果
     */
    public static boolean checkEachPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
