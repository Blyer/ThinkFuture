package org.base.platform.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.base.platform.R;
import org.base.platform.utils.StringUtils;

/**
 * Created by YinShengyi on 2017/7/4.
 */
public class ToggleRadioView extends LinearLayout {

    public static final int LEFT = 0;
    public static final int MIDDLE = 1;
    public static final int RIGHT = 2;
    public int mCurrentSelect = LEFT;
    private int mSelectColor;
    private int mUnSelectColor;
    private float mTextSize;

    private GradientDrawable mLeftSelectDrawable;
    private GradientDrawable mLeftUnSelectDrawable;
    private GradientDrawable mRightSelectDrawable;
    private GradientDrawable mRightUnSelectDrawable;

    private LinearLayout ll_root;
    private TextView tv_left;
    private TextView tv_middle;
    private TextView tv_right;

    private OnItemClickListener mOnItemClickListener;

    public ToggleRadioView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToggleRadioView);
        String leftText = StringUtils.getString(typedArray.getString(R.styleable.ToggleRadioView_left_text));
        String middleText = StringUtils.getString(typedArray.getString(R.styleable.ToggleRadioView_middle_text));
        String rightText = StringUtils.getString(typedArray.getString(R.styleable.ToggleRadioView_right_text));
        mSelectColor = typedArray.getColor(R.styleable.ToggleRadioView_select_color, getResources().getColor(R.color.blue_1));
        mUnSelectColor = typedArray.getColor(R.styleable.ToggleRadioView_unselect_color, getResources().getColor(R.color.white));
        mTextSize = typedArray.getDimension(R.styleable.ToggleRadioView_text_size, 14);
        mCurrentSelect = typedArray.getInt(R.styleable.ToggleRadioView_default_select, 0);
        typedArray.recycle();

        int radius = getResources().getDimensionPixelSize(R.dimen.toggle_radio_view_radius);

        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setCornerRadius(radius);
        backgroundDrawable.setColor(mSelectColor);
        backgroundDrawable.setStroke(1, mSelectColor);

        GradientDrawable dividerDrawable = new GradientDrawable();
        dividerDrawable.setColor(mSelectColor);
        dividerDrawable.setSize(1, 0);

        --radius;
        mLeftSelectDrawable = new GradientDrawable();
        mLeftSelectDrawable.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, radius, radius});
        mLeftSelectDrawable.setColor(mSelectColor);

        mLeftUnSelectDrawable = new GradientDrawable();
        mLeftUnSelectDrawable.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, radius, radius});
        mLeftUnSelectDrawable.setColor(mUnSelectColor);

        mRightSelectDrawable = new GradientDrawable();
        mRightSelectDrawable.setCornerRadii(new float[]{0, 0, radius, radius, radius, radius, 0, 0});
        mRightSelectDrawable.setColor(mSelectColor);

        mRightUnSelectDrawable = new GradientDrawable();
        mRightUnSelectDrawable.setCornerRadii(new float[]{0, 0, radius, radius, radius, radius, 0, 0});
        mRightUnSelectDrawable.setColor(mUnSelectColor);

        LayoutInflater.from(context).inflate(R.layout.toggle_radio_view, this);
        ll_root = (LinearLayout) findViewById(R.id.ll_root);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_middle = (TextView) findViewById(R.id.tv_middle);
        tv_right = (TextView) findViewById(R.id.tv_right);

        ll_root.setBackground(backgroundDrawable);
        ll_root.setDividerDrawable(dividerDrawable);

        tv_left.setText(leftText);
        tv_left.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        if (!StringUtils.isNull(middleText)) {
            tv_middle.setVisibility(VISIBLE);
            tv_middle.setText(middleText);
            tv_middle.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        }
        tv_right.setText(rightText);
        tv_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);

        if (mCurrentSelect == LEFT) {
            setLeftSelect();
        } else if (mCurrentSelect == MIDDLE) {
            setMiddleSelect();
        } else {
            setRightSelect();
        }
        init();
    }

    private void init() {
        tv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onLeftClick();
                }
                setLeftSelect();
            }
        });

        tv_middle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onMiddleClick();
                }
                setMiddleSelect();
            }
        });

        tv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onRightClick();
                }
                setRightSelect();
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public int getCurrentSelect() {
        return mCurrentSelect;
    }

    public void setLeftSelect() {
        mCurrentSelect = LEFT;
        tv_left.setBackground(mLeftSelectDrawable);
        tv_left.setTextColor(mUnSelectColor);
        tv_middle.setBackgroundColor(mUnSelectColor);
        tv_middle.setTextColor(mSelectColor);
        tv_right.setBackground(mRightUnSelectDrawable);
        tv_right.setTextColor(mSelectColor);
    }

    public void setMiddleSelect() {
        mCurrentSelect = MIDDLE;
        tv_left.setBackground(mLeftUnSelectDrawable);
        tv_left.setTextColor(mSelectColor);
        tv_middle.setBackgroundColor(mSelectColor);
        tv_middle.setTextColor(mUnSelectColor);
        tv_right.setBackground(mRightUnSelectDrawable);
        tv_right.setTextColor(mSelectColor);
    }

    public void setRightSelect() {
        mCurrentSelect = RIGHT;
        tv_left.setBackground(mLeftUnSelectDrawable);
        tv_left.setTextColor(mSelectColor);
        tv_middle.setBackgroundColor(mUnSelectColor);
        tv_middle.setTextColor(mSelectColor);
        tv_right.setBackground(mRightSelectDrawable);
        tv_right.setTextColor(mUnSelectColor);
    }

    public interface OnItemClickListener {
        void onLeftClick();

        void onMiddleClick();

        void onRightClick();
    }

    public static class OnItemClick implements OnItemClickListener {
        @Override
        public void onLeftClick() {

        }

        @Override
        public void onMiddleClick() {

        }

        @Override
        public void onRightClick() {

        }
    }
}
