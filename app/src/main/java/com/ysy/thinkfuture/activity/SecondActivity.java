package com.ysy.thinkfuture.activity;

import android.content.Intent;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.activity.base.FutureBaseActivity;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.MessageEvent;
import org.base.platform.bean.ResponseResult;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.JumpUtils;
import org.base.platform.utils.ToastUtils;
import org.base.platform.utils.ViewUtils;
import org.base.platform.view.UnifyButton;

public class SecondActivity extends FutureBaseActivity implements ViewUtils.OnClickListener {

    private UnifyButton btn_1;
    private UnifyButton btn_2;
    private UnifyButton btn_3;
    private UnifyButton btn_4;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_second;
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
    protected void initData() {

    }

    @Override
    protected void receivedMessage(MessageEvent event) {
        LogUtils.e(event.data.toString());
    }

    @Override
    public void processNetRequest(int id, ResponseResult result, boolean isCache) {
        super.processNetRequest(id, result, isCache);
        switch (id) {
            case 111:
                if (result.getCode() == 0) {
                    ToastUtils.show(result.getData());
                }
                break;
            case 133:
                if (!isCache) {
                    net1();
                    mHttpUtils.request();
                }
                break;
        }
    }

    @Override
    public int getStatusBarColor() {
        return getResources().getColor(R.color.blue_1);
    }

    private HttpRequestPackage net1() {
        HttpRequestPackage request = new HttpRequestPackage();
        request.id = 111;
        request.isSilentRequest = false;
        request.method = HttpMethod.GET;
        request.url = "http://192.168.2.79/r.txt";
        request.params.put("id", "111");
        mHttpUtils.addRequest(request);
        return request;
    }

    private void net2() {
        HttpRequestPackage request = new HttpRequestPackage();
        request.id = 122;
        request.isSilentRequest = false;
        request.method = HttpMethod.GET;
        request.url = "http://192.168.2.79/r.txt";
        request.params.put("id", "122");
        mHttpUtils.addRequest(request);
    }

    private void net3() {
        HttpRequestPackage request = new HttpRequestPackage();
        request.id = 133;
        request.isSilentRequest = false;
        request.method = HttpMethod.GET;
        request.url = "http://192.168.2.79/r.txt";
        request.params.put("id", "133");
        mHttpUtils.addRequest(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                net1().isSilentRequest = true;
                mHttpUtils.request();
                break;
            case R.id.btn_2:
                net1();
                net1();
                net1();
                net2();
                net2();
                mHttpUtils.request();
                break;
            case R.id.btn_3:
                net3();
                mHttpUtils.request();
                break;
            case R.id.btn_4:
                Intent intent = new Intent(mActivity, ThirdActivity.class);
                JumpUtils.jump(mActivity, intent);
                break;
        }
    }
}
