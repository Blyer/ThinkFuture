package com.ysy.thinkfuture.activity;

import android.Manifest;
import android.content.Intent;
import android.view.View;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.activity.base.FutureBaseActivity;

import org.base.platform.bean.MessageEvent;
import org.base.platform.callback.PermissionsResultListener;
import org.base.platform.utils.ActivityCollector;
import org.base.platform.utils.BaseUtils;
import org.base.platform.utils.JumpUtils;
import org.base.platform.utils.StatusBarUtils;
import org.base.platform.utils.ToastUtils;
import org.base.platform.utils.photoselect.PhotoMultiSelectActivity;
import org.base.platform.view.UnifyButton;

public class MainActivity extends FutureBaseActivity implements View.OnClickListener {
    private UnifyButton btn_1;
    private UnifyButton btn_2;
    private UnifyButton btn_3;
    private UnifyButton btn_4;
    private UnifyButton btn_5;
    private UnifyButton btn_6;
    private UnifyButton btn_7;

    private long mFirstTime = 0; // 第一次点击返回键记录的时间

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        btn_1 = (UnifyButton) findViewById(R.id.btn_1);
        btn_2 = (UnifyButton) findViewById(R.id.btn_2);
        btn_3 = (UnifyButton) findViewById(R.id.btn_3);
        btn_4 = (UnifyButton) findViewById(R.id.btn_4);
        btn_5 = (UnifyButton) findViewById(R.id.btn_5);
        btn_6 = (UnifyButton) findViewById(R.id.btn_6);
        btn_7 = (UnifyButton) findViewById(R.id.btn_7);
    }

    @Override
    protected void setListener() {
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
    }

    @Override
    protected void begin() {

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

    @Override
    protected void initData() {
        StatusBarUtils.compat(this, getResources().getColor(org.base.platform.R.color.blue_1));
        forbidSwipeFinishActivity();
        setFinishAnim(0, 0);
    }

    @Override
    protected void processMessageEvent(MessageEvent event) {
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
                        BaseUtils.callPhone(mActivity, "10086");
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
            case R.id.btn_5: {
                Intent intent = new Intent(mActivity, SingleTypeListActivity.class);
                JumpUtils.jump(mActivity, intent);
            }
            break;
            case R.id.btn_6: {
                Intent intent = new Intent(mActivity, MultiTypeListActivity.class);
                JumpUtils.jump(mActivity, intent);
            }
            break;
            case R.id.btn_7: {
                Intent intent = new Intent(mActivity, TestListViewActivity.class);
                JumpUtils.jump(mActivity, intent);
            }
            break;
        }
    }
}
