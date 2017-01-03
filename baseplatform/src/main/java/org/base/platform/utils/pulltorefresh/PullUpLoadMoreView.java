package org.base.platform.utils.pulltorefresh;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import org.base.platform.R;
import org.base.platform.utils.BaseUtils;

public class PullUpLoadMoreView extends BaseView {

    private ImageView img_progress;
    private ObjectAnimator mAnim;
    private float mLastPogress;

    public PullUpLoadMoreView(Context context) {
        this(context, null);
    }

    public PullUpLoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullUpLoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.refresh_view, this, true);
        img_progress = (ImageView) findViewById(R.id.img_progress);
        if (!isInEditMode()) {
            setPadding(0, BaseUtils.dp2px(10), 0, BaseUtils.dp2px(10));
        }
        setGravity(Gravity.TOP);
    }

    @Override
    public void begin() {
        mAnim = ObjectAnimator.ofFloat(img_progress, "rotation", 0, 0);
        mAnim.setInterpolator(new LinearInterpolator());
        mAnim.setTarget(img_progress);
    }

    @Override
    public void progress(float progress) {
        if (mAnim == null) {
            return;
        }
        float start = mLastPogress;
        float end = progress;
        mLastPogress = end;
        if (mAnim.isRunning()) {
            mAnim.cancel();
        }
        mAnim.setFloatValues(start, end);
        mAnim.setRepeatCount(0);
        mAnim.setDuration(100);
        mAnim.start();
    }

    @Override
    public void finishing(float progress) {
        if (mAnim != null) {
            mAnim.cancel();
        }
    }

    @Override
    public void loading() {
        if (mAnim == null) {
            return;
        }
        if (mAnim.isRunning()) {
            mAnim.cancel();
        }
        mAnim.setFloatValues(mLastPogress, mLastPogress + 360);
        mAnim.setDuration(1000);
        mAnim.setRepeatCount(ValueAnimator.INFINITE);
        mAnim.start();
    }

    @Override
    public void normal() {
        if (mAnim != null) {
            mAnim.cancel();
        }
    }

}
