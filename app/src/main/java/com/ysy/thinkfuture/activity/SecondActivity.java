package com.ysy.thinkfuture.activity;

import android.content.Intent;
import android.view.View;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.activity.base.FutureBaseActivity;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.ResponseResult;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.ToastUtils;
import org.base.platform.view.UnifyButton;

public class SecondActivity extends FutureBaseActivity {

    private UnifyButton btn_1;
    private UnifyButton btn_2;
    private UnifyButton btn_3;

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
    }

    @Override
    protected void setListener() {
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                net1().isSilentRequest = true;
                mHttpUtils.request();
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                net1();
                net1();
                net1();
                net2();
                net2();
                mHttpUtils.request();
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                net3();
                mHttpUtils.request();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void processNetRequest(int id, ResponseResult result, boolean isCache) {
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

}
