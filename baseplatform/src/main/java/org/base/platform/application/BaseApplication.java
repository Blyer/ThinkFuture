package org.base.platform.application;

import android.app.Application;
import android.content.Context;

import com.apkfuns.logutils.LogUtils;

import org.xutils.x;

/**
 * Created by YinShengyi on 2016/11/25.
 */
public abstract class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        initXUtils();
        initLogUtils();
    }

    /**
     * 是否运行的正式包
     *
     * @return true为正式环境包，false为非正式环境
     */
    protected abstract boolean isRelease();

    private void initLogUtils() {
        LogUtils.getLogConfig().configAllowLog(!isRelease());
    }

    private void initXUtils() {
        x.Ext.init(this);
        x.Ext.setDebug(!isRelease()); // 是否输出debug日志, 开启debug会影响性能.
    }

    public static Context getContext() {
        return context;
    }
}
