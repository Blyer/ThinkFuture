package com.ysy.thinkfuture.core.fragment;

import android.os.Bundle;
import android.view.View;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.constants.UrlConstants;
import com.ysy.thinkfuture.core.fragment.base.FutureBaseFragment;
import com.ysy.thinkfuture.core.fragment.helper.FirstFragmentHelper;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.DbCacheUtils;
import org.base.platform.utils.ToastUtils;
import org.base.platform.view.UnifyButton;

public class FirstFragment extends FutureBaseFragment implements View.OnClickListener {

    private UnifyButton btn_1;
    private UnifyButton btn_2;
    private UnifyButton btn_3;
    private UnifyButton btn_4;
    private UnifyButton btn_5;

    private DbCacheUtils mDbCacheUtils;
    private FirstFragmentHelper mFragmentHelper;

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
    }

    @Override
    protected void initData() {
        mDbCacheUtils = new DbCacheUtils(mActivity, "db_cache", null, 1);
        mFragmentHelper = new FirstFragmentHelper(this);
    }

    @Override
    protected void setListener() {
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
    }

    @Override
    protected void begin() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                showLoadingDialog();
                mFragmentHelper.getCustomerDetail(generateCustomerDetailRequest());
                mFragmentHelper.getCustomerSource(generateCustomerSourceRequest());
                mFragmentHelper.getCustomerGrade(generateCustomerGradeCodeRequest());
                break;
            case R.id.btn_2:
                mFileCacheUtils.write("cache", "Hello world from filecache");
                break;
            case R.id.btn_3: {
                String cache = mFileCacheUtils.read("cache");
                ToastUtils.show(cache);
            }
            break;
            case R.id.btn_4:
                mDbCacheUtils.putString("cache", "Hello world from dbcache");
                break;
            case R.id.btn_5: {
                String cache = mDbCacheUtils.getString("cache", "default");
                ToastUtils.show(cache);
            }
            break;
        }
    }

    private HttpRequestPackage generateCustomerDetailRequest() {
        HttpRequestPackage request = new HttpRequestPackage();
        request.method = HttpMethod.GET;
        request.url = UrlConstants.host + "/test.txt";
        request.params.put("id", "111");
        return request;
    }

    private HttpRequestPackage generateCustomerSourceRequest() {
        HttpRequestPackage request = new HttpRequestPackage();
        request.method = HttpMethod.GET;
        request.url = UrlConstants.host + "/test.txt";
        request.params.put("id", "122");
        return request;
    }

    private HttpRequestPackage generateCustomerGradeCodeRequest() {
        HttpRequestPackage request = new HttpRequestPackage();
        request.method = HttpMethod.GET;
        request.url = UrlConstants.host + "/test.txt";
        request.params.put("id", "133");
        return request;
    }

    public void getCustomerDetailSuccess(String info) {
        ToastUtils.show(info);
    }

    public void getCustomerDetailFailed(String reason) {
        ToastUtils.show(reason);
    }

    public void getCustomerSourceSuccess(String info) {
        ToastUtils.show(info);
    }

    public void getCustomerGradeSuccess(String info) {
        ToastUtils.show(info);
    }
}
