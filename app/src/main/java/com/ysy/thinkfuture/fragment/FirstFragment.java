package com.ysy.thinkfuture.fragment;

import android.os.Bundle;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.constants.UrlConstants;
import com.ysy.thinkfuture.fragment.base.FutureBaseFragment;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.MessageEvent;
import org.base.platform.bean.ResponseResult;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.DbCacheUtils;
import org.base.platform.utils.MessageEventUtils;
import org.base.platform.utils.ToastUtils;
import org.base.platform.view.UnifyButton;

public class FirstFragment extends FutureBaseFragment implements View.OnClickListener {

    private UnifyButton btn_1;
    private UnifyButton btn_2;
    private UnifyButton btn_3;
    private UnifyButton btn_4;
    private UnifyButton btn_5;
    private UnifyButton btn_6;
    private UnifyButton btn_7;

    private DbCacheUtils mDbCacheUtils;

    public static FutureBaseFragment get() {
        FirstFragment fragment = new FirstFragment();
        Bundle bundle = new Bundle();
        bundle.putString("he", "Hello world");
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_first;
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
    protected void initData() {
        mDbCacheUtils = new DbCacheUtils(mActivity, "db_cache", null, 1);
        MessageEvent event = new MessageEvent();
        event.data = "Get event bus";
        MessageEventUtils.post(event);
    }

    @Override
    protected void begin() {

    }

    public void processNetRequest(int id, ResponseResult result, boolean isCache) {
        switch (id) {
            case 111:
                if (result.getCode() == 0) {
                    ToastUtils.show(result.getData());
                }
                break;
            case 133:
                if (result.getCode() == 0 && !isCache) {
                    net1();
                }
                break;
        }
    }

    @Override
    protected void processMessageEvent(MessageEvent event) {
        super.processMessageEvent(event);
        LogUtils.e(event.data);
    }

    private HttpRequestPackage net1() {
        HttpRequestPackage request = new HttpRequestPackage();
        request.method = HttpMethod.GET;
        request.url = UrlConstants.host + "/r.txt";
        request.params.put("id", "111");
        return request;
    }

    private void net2() {
        HttpRequestPackage request = new HttpRequestPackage();
        request.method = HttpMethod.GET;
        request.url = UrlConstants.host + "/r.txt";
        request.params.put("id", "122");
    }

    private void net3() {
        HttpRequestPackage request = new HttpRequestPackage();
        request.method = HttpMethod.GET;
        request.url = UrlConstants.host + "/r.txt";
        request.params.put("id", "133");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                break;
            case R.id.btn_2:
                net1();
                net1();
                net1();
                net2();
                net2();
                break;
            case R.id.btn_3:
                net3();
                break;
            case R.id.btn_4:
                mFileCacheUtils.write("cache", "Hello world from filecache");
                break;
            case R.id.btn_5: {
                String cache = mFileCacheUtils.read("cache");
                ToastUtils.show(cache);
            }
            break;
            case R.id.btn_6:
                mDbCacheUtils.putString("cache", "Hello world from dbcache");
                break;
            case R.id.btn_7: {
                String cache = mDbCacheUtils.getString("cache", "default");
                ToastUtils.show(cache);
            }
            break;
        }
    }
}
