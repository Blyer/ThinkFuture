package org.base.platform.utils.photoselect;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.base.platform.R;
import org.base.platform.activity.BaseActivity;
import org.base.platform.utils.BaseUtils;
import org.base.platform.utils.BitmapUtils;
import org.base.platform.utils.JumpUtils;
import org.base.platform.utils.photoselect.inner.ClipImageView;

/**
 * 图像裁剪Activity
 */
public class ClipImageActivity extends BaseActivity implements View.OnClickListener {

    public static String CLIP_RESULT = "clip_result";
    public static String CLIP_SOURCE = "clip_source";

    private ClipImageView civ_photo;
    private TextView btn_cancel;
    private TextView btn_ok;

    private String path;

    public static void startForResult(Activity activity, String imgPath, int requestCode) {
        Intent intent = new Intent(activity, ClipImageActivity.class);
        intent.putExtra(CLIP_SOURCE, imgPath);
        JumpUtils.jumpForResult(activity, intent, requestCode);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_clip_image;
    }

    @Override
    protected void initView() {
        civ_photo = (ClipImageView) findViewById(R.id.civ_photo);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_ok = (TextView) findViewById(R.id.btn_ok);
    }

    @Override
    protected void setListener() {
        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        path = getIntent().getStringExtra(CLIP_SOURCE);
        forbidSwipeFinishActivity();
        if (path == null || "".equals(path.trim())) {
            finish();
        }
        civ_photo.setSrc(BitmapUtils.getBitmap(path, BaseUtils.getScreenWidth(), BaseUtils.getScreenHeight()));
    }

    @Override
    protected void begin() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_cancel) {
            setResult(RESULT_CANCELED, null);
            finish();
        } else if (v.getId() == R.id.btn_ok) {
            Intent intent = new Intent();
            intent.putExtra(CLIP_RESULT, civ_photo.getClipImagePath());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
