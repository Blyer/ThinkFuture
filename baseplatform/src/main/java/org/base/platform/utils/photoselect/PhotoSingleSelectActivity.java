package org.base.platform.utils.photoselect;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.base.platform.Constants.MsgEventConstants;
import org.base.platform.R;
import org.base.platform.activity.BaseActivity;
import org.base.platform.bean.MessageEvent;
import org.base.platform.bean.ResponseResult;
import org.base.platform.utils.BaseUtils;
import org.base.platform.utils.ImageUtils;
import org.base.platform.utils.JumpUtils;
import org.base.platform.utils.ToastUtils;
import org.base.platform.utils.photoselect.inner.AlbumBean;
import org.base.platform.utils.photoselect.inner.AlbumData;
import org.base.platform.utils.photoselect.inner.AlbumListWindow;
import org.base.platform.utils.photoselect.inner.PhotoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片单选功能
 */
public class PhotoSingleSelectActivity extends BaseActivity implements View.OnClickListener {

    public static final String SELECT_RESULT = "select_result";
    private static final int CLIP_CODE = 100;
    private static final String IS_CLIP = "is_clip";

    private ImageView img_back;
    private TextView tv_title;
    private GridView gv_photo;
    private View view_bg;

    private AlbumBean mCurrentAlbum; // 当前选择的相册
    private List<PhotoBean> mData; // 相册里图片Bean的集合
    private MyAdapter mAdapter;
    private boolean mIsClip = false; // 是否需要裁剪

    /**
     * @param activity
     * @param requestCode 请求码
     * @param isClip      是否需要裁剪
     */
    public static void startForResult(Activity activity, int requestCode, boolean isClip) {
        List<AlbumBean> albumList = AlbumData.getAlbumList(activity);
        if (albumList == null || albumList.size() == 0) {
            ToastUtils.show("您的手机还没有图片哦，快去拍照获取吧～");
            return;
        } else if (albumList.get(0).getPhotoList().size() == 0) {
            ToastUtils.show("您的手机还没有图片哦，快去拍照获取吧～");
            return;
        }
        Intent intent = new Intent(activity, PhotoSingleSelectActivity.class);
        intent.putExtra(IS_CLIP, isClip);
        JumpUtils.jumpForResult(activity, intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_photo_single_select;
    }

    @Override
    protected void resolveIntent(Intent intent) {
        mIsClip = intent.getBooleanExtra(IS_CLIP, false);
    }

    @Override
    protected void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        gv_photo = (GridView) findViewById(R.id.gv_photo);
        view_bg = findViewById(R.id.view_bg);
    }

    @Override
    protected void setListener() {
        img_back.setOnClickListener(this);
        tv_title.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mCurrentAlbum = AlbumData.getAlbumList(mActivity).get(0); // 默认显示全部图片
        tv_title.setText(mCurrentAlbum.getName());
        mData = new ArrayList<>();
        mData.addAll(mCurrentAlbum.getPhotoList());
        mAdapter = new MyAdapter();
        gv_photo.setAdapter(mAdapter);
    }

    @Override
    public void processNetRequest(int id, ResponseResult result, boolean isCache) {

    }

    @Override
    protected void processMessageEvent(MessageEvent event) {
        super.processMessageEvent(event);
        switch (event.id) {
            case MsgEventConstants.CHANGE_ALBUM:
                mCurrentAlbum = AlbumData.getCurrentSelectedAlbum();
                tv_title.setText(mCurrentAlbum.getName());
                mData.clear();
                mData.addAll(mCurrentAlbum.getPhotoList());
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CLIP_CODE && data != null) {
                Intent intent = new Intent();
                intent.putExtra(SELECT_RESULT, data.getStringExtra(ClipImageActivity.CLIP_RESULT));
                setResult(RESULT_OK, intent);
                finish();
            }
        } else {
            back();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        Intent intent = new Intent();
        intent.putExtra(SELECT_RESULT, "");
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back) {
            back();
        } else if (v.getId() == R.id.tv_title) {
            AlbumListWindow mWindow = new AlbumListWindow(mActivity);
            mWindow.show(tv_title);
            tv_title.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_arrow_up), null);
            view_bg.setVisibility(View.VISIBLE);
            mWindow.getWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    view_bg.setVisibility(View.GONE);
                    tv_title.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_arrow_down), null);
                }
            });
        }
    }

    private class MyAdapter extends BaseAdapter {

        ImageUtils imageUtil = new ImageUtils();

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.album_photo_item, parent, false);
                holder = new ViewHolder();
                holder.img_photo = (ImageView) convertView.findViewById(R.id.img_photo);
                holder.view_bg = convertView.findViewById(R.id.view_bg);
                holder.relative_status = (RelativeLayout) convertView.findViewById(R.id.relative_status);

                int width = BaseUtils.getScreenWidth() - BaseUtils.dp2px(12);
                int height = width / 3;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height, height);
                holder.img_photo.setLayoutParams(params);
                holder.view_bg.setLayoutParams(params);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final PhotoBean bean = mData.get(position);
            holder.img_photo.setImageBitmap(null);
            imageUtil.displayImage(bean.getPath(), holder.img_photo);

            holder.relative_status.setVisibility(View.GONE);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mIsClip) {
                        Intent intent = new Intent();
                        intent.putExtra(SELECT_RESULT, bean.getPath());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ClipImageActivity.startForResult(mActivity, bean.getPath(), CLIP_CODE);
                    }
                }
            });

            return convertView;
        }

        class ViewHolder {
            ImageView img_photo;
            View view_bg;
            RelativeLayout relative_status;
        }

    }
}
