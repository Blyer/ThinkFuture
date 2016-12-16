package org.base.platform.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

/**
 * Created by YinShengyi on 2016/12/7.
 * Activity跳转工具类
 */
public class JumpUtils {

    public static void jump(Activity activity, Intent intent) {
        jump(activity, intent, 0, 0);
    }

    public static void jump(Activity activity, Intent intent, int openNewInAnimId, int openOldOutAnimId) {
        if (activity == null || intent == null) {
            return;
        }
        if (openNewInAnimId != 0 && openOldOutAnimId != 0) {
            activity.startActivity(intent);
            activity.overridePendingTransition(openNewInAnimId, openOldOutAnimId);
        } else {
            activity.startActivity(intent);
        }
    }

    public static void jumpForResult(Activity activity, Intent intent, int requestCode) {
        jumpForResult(activity, intent, requestCode, 0, 0);
    }

    public static void jumpForResult(Activity activity, Intent intent, int requestCode, int openNewInAnimId, int openOldOutAnimId) {
        if (activity == null || intent == null) {
            return;
        }
        if (openNewInAnimId != 0 && openOldOutAnimId != 0) {
            activity.startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(openNewInAnimId, openOldOutAnimId);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void jump(Fragment fragment, Intent intent) {
        jump(fragment, intent, 0, 0);
    }

    public static void jump(Fragment fragment, Intent intent, int openNewInAnimId, int openOldOutAnimId) {
        if (fragment == null || intent == null || fragment.getActivity() == null) {
            return;
        }
        if (openNewInAnimId != 0 && openOldOutAnimId != 0) {
            fragment.startActivity(intent);
            fragment.getActivity().overridePendingTransition(openNewInAnimId, openOldOutAnimId);
        } else {
            fragment.startActivity(intent);
        }
    }

    public static void jumpForResult(Fragment fragment, Intent intent, int requestCode) {
        jumpForResult(fragment, intent, requestCode, 0, 0);
    }

    public static void jumpForResult(Fragment fragment, Intent intent, int requestCode, int openNewInAnimId, int openOldOutAnimId) {
        if (fragment == null || intent == null || fragment.getActivity() == null) {
            return;
        }
        if (openNewInAnimId != 0 && openOldOutAnimId != 0) {
            fragment.startActivityForResult(intent, requestCode);
            fragment.getActivity().overridePendingTransition(openNewInAnimId, openOldOutAnimId);
        } else {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

}
