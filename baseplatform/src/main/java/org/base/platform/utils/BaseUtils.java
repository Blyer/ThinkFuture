package org.base.platform.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.apkfuns.logutils.LogUtils;

import org.base.platform.application.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by YinShengyi on 2016/11/25.
 */
public class BaseUtils {
    /**
     * 获取屏幕宽的像素数
     */
    public static int getScreenWidth() {
        return BaseApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高的像素数
     */
    public static int getScreenHeight() {
        return BaseApplication.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 将px值转换为p值，保证尺寸大小不变
     */
    public static int px2dp(float pxValue) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dp2px(float dipValue) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(float pxValue) {
        final float fontScale = BaseApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(float spValue) {
        final float fontScale = BaseApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取缓存路径
     */
    public static String getCachePath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if (BaseApplication.getContext().getExternalCacheDir() != null) {
                return BaseApplication.getContext().getExternalCacheDir().getAbsolutePath();
            } else {
                String dir = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + BaseApplication.getContext().getApplicationContext().getPackageName()
                        + "/cache";
                File f = new File(dir);
                if (!f.exists()) {
                    f.mkdirs();
                }
                return dir;
            }
        } else {
            return BaseApplication.getContext().getCacheDir().getAbsolutePath();
        }
    }

    /**
     * 获取APP的版本值
     */
    public static int getAppVersionCode() {
        try {
            PackageInfo info = BaseApplication.getContext().getPackageManager().getPackageInfo(BaseApplication.getContext().getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取APP的版本名
     */
    public static String getAppVersionName() {
        try {
            PackageInfo info = BaseApplication.getContext().getPackageManager().getPackageInfo(BaseApplication.getContext().getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * List深拷贝
     * 注意：如果拷贝单个对象，则该对象必须实现Serialable接口，否则序列化失败
     * 同理，如果拷贝List集合，则集合中的对象必须实现了Serialable接口，否则序列化失败
     */
    public static Object deepCopy(Object src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return in.readObject();
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return null;
    }

    /**
     * 拨打电话
     */
    public static void callPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        context.startActivity(intent);
    }
}
