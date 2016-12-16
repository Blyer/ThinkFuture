package com.ysy.thinkfuture.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.fragment.base.FutureBaseFragment;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.ResponseResult;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.DbCacheUtils;
import org.base.platform.utils.ToastUtils;
import org.base.platform.view.UnifyButton;

public class FirstFragment extends FutureBaseFragment {

    private UnifyButton btn_1;
    private UnifyButton btn_2;
    private UnifyButton btn_3;
    private UnifyButton btn_4;
    private UnifyButton btn_5;
    private UnifyButton btn_6;
    private UnifyButton btn_7;

    private DbCacheUtils mDbCacheUtils;

    public static Fragment get() {
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
    protected void resolveBundle(Bundle bundle) {
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
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFileCacheUtils.write("cache", "Hello world from filecache");
            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cache = mFileCacheUtils.read("cache");
                ToastUtils.show(cache);
            }
        });
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbCacheUtils.putString("cache", "Hello world from dbcache");
            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cache = mDbCacheUtils.getString("cache", "default");
                ToastUtils.show(cache);
            }
        });
    }

    @Override
    protected void initData() {
        mDbCacheUtils = new DbCacheUtils(mActivity, "db_cache", null, 1);
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
