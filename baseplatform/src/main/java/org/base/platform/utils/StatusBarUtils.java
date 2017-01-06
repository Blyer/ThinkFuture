package org.base.platform.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;

/**
 * Created by YinShengyi on 15/9/21.
 */
public class StatusBarUtils {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(statusColor);
        }

    }
}
