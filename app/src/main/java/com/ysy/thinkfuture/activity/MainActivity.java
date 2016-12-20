package com.ysy.thinkfuture.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.activity.base.FutureBaseActivity;

import org.base.platform.bean.MessageEvent;
import org.base.platform.bean.ResponseResult;
import org.base.platform.callback.PermissionsResultListener;
import org.base.platform.utils.ActivityCollector;
import org.base.platform.utils.JumpUtils;
import org.base.platform.utils.ToastUtils;
import org.base.platform.utils.ViewUtils;
import org.base.platform.utils.photoselect.PhotoMultiSelectActivity;
import org.base.platform.view.UnifyButton;

public class MainActivity extends FutureBaseActivity implements ViewUtils.OnClickListener {
    private UnifyButton btn_1;
    private UnifyButton btn_2;
    private UnifyButton btn_3;
    private UnifyButton btn_4;

    private long mFirstTime = 0; // 第一次点击返回键记录的时间

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void resolveIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        btn_1 = (UnifyButton) findViewById(R.id.btn_1);
        btn_2 = (UnifyButton) findViewById(R.id.btn_2);
        btn_3 = (UnifyButton) findViewById(R.id.btn_3);
        btn_4 = (UnifyButton) findViewById(R.id.btn_4);
    }

    @Override
    protected void setListener() {
        ViewUtils.setOnClickListener(btn_1, this);
        ViewUtils.setOnClickListener(btn_2, this);
        ViewUtils.setOnClickListener(btn_3, this);
        ViewUtils.setOnClickListener(btn_4, this);
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - mFirstTime > 3000) {
            // --两次back事件大于3秒，提示
            ToastUtils.show("再按一次返回键退出！");
            mFirstTime = secondTime;
        } else {
            // --两次back事件小于等于3秒，退出App
            ActivityCollector.finishAll();
        }
    }

    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10086");
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    protected void initData() {
        forbidSwipeFinishActivity();
        setFinishAnim(0, 0);
    }

    @Override
    protected void receivedMessage(MessageEvent event) {
        LogUtils.e(event.data.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK)
//                    ToastUtils.show(data.getStringArrayListExtra(PhotoMultiSelectActivity.SELECT_RESULT).get(0));
//                ToastUtils.show(data.getStringExtra(PhotoSingleSelectActivity.SELECT_RESULT));
                    break;
        }
    }

    @Override
    public int getStatusBarColor() {
        return getResources().getColor(R.color.blue_1);
    }

    @Override
    public void processNetRequest(int id, ResponseResult result, boolean isCache) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1: {
                Intent intent = new Intent(mActivity, SecondActivity.class);
                JumpUtils.jump(mActivity, intent);
            }
            break;
            case R.id.btn_2: {
                requestPermissions("获取图片需要这个权限", new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionsResultListener() {
                    @Override
                    public void onPermissionGranted() {
                        PhotoMultiSelectActivity.startForResult(mActivity, 1, 0, 9);
//                        PhotoSingleSelectActivity.startForResult(mActivity, 1, true);
                    }

                    @Override
                    public void onPermissionDenied() {
                        ToastUtils.show("您拒绝了读取图片的权限");
                    }
                });
            }
            break;
            case R.id.btn_3: {
                requestPermissions("没这个权限没法拨打电话哦~", new String[]{Manifest.permission.CALL_PHONE}, new PermissionsResultListener() {
                    @Override
                    public void onPermissionGranted() {
                        callPhone();
                    }

                    @Override
                    public void onPermissionDenied() {
                        ToastUtils.show("您拒绝了拨打电话的权限");
                    }
                });
            }
            break;
            case R.id.btn_4: {
                Intent intent = new Intent(mActivity, ThirdActivity.class);
                JumpUtils.jump(mActivity, intent);
            }
            break;
        }
    }
}
