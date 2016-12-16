package org.base.platform.utils.photoselect.inner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * Created by YinShengyi on 2015/12/25.
 * 提供给头像裁剪模块用的Drawable，作用是显示中间的矩形框
 */
public class FloatDrawable extends Drawable {

    private Paint mPaint;

    public FloatDrawable() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE); // 设置画笔颜色
        mPaint.setStyle(Paint.Style.STROKE); // 设置绘制的图形为空心的
        mPaint.setStrokeWidth(1.0f); // 设置画笔的粗细
        mPaint.setAntiAlias(true); // 画笔抗锯齿
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(getBounds(), mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
