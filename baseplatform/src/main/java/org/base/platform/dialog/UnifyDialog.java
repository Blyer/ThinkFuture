package org.base.platform.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.base.platform.R;
import org.base.platform.utils.StringUtils;
import org.base.platform.view.AlignTextView;

/**
 * Created by YinShengyi on 2016/5/20.
 * 提示对话框
 */
public class UnifyDialog {

    private Activity mActivity;
    private Dialog mDialog;
    private String mTitle; // 对话框标题
    private String mContent; // 对话框内容
    private String mLeftBtnTxt; // 左侧按钮的文字
    private String mRightBtnTxt; // 右侧按钮的文字
    private boolean mIsCancelable = false; // 按返回键是否可用，默认不可用
    private boolean mIsCancelableOutside = false; // 外部触摸是否可用，默认不可用
    private boolean mHasLeftBtn = true; // 是否有左侧按钮，默认有
    private boolean mHasTitle = true; // 是否含有标题栏，默认有

    private OnRightBtnClickListener mRightBtnClickListener;
    private OnLeftBtnClickListener mLeftBtnClickListener;

    /**
     * 对话框左边按钮默认为取消，右边默认为确定
     *
     * @param activity
     * @param title    对话框的标题
     * @param content  对话框中的内容
     */
    public UnifyDialog(Activity activity, String title, String content) {
        this(activity, title, content, "取消", "确定");
    }

    /**
     * 只有一个按钮的对话框
     *
     * @param activity
     * @param title    对话框的标题
     * @param content  对话框中的内容
     * @param btnTxt   按钮的内容
     */
    public UnifyDialog(Activity activity, String title, String content, String btnTxt) {
        this(activity, title, content, "", btnTxt);
        mHasLeftBtn = false;
    }

    /**
     * @param activity    传入Context作为参数
     * @param title       对话框的标题
     * @param content     对话框中的内容
     * @param leftBtnTxt  左边按钮的文字
     * @param rightBtnTxt 右边按钮的文字
     */
    public UnifyDialog(Activity activity, String title, String content, String leftBtnTxt, String rightBtnTxt) {
        mActivity = activity;
        mTitle = title;
        mContent = content;
        mLeftBtnTxt = leftBtnTxt;
        mRightBtnTxt = rightBtnTxt;

        if (StringUtils.isNull(title)) {
            mHasTitle = false;
        }
    }

    /**
     * 隐藏对话框标题
     */
    public UnifyDialog hideTitle() {
        mHasTitle = false;
        return this;
    }

    /**
     * 设置返回键是否可用
     *
     * @param isCancelable true，可用；false，不可用
     */
    public UnifyDialog setCancelable(boolean isCancelable) {
        mIsCancelable = isCancelable;
        return this;
    }

    /**
     * 设置是否可以外部触摸销毁对话框
     */
    public UnifyDialog setCancelableOutside(boolean isCancelableOutside) {
        mIsCancelableOutside = isCancelableOutside;
        return this;
    }

    /**
     * 显示对话框
     */
    public void show() {
        if (mActivity != null) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.unify_dialog, null);// 得到加载view

            LinearLayout linear_title = (LinearLayout) view.findViewById(R.id.linear_title);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            RelativeLayout relative_content = (RelativeLayout) view.findViewById(R.id.relative_content);
            AlignTextView tv_content = (AlignTextView) view.findViewById(R.id.tv_content);
            TextView tv_left = (TextView) view.findViewById(R.id.tv_left);
            View view_divider_line = view.findViewById(R.id.view_divider_line);
            TextView tv_right = (TextView) view.findViewById(R.id.tv_right);

            if (!mHasTitle) {
                linear_title.setVisibility(View.GONE);
                relative_content.setGravity(Gravity.CENTER);
            }

            tv_title.setText(mTitle);
            tv_content.setText(mContent);

            tv_right.setText(mRightBtnTxt);
            tv_right.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                    if (mRightBtnClickListener != null) {
                        mRightBtnClickListener.onRightBtnClick();
                    }
                }
            });

            if (!mHasLeftBtn) {
                tv_left.setVisibility(View.GONE);
                view_divider_line.setVisibility(View.GONE);
            }
            tv_left.setText(mLeftBtnTxt);
            tv_left.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                    if (mLeftBtnClickListener != null) {
                        mLeftBtnClickListener.onLeftBtnClick();
                    }
                }
            });

            mDialog = createDialogByView(view);
            mDialog.setCancelable(mIsCancelable);
            mDialog.setCanceledOnTouchOutside(mIsCancelableOutside);
            mDialog.show();
        }
    }

    /**
     * 隐藏对话框
     */
    private void hide() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private Dialog createDialogByView(View view) {
        Dialog dialog = new Dialog(mActivity, R.style.UnifyDialog);// 创建自定义样式dialog
        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        return dialog;
    }

    public void setOnRightBtnClickListener(OnRightBtnClickListener rightBtnClickListener) {
        mRightBtnClickListener = rightBtnClickListener;
    }

    public void setOnLeftBtnClickListener(OnLeftBtnClickListener leftBtnClickListener) {
        mLeftBtnClickListener = leftBtnClickListener;
    }

    public interface OnRightBtnClickListener {
        void onRightBtnClick();
    }

    public interface OnLeftBtnClickListener {
        void onLeftBtnClick();
    }
}
