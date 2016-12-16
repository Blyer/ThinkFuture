package org.base.platform.utils.photoselect.inner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.base.platform.R;
import org.base.platform.utils.ImageUtils;

import java.util.List;

/**
 * Created by YinShengyi on 2015/10/19.
 * 选择相册
 */
public class AlbumListWindow {

    public static final String CHANGE_ALBUM = "change_album";

    private Activity mActivity;
    private PopupWindow mWindow;
    private static List<AlbumBean> mData;

    public AlbumListWindow(Activity activity) {
        mActivity = activity;
    }

    /**
     * 显示PopupWindow
     *
     * @param view 定位点的View
     */
    public void show(View view) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.window_photo_album_list, null);
        mWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mWindow.setAnimationStyle(R.style.popwin_anim_style);
        mWindow.setFocusable(true);
        mWindow.setOutsideTouchable(true);
        mWindow.setBackgroundDrawable(new ColorDrawable(0));

        LinearLayout linear_pop = (LinearLayout) v.findViewById(R.id.linear_pop);
        ListView lv_album_list = (ListView) v.findViewById(R.id.lv_album_list);

        linear_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWindow != null && mWindow.isShowing())
                    mWindow.dismiss();
            }
        });
        mData = AlbumData.getAlbumList(mActivity);
        lv_album_list.setAdapter(new MyAdapter());

        mWindow.showAsDropDown(view);
    }

    public PopupWindow getWindow() {
        return mWindow;
    }

    private class MyAdapter extends BaseAdapter {

        ImageUtils imageUtil = new ImageUtils(mActivity);

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
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.album_list_item, parent, false);
                holder = new ViewHolder();
                holder.img_thumbnail = (ImageView) convertView.findViewById(R.id.img_thumbnail);
                holder.tv_album_name = (TextView) convertView.findViewById(R.id.tv_album_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final AlbumBean bean = mData.get(position);
            holder.img_thumbnail.setImageBitmap(null);
            imageUtil.displayImage(bean.getPhotoPath(), holder.img_thumbnail);
            holder.tv_album_name.setText(bean.getName() + " ( " + bean.getCount() + " )");

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlbumData.setCurrentSelectedAlbum(bean);
                    mActivity.sendBroadcast(new Intent(CHANGE_ALBUM));
                    if (mWindow != null && mWindow.isShowing())
                        mWindow.dismiss();
                }
            });

            return convertView;
        }

        class ViewHolder {
            ImageView img_thumbnail;
            TextView tv_album_name;
        }

    }
}
