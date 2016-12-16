package org.base.platform.utils.photoselect.inner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by YinShengyi on 2015/12/25.
 * 裁剪头像的控件
 */
public class ClipImageView extends View {

    private Context mContext;
    private Drawable mDrawable; // 图片
    private Rect mDrawableRect;
    private FloatDrawable mFloatDrawable; // 浮层
    private Rect mFloatDrawableRect;

    private boolean mFirst; // 是否第一次绘制
    private float p1_x, p1_y, p2_x, p2_y; // 触摸屏上手指的坐标

    private enum Mode {
        NONE,// 无触摸
        MOVE, // 平移
        ZOOM // 缩放
    }

    private Mode mCurrentMode = Mode.NONE;

    public ClipImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mDrawableRect = new Rect();
        mFloatDrawable = new FloatDrawable();
        mFloatDrawableRect = new Rect();
        mFirst = true;
        try {
            this.setLayerType(LAYER_TYPE_SOFTWARE, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawable == null) {
            return;
        }
        configFloatBounds();
        configDrawableBounds();
        checkBorder();
        mDrawable.draw(canvas);
        canvas.save();
        // 在画布上画浮层FloatDrawable,Region.Op.DIFFERENCE是表示Rect交集的补集
        canvas.clipRect(mFloatDrawableRect, Region.Op.DIFFERENCE);
        // 在交集的补集上画上灰色用来区分
        canvas.drawColor(Color.parseColor("#a0000000"));
        canvas.restore();
        // 画浮层
        mFloatDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentMode = Mode.MOVE;
                p1_x = event.getX(0);
                p1_y = event.getY(0);
                p2_x = 0;
                p2_y = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mCurrentMode = Mode.ZOOM;
                p1_x = event.getX(0);
                p1_y = event.getY(0);
                p2_x = event.getX(1);
                p2_y = event.getY(1);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentMode == Mode.MOVE) {
                    float t1_x = event.getX(0);
                    float t1_y = event.getY(0);
                    if (p1_x != 0) {
                        int o_x = (int) (t1_x - p1_x);
                        int o_y = (int) (t1_y - p1_y);
                        if (o_x < 0) {
                            // 左移
                            if (mDrawableRect.right + o_x < mFloatDrawableRect.right) {
                                o_x = mFloatDrawableRect.right - mDrawableRect.right;
                            }
                        } else if (o_x > 0) {
                            // 右移
                            if (mDrawableRect.left + o_x > mFloatDrawableRect.left) {
                                o_x = mFloatDrawableRect.left - mDrawableRect.left;
                            }
                        }
                        if (o_y < 0) {
                            // 上移
                            if (mDrawableRect.bottom + o_y < mFloatDrawableRect.bottom) {
                                o_y = mFloatDrawableRect.bottom - mDrawableRect.bottom;
                            }
                        } else if (o_y > 0) {
                            // 下移
                            if (mDrawableRect.top + o_y > mFloatDrawableRect.top) {
                                o_y = mFloatDrawableRect.top - mDrawableRect.top;
                            }
                        }
                        mDrawableRect.offset(o_x, o_y);
                        invalidate();
                    }
                    p1_x = t1_x;
                    p1_y = t1_y;
                } else if (mCurrentMode == Mode.ZOOM) {
                    float t1_x = event.getX(0);
                    float t1_y = event.getY(0);
                    float t2_x = event.getX(1);
                    float t2_y = event.getY(1);
                    double d1 = Math.sqrt((Math.pow(p1_x - p2_x, 2) + Math.pow(p1_y - p2_y, 2)));
                    double d2 = Math.sqrt((Math.pow(t1_x - t2_x, 2) + Math.pow(t1_y - t2_y, 2)));
                    double scale = d2 / d1;
                    int width = mDrawableRect.width();
                    int height = mDrawableRect.height();
                    if (scale < 1.0) {
                        // 缩小
                        Rect tmp = new Rect((int) (mDrawableRect.left + width * (1 - scale) / 2), (int) (mDrawableRect.top + height * (1 - scale) / 2), (int) (mDrawableRect.right - width * (1 - scale) / 2), (int) (mDrawableRect.bottom - height * (1 - scale) / 2));
                        double scaleX = 0, scaleY = 0;
                        if (tmp.width() < mFloatDrawableRect.width()) {
                            scaleX = mFloatDrawableRect.width() * 1.0 / width;
                        }
                        if (tmp.height() < mFloatDrawableRect.height()) {
                            scaleY = mFloatDrawableRect.height() * 1.0 / height;
                        }
                        if (scaleX != 0 || scaleY != 0)
                            scale = scaleX > scaleY ? scaleX : scaleY;
                        mDrawableRect.set((int) (mDrawableRect.left + width * (1 - scale) / 2), (int) (mDrawableRect.top + height * (1 - scale) / 2), (int) (mDrawableRect.right - width * (1 - scale) / 2), (int) (mDrawableRect.bottom - height * (1 - scale) / 2));
                    } else if (scale > 1.0) {
                        // 放大
                        Rect tmp = new Rect((int) (mDrawableRect.left - width * (scale - 1) / 2), (int) (mDrawableRect.top - height * (scale - 1) / 2), (int) (mDrawableRect.right + width * (scale - 1) / 2), (int) (mDrawableRect.bottom + height * (scale - 1) / 2));
                        if (tmp.width() * 1.0 / mContext.getResources().getDisplayMetrics().widthPixels > 2.0) {
                            scale = 1.0;
                        }
                        mDrawableRect.set((int) (mDrawableRect.left - width * (scale - 1) / 2), (int) (mDrawableRect.top - height * (scale - 1) / 2), (int) (mDrawableRect.right + width * (scale - 1) / 2), (int) (mDrawableRect.bottom + height * (scale - 1) / 2));
                    }
                    invalidate();
                    p1_x = t1_x;
                    p1_y = t1_y;
                    p2_x = t2_x;
                    p2_y = t2_y;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mCurrentMode = Mode.MOVE;
                p1_x = 0;
                p1_y = 0;
                p2_x = 0;
                p2_y = 0;
                break;
            case MotionEvent.ACTION_UP:
                mCurrentMode = Mode.NONE;
                p1_x = 0;
                p1_y = 0;
                p2_x = 0;
                p2_y = 0;
                break;
        }
        return true;
    }

    /**
     * 检查图片是否超出边界
     */
    private void checkBorder() {
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        int w = mDrawableRect.width() / 2;
        int h = mDrawableRect.height() / 2;
        if (mDrawableRect.left > mFloatDrawableRect.left) {
            mDrawableRect.set(mFloatDrawableRect.left, mDrawableRect.top, mDrawableRect.right - (mDrawableRect.left - mFloatDrawableRect.left), mDrawableRect.bottom);
        }
        if (mDrawableRect.top > mFloatDrawableRect.top) {
            mDrawableRect.set(mDrawableRect.left, mFloatDrawableRect.top, mDrawableRect.right, mDrawableRect.bottom - (mDrawableRect.top - mFloatDrawableRect.top));
        }
        if (mDrawableRect.right < mFloatDrawableRect.right) {
            mDrawableRect.set(mDrawableRect.left - (mDrawableRect.right - mFloatDrawableRect.right), mDrawableRect.top, mFloatDrawableRect.right, mDrawableRect.bottom);
        }
        if (mDrawableRect.bottom < mFloatDrawableRect.bottom) {
            mDrawableRect.set(mDrawableRect.left, mDrawableRect.top - (mDrawableRect.bottom - mFloatDrawableRect.bottom), mDrawableRect.right, mFloatDrawableRect.bottom);
        }
        mDrawable.setBounds(mDrawableRect);
    }

    /**
     * 设置浮层的大小
     */
    private void configFloatBounds() {
        int w = getWidth();
        int h = getHeight();
        int r = dp2px(mContext, 200);
        if (r > w) {
            // 裁剪浮层的正方形边长大于控件的宽
            r = w;
        }
        int left = (w - r) / 2;
        int top = (h - r) / 2;
        int right = left + r;
        int bottom = top + r;
        mFloatDrawableRect.set(left, top, right, bottom);
        mFloatDrawable.setBounds(mFloatDrawableRect);
    }

    /**
     * 设置图片的大小
     */
    private void configDrawableBounds() {
        if (mFirst) {
            int w = getWidth(); // 本控件的宽度
            int h = getHeight(); // 本控件的高度
            int iw = mDrawable.getIntrinsicWidth(); // Drawable的实际宽度
            int ih = mDrawable.getIntrinsicHeight(); // Drawable的实际高度
            int rw = 0; // 计算后得出的实际需求的宽
            int rh = 0; // 计算后得出的实际需求的高
            double ratio_w = w * 1.0 / iw;
            double ratio_h = h * 1.0 / ih;
            if (ratio_w < 1.0 && ratio_h < 1.0) {
                // 控件比图片的宽和高都要小
                rw = w;
                rh = h;
            } else {
                double tmp = ratio_w < ratio_h ? ratio_w : ratio_h;
                rw = (int) (iw * tmp);
                rh = (int) (ih * tmp);
            }
            if (rw < mFloatDrawableRect.width()) {
                rw = mFloatDrawableRect.width();
                rh = rw * ih / iw;
            }
            if (rh < mFloatDrawableRect.height()) {
                rh = mFloatDrawableRect.height();
                rw = rh * iw / ih;
            }
            int left = (w - rw) / 2;
            int top = (h - rh) / 2;
            int right = left + rw;
            int bottom = top + rh;
            mDrawableRect.set(left, top, right, bottom);
            mFirst = false;
        }
        mDrawable.setBounds(mDrawableRect);
    }

    /**
     * 设置图片源
     */
    public void setSrc(Bitmap src) {
        mDrawable = new BitmapDrawable(src);
        invalidate();
    }

    /**
     * 获取裁剪完成的图片路径
     */
    public String getClipImagePath() {
        Bitmap tmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tmp);
        mDrawable.draw(canvas);
        Bitmap res = Bitmap.createBitmap(tmp, mFloatDrawableRect.left, mFloatDrawableRect.top, mFloatDrawableRect.width(), mFloatDrawableRect.height());
        String path = saveBitmap(res);
        return path;
    }

    /**
     * 获取裁剪图片对应的bitmap
     */
    public Bitmap getClipImageBitmap() {
        Bitmap tmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tmp);
        mDrawable.draw(canvas);
        return Bitmap.createBitmap(tmp, mFloatDrawableRect.left, mFloatDrawableRect.top, mFloatDrawableRect.width(), mFloatDrawableRect.height());
    }

    /**
     * 保存Bitmap对象到本地
     *
     * @return 保存完后生成的路径
     */
    private String saveBitmap(Bitmap bmp) {
        File file = null;
        try {
            String cachePath = getCachePath(mContext);
            String name = String.valueOf(System.currentTimeMillis()) + ".png";
            file = new File(cachePath, name);
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            return cachePath + "/" + name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取缓存存储路径
     *
     * @return
     */
    private String getCachePath(Context mContext) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (null != mContext.getExternalCacheDir()) {
                return mContext.getExternalCacheDir().getAbsolutePath();
            } else {
                String dir = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + mContext.getApplicationContext().getPackageName()
                        + "/cache";
                File f = new File(dir);
                if (!f.exists()) {
                    f.mkdirs();
                }
                return dir;
            }
        } else {
            return mContext.getCacheDir().getAbsolutePath();
        }
    }

    /**
     * 将dp转换为px
     */
    public int dp2px(Context context, int value) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

}
