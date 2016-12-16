package org.base.platform.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by YinShengyi on 2016/10/17.
 */
public class BaseScrollView extends ScrollView {

    private OnScrollListener mListener;

    public BaseScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.scroll(l, t, oldl, oldt);
        }
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mListener = listener;
    }

    public interface OnScrollListener {
        void scroll(int currHoriOrigin, int currVertOrigin, int oldHoriOrigin, int oldVertOrigin);
    }
}
