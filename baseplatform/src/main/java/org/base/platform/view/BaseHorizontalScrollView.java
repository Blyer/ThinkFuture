package org.base.platform.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;


/**
 * @author YinShengyi
 * @version v1.0
 * @date 2016/9/7.
 */
public class BaseHorizontalScrollView extends HorizontalScrollView {

    private OnScrollListener mListener;

    public BaseHorizontalScrollView(Context context, AttributeSet attrs) {
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