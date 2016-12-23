package org.base.platform.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by YinShengyi on 2016/12/23.
 */
public class UnifyView extends View {

    private long mLastClickTime = 0;

    public UnifyView(Context context) {
        super(context);
    }

    public UnifyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnifyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - mLastClickTime > 500) {
            mLastClickTime = currentClickTime;
            return super.performClick();
        } else {
            return true;
        }
    }
}
