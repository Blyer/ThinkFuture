package org.base.platform.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by YinShengyi on 2016/11/29.
 */
public class ImageUtils {
    private Context mContext;
    private ImageOptions imageOptions;

    private int mWidth = 300; // 加载图片时的宽
    private int mHeight = 300; // 加载图片时的高

    private int mLoadingRes = 0; // 加载过程中显示的图片资源ID
    private int mLoadingFailedRes = 0; // 加载失败时显示的图片资源ID

    public ImageUtils(Context context) {
        mContext = context;
        imageOptions = new ImageOptions.Builder()
                .setAutoRotate(true)
                .build();
    }

    /**
     * 配置加载中时展示的图片
     *
     * @param resId 图片资源ID
     */
    public ImageUtils configLoadingImage(int resId) {
        mLoadingRes = resId;
        buildImageOptions();
        return this;
    }

    /**
     * 配置加载失败时展示的图片
     *
     * @param resId 图片资源ID
     */
    public ImageUtils configFailedImage(int resId) {
        mLoadingFailedRes = resId;
        buildImageOptions();
        return this;
    }

    /**
     * 配置图片显示时的宽和高
     *
     * @param width  宽
     * @param height 高
     */
    public ImageUtils configImageSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        buildImageOptions();
        return this;
    }

    private void buildImageOptions() {
        imageOptions = new ImageOptions.Builder()
                .setSize(mWidth, mHeight)
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
//                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(mLoadingRes)
                .setFailureDrawableId(mLoadingFailedRes)
                .setAutoRotate(true)
                .build();
    }

    /**
     * 显示图片到指定的ImageView上
     *
     * @param url 图片的地址，可以是网络url或者本地path
     * @param iv  待加载的ImageView
     */
    public void displayImage(String url, ImageView iv) {
        url = getPicPath(url);
        x.image().bind(iv, url, imageOptions);
    }

    public void displayImage(String url, ImageView iv, Callback.CommonCallback<Drawable> callback) {
        url = getPicPath(url);
        x.image().bind(iv, url, imageOptions, callback);
    }

    /**
     * 将图片地址转换为ImageLoader可接受的格式
     *
     * @param url 原始图片地址
     * @return 转换后的地址
     */
    private String getPicPath(String url) {
        if (url == null || "".equals(url.trim())) {
            return "";
        }
        if (url.startsWith("/")) {
            return "file://" + url;
        }
        return url;
    }

}
