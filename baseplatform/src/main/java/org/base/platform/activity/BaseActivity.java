package org.base.platform.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.jude.swipbackhelper.SwipeBackHelper;

import org.base.platform.bean.MessageEvent;
import org.base.platform.callback.NetRequestProcessCallback;
import org.base.platform.callback.PermissionsResultListener;
import org.base.platform.dialog.LoadingDialog;
import org.base.platform.enums.CacheType;
import org.base.platform.utils.ActivityCollector;
import org.base.platform.utils.FileCacheUtils;
import org.base.platform.utils.HttpUtils;
import org.base.platform.utils.MessageEventUtils;
import org.base.platform.utils.PermissionUtils;

/**
 * Created by YinShengyi on 2016/11/18.
 * 基础Activity，所有Activity必须继承此Activity
 */
public abstract class BaseActivity extends AppCompatActivity implements NetRequestProcessCallback {

    protected BaseActivity mActivity; // 标识自己
    private LoadingDialog mLoadingDialog; // 显示加载中弹框
    protected HttpUtils mHttpUtils; // 网络请求工具
    protected FileCacheUtils mFileCacheUtils; // 文件存储工具
    private MessageEventUtils mMessageEventUtils; // 总线消息工具
    private boolean mIsDestroyed = false; // 当前activity是否被销毁
    private boolean mInBackground = false; // 是否处于后台

    private PermissionsResultListener mPermissionListener;  // 权限申请之后的监听

    private int mFinishOldInAnimId = 0; // 结束activity时，老activity出现时的动画
    private int mFinishNewOutAnimId = 0; // 结束activity时，当前activity结束时的动画

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        mActivity = this;
        mHttpUtils = new HttpUtils(this);
        ActivityCollector.put(this);

        mMessageEventUtils = new MessageEventUtils(new MessageEventUtils.OnProcessMessageEvent() {
            @Override
            public void onProcessMessageEvent(MessageEvent event) {
                processMessageEvent(event);
            }
        });
        mMessageEventUtils.register();

        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeSensitivity(0.5f)
                .setSwipeRelateEnable(true)
                .setSwipeRelateOffset(300)
                .setSwipeEdgePercent(0.05f);

        initView();
        initData();
        setListener();
        begin();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInBackground) {
            backgroundToFront();
            mInBackground = false;
        }
    }

    @Override
    protected void onPause() {
        if (mFileCacheUtils != null) {
            mFileCacheUtils.flush();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        mInBackground = true;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        closeLoadingDialog();
        mHttpUtils.cancelAllRequests();
        if (mFileCacheUtils != null) {
            mFileCacheUtils.close();
        }
        ActivityCollector.remove(this);
        SwipeBackHelper.onDestroy(this);
        mMessageEventUtils.unregister();
        mIsDestroyed = true;
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.checkEachPermissionsGranted(grantResults)) {
            if (mPermissionListener != null) {
                mPermissionListener.onPermissionGranted();
            }
        } else {
            if (mPermissionListener != null) {
                mPermissionListener.onPermissionDenied();
            }
        }
    }

    @Override
    public void finish() {
        if (mFinishOldInAnimId == 0 || mFinishNewOutAnimId == 0) {
            super.finish();
        } else {
            super.finish();
            overridePendingTransition(mFinishOldInAnimId, mFinishNewOutAnimId);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * 获取本activity的布局文件
     */
    protected abstract int getContentViewId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 设置事件监听
     */
    protected abstract void setListener();

    /**
     * 开始执行操作指令
     */
    protected abstract void begin();

    /**
     * 总线消息处理
     */
    protected void processMessageEvent(MessageEvent event) {

    }

    /**
     * 当前activity由后台切入前台时执行此方法
     * 注意：此方法不可做耗时操作
     */
    protected void backgroundToFront() {
        for (BaseActivity activity : ActivityCollector.getAllActivity()) {
            activity.mInBackground = false;
        }
    }

    /**
     * 设置结束activity时的动画资源
     *
     * @param finishOldInAnimId  结束当前activity时，上一个activity重新resume时的动画ID。如果传0，则为系统默认动画
     * @param finishNewOutAnimId 结束当前activity时，当前activity结束时的动画资源ID。如果传0，则为系统默认动画
     */
    protected void setFinishAnim(int finishOldInAnimId, int finishNewOutAnimId) {
        mFinishOldInAnimId = finishOldInAnimId;
        mFinishNewOutAnimId = finishNewOutAnimId;
    }

    /**
     * @param desc        首次申请权限被拒绝后再次申请给用户的描述提示
     * @param permissions 要申请的权限数组
     * @param listener    实现的接口
     */
    protected void requestPermissions(String desc, String[] permissions, PermissionsResultListener listener) {
        if (permissions == null || permissions.length == 0) {
            return;
        }
        mPermissionListener = listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtils.checkEachSelfPermission(mActivity, permissions)) {// 检查是否声明了权限
                PermissionUtils.requestEachPermissions(mActivity, desc, permissions, 1);
            } else {// 已经申请权限
                if (mPermissionListener != null) {
                    mPermissionListener.onPermissionGranted();
                }
            }
        } else {
            if (mPermissionListener != null) {
                mPermissionListener.onPermissionGranted();
            }
        }
    }

    /**
     * 禁止activity右滑关闭
     */
    public void forbidSwipeFinishActivity() {
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mActivity, "");
        }
        if (!mIsDestroyed) {
            mLoadingDialog.show();
        }
    }

    @Override
    public void closeLoadingDialog() {
        if (!mIsDestroyed && mLoadingDialog != null) {
            mLoadingDialog.close();
        }
    }

    /**
     * 当前activity是否被销毁
     */
    public boolean isDestroyed() {
        return mIsDestroyed;
    }

    /**
     * 初始化文件缓存工具，在子类中使用前必须先调用此方法
     */
    protected void initFileCacheUtils() {
        mFileCacheUtils = new FileCacheUtils();
        mFileCacheUtils.open(CacheType.FILE);
    }

    /**
     * 获取文件缓存对象
     */
    public FileCacheUtils getFileCacheUtils() {
        return mFileCacheUtils;
    }

}
