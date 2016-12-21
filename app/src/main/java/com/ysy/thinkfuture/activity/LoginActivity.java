package com.ysy.thinkfuture.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.activity.base.FutureBaseActivity;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.ResponseResult;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.JumpUtils;
import org.base.platform.utils.ToastUtils;
import org.base.platform.utils.ViewUtils;
import org.base.platform.view.UnifyButton;

public class LoginActivity extends FutureBaseActivity implements ViewUtils.OnClickListener {

    private EditText et_user_name;
    private EditText et_password;
    private UnifyButton btn_login;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void resolveIntent(Intent intent) {
    }

    @Override
    protected void initView() {
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (UnifyButton) findViewById(R.id.btn_login);
    }

    @Override
    protected void setListener() {
        ViewUtils.setOnClickListener(btn_login, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getStatusBarColor() {
        return getResources().getColor(R.color.blue_1);
    }

    @Override
    public void processNetRequest(int id, ResponseResult result, boolean isCache) {
        super.processNetRequest(id, result, isCache);
        switch (id) {
            case R.id.btn_login:
                if (result.getCode() == 0) {
                    ToastUtils.show("登录成功");
                    loginSucess();
                } else {
                    ToastUtils.show(result.getMessage());
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                generateLoginRequest();
                mHttpUtils.request();
                break;
        }
    }

    private void generateLoginRequest() {
        HttpRequestPackage httpRequestPackage = new HttpRequestPackage();
        httpRequestPackage.id = R.id.btn_login;
        httpRequestPackage.url = "http://192.168.2.79/login.txt";
        httpRequestPackage.method = HttpMethod.GET;
        httpRequestPackage.params.put("username", et_user_name.getText().toString().trim());
        httpRequestPackage.params.put("password", et_password.getText().toString().trim());
        mHttpUtils.addRequest(httpRequestPackage);
    }

    private void loginSucess() {
        Intent intent = new Intent(mActivity, MainActivity.class);
        JumpUtils.jump(mActivity, intent);
    }
}
