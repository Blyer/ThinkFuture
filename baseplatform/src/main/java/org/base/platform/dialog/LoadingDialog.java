package org.base.platform.dialog;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.base.platform.R;
import org.base.platform.activity.BaseActivity;
import org.base.platform.callback.IDialog;
import org.base.platform.utils.StringUtils;

/**
 * Created by YinShengyi on 2016/11/29.
 */
public class LoadingDialog implements IDialog {
    private BaseActivity mActivity;
    private Dialog mLoadingDialog;

    public LoadingDialog(BaseActivity activity, String message) {
        mActivity = activity;
        createLoadingDialog(message);
    }

    @Override
    public void show() {
        if (!mActivity.isDestroyed() && mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            try {
                mLoadingDialog.show();
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void close() {
        if (!mActivity.isDestroyed() && mLoadingDialog != null && mLoadingDialog.isShowing()) {
            try {
                mLoadingDialog.dismiss();
            } catch (Exception e) {

            }
        }
    }

    private void createLoadingDialog(String message) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.dialog_loading, null);
        LinearLayout ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);// 加载布局
        ImageView img_loading = (ImageView) view.findViewById(R.id.img_loading);
        TextView tv_loading_text = (TextView) view.findViewById(R.id.tv_loading_text);// 提示文字

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(img_loading, "rotation", 360);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.start();

        if (StringUtils.isNull(message)) {// 设置加载信息 没有则隐藏
            tv_loading_text.setVisibility(View.GONE);
        } else {
            tv_loading_text.setVisibility(View.VISIBLE);
            tv_loading_text.setText(message);
        }

        mLoadingDialog = new Dialog(mActivity, R.style.LoadingDialog);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.addContentView(ll_loading, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mLoadingDialog.setContentView(ll_loading);
    }
}
