package org.base.platform.utils.photoselect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.base.platform.R;
import org.base.platform.activity.BaseActivity;
import org.base.platform.bean.MessageEvent;
import org.base.platform.constants.MsgEventConstants;
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
 * 图片多选功能
 */
public class PhotoMultiSelectActivity extends BaseActivity implements View.OnClickListener {
    public static final String SELECT_RESULT = "select_result";
    private static final String CURRENT_SELECT_NUM = "currentSelectNum";
    private static final String TOTAL_NUM = "totalNum";

    private ImageView img_back;
    private TextView tv_title;
    private Button btn_ok;
    private GridView gv_photo;
    private View view_bg;
    private Button btn_look;

    private AlbumBean mCurrentAlbum; // 当前选择的相册
    private List<PhotoBean> mData; // 相册里图片Bean的集合
    private MyAdapter mAdapter;
    private List<PhotoBean> mSelectedPhotos; // 已选的图片Bean集合

    private int mCurrentSelectNum; // 当前已选择数量
    private int mTotalNum; // 可以选择的图片数量

    /**
     * @param activity
     * @param requestCode
     * @param currentSelectNum 当前已选择图片数量
     * @param totalNum         总共可选图片数量
     */
    public static void startForResult(Activity activity, int requestCode, int currentSelectNum, int totalNum) {
        List<AlbumBean> albumList = AlbumData.getAlbumList(activity);
        if (albumList == null || albumList.size() == 0) {
            ToastUtils.show("您的手机还没有图片哦，快去拍照获取吧～");
            return;
        } else if (albumList.get(0).getPhotoList().size() == 0) {
            ToastUtils.show("您的手机还没有图片哦，快去拍照获取吧～");
            return;
        }
        Intent intent = new Intent(activity, PhotoMultiSelectActivity.class);
        intent.putExtra(CURRENT_SELECT_NUM, currentSelectNum);
        intent.putExtra(TOTAL_NUM, totalNum);
        JumpUtils.jumpForResult(activity, intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        if (mSelectedPhotos != null) {
            for (PhotoBean bean : mSelectedPhotos) {
                bean.setSelect(false);
            }
        }
        mSelectedPhotos.clear();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_photo_multi_select;
    }

    @Override
    protected void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        gv_photo = (GridView) findViewById(R.id.gv_photo);
        view_bg = findViewById(R.id.view_bg);
        btn_look = (Button) findViewById(R.id.btn_look);
    }

    @Override
    protected void setListener() {
        img_back.setOnClickListener(this);
        tv_title.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_look.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mCurrentSelectNum = getIntent().getIntExtra(CURRENT_SELECT_NUM, 0);
        mTotalNum = getIntent().getIntExtra(TOTAL_NUM, 0);
        mCurrentAlbum = AlbumData.getAlbumList(mActivity).get(0); // 默认显示全部图片
        tv_title.setText(mCurrentAlbum.getName());
        mData = new ArrayList<>();
        mData.addAll(mCurrentAlbum.getPhotoList());
        mAdapter = new MyAdapter();
        gv_photo.setAdapter(mAdapter);
        mSelectedPhotos = new ArrayList<>();
    }

    @Override
    protected void begin() {

    }

    @Override
    protected void processMessageEvent(MessageEvent event) {
        super.processMessageEvent(event);
        switch (event.id) {
            case MsgEventConstants.CHANGE_ALBUM:
                for (PhotoBean bean : mSelectedPhotos) {
                    bean.setSelect(false);
                }
                mSelectedPhotos.clear();
                btn_ok.setText("确定");
                btn_ok.setEnabled(false);
                btn_ok.setTextColor(Color.parseColor("#999999"));
                btn_look.setText("预览");
                btn_look.setEnabled(false);
                btn_look.setTextColor(Color.parseColor("#999999"));
                mCurrentSelectNum = getIntent().getIntExtra(CURRENT_SELECT_NUM, 0);
                mCurrentAlbum = AlbumData.getCurrentSelectedAlbum();
                tv_title.setText(mCurrentAlbum.getName());
                mData.clear();
                mData.addAll(mCurrentAlbum.getPhotoList());
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void back() {
        Intent intent = new Intent();
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
        } else if (v.getId() == R.id.btn_ok) {
            ArrayList<String> picUrls = new ArrayList<String>();
            for (int i = 0; i < mSelectedPhotos.size(); ++i) {
                picUrls.add(mSelectedPhotos.get(i).getPath());
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra(SELECT_RESULT, picUrls);
            setResult(RESULT_OK, intent);
            finish();
        } else if (v.getId() == R.id.btn_look) {
            ArrayList<String> picUrls = new ArrayList<String>();
            for (int i = 0; i < mSelectedPhotos.size(); ++i) {
                picUrls.add(mSelectedPhotos.get(i).getPath());
            }
            BrowserBigPhotoActivity.start(mActivity, 0, picUrls);
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
                holder.img_status = (ImageView) convertView.findViewById(R.id.img_status);

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

            final View view_bg = holder.view_bg;
            final ImageView img = holder.img_status;
            final boolean select = bean.isSelect();
            if (select) {
                img.setImageResource(R.drawable.select);
                view_bg.setVisibility(View.VISIBLE);
            } else {
                img.setImageResource(R.drawable.unselect);
                view_bg.setVisibility(View.GONE);
            }
            holder.relative_status.setOnClickListener(new View.OnClickListener() {
                private boolean isSelect = select;

                @Override
                public void onClick(View v) {
                    if (isSelect) {
                        view_bg.setVisibility(View.GONE);
                        img.setImageResource(R.drawable.unselect);
                        mSelectedPhotos.remove(bean);
                        --mCurrentSelectNum;
                    } else {
                        if (mCurrentSelectNum >= mTotalNum) {
                            Toast.makeText(mActivity, "最多可以选择" + mTotalNum + "张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        view_bg.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.select);
                        mSelectedPhotos.add(bean);
                        ++mCurrentSelectNum;
                    }
                    isSelect = !isSelect;
                    bean.setSelect(isSelect);
                    if (mCurrentSelectNum == 0) {
                        btn_ok.setText("确定");
                        btn_ok.setEnabled(false);
                        btn_ok.setTextColor(Color.parseColor("#999999"));
                        btn_look.setText("预览");
                        btn_look.setEnabled(false);
                        btn_look.setTextColor(Color.parseColor("#999999"));
                    } else {
                        btn_ok.setText("确定(" + (mCurrentSelectNum - getIntent().getIntExtra(CURRENT_SELECT_NUM, 0)) + ")");
                        btn_ok.setEnabled(true);
                        btn_ok.setTextColor(Color.parseColor("#ffffff"));
                        btn_look.setText("预览(" + (mCurrentSelectNum - getIntent().getIntExtra(CURRENT_SELECT_NUM, 0)) + ")");
                        btn_look.setEnabled(true);
                        btn_look.setTextColor(Color.parseColor("#ffffff"));
                    }
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 查看大图
                    ArrayList<String> picUrls = new ArrayList<String>();
                    picUrls.add(bean.getPath());
                    BrowserBigPhotoActivity.start(mActivity, 0, picUrls);
                }
            });

            return convertView;
        }

        class ViewHolder {
            ImageView img_photo;
            View view_bg;
            RelativeLayout relative_status;
            ImageView img_status;
        }

    }
}
