package org.base.platform.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import org.base.platform.R;
import org.base.platform.application.BaseApplication;

/**
 * Created by YinShengyi on 2015/4/24.
 */
public class ToastUtils {

    public static Toast toast;

    static {
        toast = new Toast(BaseApplication.getContext());

        LayoutInflater inflate = (LayoutInflater) BaseApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast_layout, null);
        toast.setView(v);
    }

    public static void show(String text) {
        toast.setText(text);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(int resId) {
        toast.setText(resId);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLongToast(String text) {
        toast.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showLongToast(int resId) {
        toast.setText(resId);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

}
