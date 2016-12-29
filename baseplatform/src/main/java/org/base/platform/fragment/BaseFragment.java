package org.base.platform.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.base.platform.activity.BaseActivity;
import org.base.platform.bean.MessageEvent;
import org.base.platform.callback.NetRequestProcessCallback;
import org.base.platform.callback.PermissionsResultListener;
import org.base.platform.utils.FileCacheUtils;
import org.base.platform.utils.HttpUtils;
import org.base.platform.utils.MessageEventUtils;
import org.base.platform.utils.PermissionUtils;

/**
 * Created by YinShengyi on 2016/12/9.
 * 基础Fragment，所有Fragment必须继承此Fragment
 */
public abstract class BaseFragment extends Fragment implements NetRequestProcessCallback {

    protected BaseActivity mActivity; // 本Fragment依附的Activity
    protected View mFragmentView; // 本Fragment对应的View
    protected HttpUtils mHttpUtils; // 网络请求工具
    protected FileCacheUtils mFileCacheUtils; // 文件存储工具
    private MessageEventUtils mMessageEventUtils; // 总线消息工具

    private PermissionsResultListener mPermissionListener;  // 权限申请之后的监听

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(getViewId(), null);
        return mFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = (BaseActivity) getActivity();
        mHttpUtils = new HttpUtils(this);

        mMessageEventUtils = new MessageEventUtils(new MessageEventUtils.OnProcessMessageEvent() {
            @Override
            public void onProcessMessageEvent(MessageEvent event) {
                processMessageEvent(event);
            }
        });
        mMessageEventUtils.register();

        mFileCacheUtils = mActivity.getFileCacheUtils();
        if (mFileCacheUtils == null) {
            throw new RuntimeException("请在Fragment所属Activity中初始化文件工具类");
        }

        initView();
        initData();
        setListener();
        begin();
    }

    @Override
    public void onDestroy() {
        mHttpUtils.cancelAllRequests();
        mMessageEventUtils.unregister();
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

    /**
     * 显示加载中对话框
     */
    @Override
    public void showLoadingDialog() {
        mActivity.showLoadingDialog();
    }

    /**
     * 关闭加载中对话框
     */
    @Override
    public void closeLoadingDialog() {
        mActivity.closeLoadingDialog();
    }

    /**
     * 返回本Fragment所关联的布局文件ID
     */
    protected abstract int getViewId();

    /**
     * 初始化控件，直接使用findViewById方法（已重写）即可
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
     * 根据控件ID获取控件对应的View对象
     */
    protected View findViewById(int id) {
        return mFragmentView.findViewById(id);
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
                PermissionUtils.requestEachPermissions(mActivity, desc, permissions, 2);
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

}
