package org.base.platform.utils.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public abstract class BaseView extends RelativeLayout {
    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 开始下拉
     */
    public abstract void begin();

    /**
     * 回调的精度,单位为px
     *
     * @param progress 当前高度
     */
    public abstract void progress(float progress);

    public abstract void finishing(float progress);

    /**
     * 下拉完毕
     */
    public abstract void loading();

    /**
     * 看不见的状态
     */
    public abstract void normal();

}
