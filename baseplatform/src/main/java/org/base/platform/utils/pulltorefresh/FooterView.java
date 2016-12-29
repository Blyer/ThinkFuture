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

public class FooterView extends BaseView {

    private ImageView img_progress;
    private ObjectAnimator mAnim;
    private float mLastPogress;

    public FooterView(Context context) {
        this(context, null);
    }

    public FooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    }

    @Override
    public void progress(float progress) {
        float start = mLastPogress;
        float end = progress;
        if (start != end) {
            mLastPogress = end;
            ObjectAnimator anim = ObjectAnimator.ofFloat(img_progress, "rotation", start, end);
            anim.setDuration(100);
            anim.start();
        }
    }

    @Override
    public void finishing(float progress) {
        if (mAnim != null) {
            mAnim.cancel();
        }
    }

    @Override
    public void loading() {
        mAnim = ObjectAnimator.ofFloat(img_progress, "rotation", 0, 360);
        mAnim.setDuration(1000);
        mAnim.setRepeatCount(ValueAnimator.INFINITE);
        mAnim.setInterpolator(new LinearInterpolator());
        mAnim.start();
    }

    @Override
    public void normal() {
        if (mAnim != null) {
            mAnim.cancel();
        }
    }

}
