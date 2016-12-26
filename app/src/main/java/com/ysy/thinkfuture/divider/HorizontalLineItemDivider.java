package com.ysy.thinkfuture.divider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by YinShengyi on 2016/12/24.
 */
public class HorizontalLineItemDivider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mHeight = 1;

    public HorizontalLineItemDivider() {
        mDivider = new ColorDrawable(Color.parseColor("#d7d7d7"));
        mHeight = 1;
    }

    /**
     * @param resId  分割线的资源ID，可以是一个颜色资源，也可以是drawabled的ID
     * @param height 分割线的高度，单位为像素
     */
    public HorizontalLineItemDivider(Context context, int resId, int height) {
        mDivider = context.getResources().getDrawable(resId);
        mHeight = height;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childNum = parent.getChildCount();
        if (childNum == 0) {
            return;
        }
        for (int i = 0; i <= childNum; ++i) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            int top = 0;
            int bottom = 0;
            int pos = -1;
            if (i == 0) {
                pos = 0;
            } else {
                pos = i - 1;
            }
            View childView = parent.getChildAt(pos);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            if (i == 0) {
                top = childView.getTop() - mHeight;
            } else if (i != childNum) {
                left += childView.getPaddingLeft();
                top = childView.getBottom() + layoutParams.bottomMargin;
            } else {
                top = childView.getBottom() + layoutParams.bottomMargin;
            }
            bottom = top + mHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int halfHeight = mHeight / 2;
        if (position == 0) {
            outRect.set(0, mHeight, 0, halfHeight);
        } else if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, halfHeight, 0, mHeight);
        } else {
            outRect.set(0, halfHeight, 0, halfHeight);
        }
    }
}
