package org.base.platform.utils.photoselect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.base.platform.R;
import org.base.platform.activity.BaseActivity;
import org.base.platform.bean.ResponseResult;
import org.base.platform.utils.ImageUtils;
import org.base.platform.utils.JumpUtils;
import org.base.platform.utils.StatusBarCompat;
import org.xutils.common.Callback;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 浏览大图Activity
 */
public class BrowserBigPhotoActivity extends BaseActivity {

    private ViewPager vp_content;
    private TextView tv_indicator;
    private ProgressBar progress_loading;

    private int mPosition = 0; // 当前选择的图片的位置
    private ArrayList<String> mData; // 显示的图片的地址集合

    /**
     * @param activity
     * @param currentPosition 当前显示图片的位置
     * @param picUrls         图片集合
     */
    public static void start(Activity activity, int currentPosition, ArrayList<String> picUrls) {
        Intent intent = new Intent(activity, BrowserBigPhotoActivity.class);
        intent.putExtra("currentPosition", currentPosition);
        intent.putStringArrayListExtra("picUrls", picUrls);
        JumpUtils.jump(activity, intent, R.anim.anim_for_open_big_pic, R.anim.empty_anim);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_browser_big_photo;
    }

    @Override
    protected void resolveIntent(Intent intent) {
        mPosition = intent.getIntExtra("currentPosition", 0);
        mData = intent.getStringArrayListExtra("picUrls");
    }

    @Override
    protected void initView() {
        vp_content = (ViewPager) findViewById(R.id.vp_content);
        tv_indicator = (TextView) findViewById(R.id.tv_indicator);
        progress_loading = (ProgressBar) findViewById(R.id.progress_loading);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void initData() {
        StatusBarCompat.compat(this, getResources().getColor(R.color.black));
        setFinishAnim(R.anim.empty_anim, R.anim.anim_for_close_big_pic);
        forbidSwipeFinishActivity();

        if (mData == null || mData.size() == 0) {
            return;
        } else if (mData.size() == 1) {
            tv_indicator.setVisibility(View.GONE);
        }

        tv_indicator.setText((mPosition + 1) + "/" + mData.size());
        vp_content.setAdapter(new MyAdapter());
        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                tv_indicator.setText((position + 1) + "/" + mData.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public void processNetRequest(int id, ResponseResult result, boolean isCache) {

    }

    private class MyAdapter extends PagerAdapter {

        private ImageUtils imageUtil = new ImageUtils();
        private LayoutInflater mInflater = LayoutInflater.from(mActivity);

        {
            imageUtil.configImageSize(720, 1280).configScaleType(ImageView.ScaleType.FIT_CENTER).configFinish();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View v = mInflater.inflate(R.layout.big_photo_item, container, false);
            final PhotoView view = (PhotoView) v.findViewById(R.id.pv_big);
            final String url = mData.get(position);
            imageUtil.displayImage(url, view, new Callback.CommonCallback<Drawable>() {
                @Override
                public void onSuccess(Drawable result) {

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    progress_loading.setVisibility(View.GONE);
                }
            });

            view.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float v, float v1) {
                    finish();
                }
            });
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
