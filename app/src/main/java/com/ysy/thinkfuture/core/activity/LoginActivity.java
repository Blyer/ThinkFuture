package com.ysy.thinkfuture.core.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.core.activity.base.FutureBaseActivity;
import com.ysy.thinkfuture.constants.UrlConstants;
import com.ysy.thinkfuture.core.activity.helper.LoginActivityHelper;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.JumpUtils;
import org.base.platform.utils.StatusBarUtils;
import org.base.platform.utils.ToastUtils;
import org.base.platform.view.UnifyButton;

public class LoginActivity extends FutureBaseActivity implements View.OnClickListener {

    private EditText et_user_name;
    private EditText et_password;
    private UnifyButton btn_login;

    private LoginActivityHelper mActivityHelper;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (UnifyButton) findViewById(R.id.btn_login);
    }

    @Override
    protected void setListener() {
        btn_login.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        StatusBarUtils.compat(this, getResources().getColor(org.base.platform.R.color.blue_1));
        forbidSwipeFinishActivity();
        mActivityHelper = new LoginActivityHelper(this);
    }

    @Override
    protected void begin() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                showLoadingDialog();
                mActivityHelper.login(generateLoginRequest());
                break;
        }
    }

    private HttpRequestPackage generateLoginRequest() {
        HttpRequestPackage httpRequestPackage = new HttpRequestPackage();
        httpRequestPackage.url = UrlConstants.host + "/login.txt";
        httpRequestPackage.method = HttpMethod.GET;
        httpRequestPackage.params.put("username", et_user_name.getText().toString().trim());
        httpRequestPackage.params.put("password", et_password.getText().toString().trim());
        return httpRequestPackage;
    }

    public void loginSucess() {
        closeLoadingDialog();
        Intent intent = new Intent(mActivity, MainActivity.class);
        JumpUtils.jump(mActivity, intent);
        finish();
    }

    public void loginFailed(String reason) {
        closeLoadingDialog();
        ToastUtils.show(reason);
    }
}
