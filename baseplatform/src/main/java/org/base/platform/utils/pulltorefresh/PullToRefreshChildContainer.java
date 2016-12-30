package org.base.platform.utils.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by YinShengyi on 2016/12/30.
 */
public class PullToRefreshChildContainer extends LinearLayout {
    private View scrollView;

    public PullToRefreshChildContainer(Context context) {
        this(context, null);
    }

    public PullToRefreshChildContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshChildContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    public void setScrollView(View view) {
        scrollView = view;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        if (scrollView == null) {
            return false;
        } else {
            return scrollView.canScrollHorizontally(direction);
        }
    }

    @Override
    public boolean canScrollVertically(int direction) {
        if (scrollView == null) {
            return false;
        } else {
            return scrollView.canScrollVertically(direction);
        }
    }
}
