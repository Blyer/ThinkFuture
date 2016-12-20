package org.base.platform.utils;

import android.view.View;

/**
 * Created by YinShengyi on 2016/12/20.
 */
public class ViewUtils {

    /**
     * 对点击事件进行包装防止快速多次点击产生多次事件
     */
    public static void setOnClickListener(View view, final OnClickListener listener) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setClickable(false);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                }, 500);
                listener.onClick(v);
            }
        });
    }

    public interface OnClickListener {
        void onClick(View v);
    }

}
