package org.base.platform.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import org.base.platform.R;

/**
 * @author yinshengyi yinshengyi2533@tops001.com
 * @version V1.0
 * @Description:自定义的Button，主要用于设置背景
 * @date 16-5-16 下午4:53
 */
public class UnifyButton extends TextView {

    private Context mContext;
    private Resources mResources;
    private int mNormalTextColor;
    private int mUnEnableTextColor;

    private long mLastClickTime = 0;

    public UnifyButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mResources = getResources();
        setGravity(Gravity.CENTER);

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.UnifyButton);
        float cornerSize = typedArray.getDimensionPixelSize(R.styleable.UnifyButton_cornerSize, getResources().getDimensionPixelSize(R.dimen.normal_corner_size));
        int bgNormalColor = typedArray.getColor(R.styleable.UnifyButton_bgNormalColor, mResources.getColor(R.color.orange_1));
        int bgPressedColor = typedArray.getColor(R.styleable.UnifyButton_bgPressedColor, mResources.getColor(R.color.orange_2));
        int bgUnEnableColor = typedArray.getColor(R.styleable.UnifyButton_bgUnEnableColor, mResources.getColor(R.color.grey_2));
        mNormalTextColor = typedArray.getColor(R.styleable.UnifyButton_normalTextColor, mResources.getColor(R.color.white));
        mUnEnableTextColor = typedArray.getColor(R.styleable.UnifyButton_unEnableTextColor, mResources.getColor(R.color.white));
        typedArray.recycle();

        StateListDrawable bgDrawable = new StateListDrawable();

        GradientDrawable d1 = new GradientDrawable();
        d1.setCornerRadius(cornerSize);
        d1.setColor(bgPressedColor);
        bgDrawable.addState(new int[]{android.R.attr.state_pressed}, d1);

        GradientDrawable d2 = new GradientDrawable();
        d2.setCornerRadius(cornerSize);
        d2.setColor(bgUnEnableColor);
        bgDrawable.addState(new int[]{-android.R.attr.state_enabled}, d2);

        GradientDrawable d3 = new GradientDrawable();
        d3.setCornerRadius(cornerSize);
        d3.setColor(bgNormalColor);
        bgDrawable.addState(new int[]{}, d3);

        setBackgroundDrawable(bgDrawable);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setTextColor(mNormalTextColor);
        } else {
            setTextColor(mUnEnableTextColor);
        }
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
