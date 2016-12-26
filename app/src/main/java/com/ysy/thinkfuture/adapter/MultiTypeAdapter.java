package com.ysy.thinkfuture.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ysy.thinkfuture.R;

import org.base.platform.adapter.UnifyAdapter;
import org.base.platform.adapter.UnifyHolder;

/**
 * Created by YinShengyi on 2016/12/26.
 */
public class MultiTypeAdapter extends UnifyAdapter<String> {

    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;

    public MultiTypeAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindData(UnifyHolder holder, String item) {
        TextView tv_level = holder.getView(R.id.tv_level);
        TextView tv_time = holder.getView(R.id.tv_time);

        tv_level.setText(item);
        tv_time.setText(item);
    }

    @Override
    public int getViewType(int position) {
        if (Integer.parseInt(getItem(position)) % 2 == 0) {
            return TYPE_1;
        } else {
            return TYPE_2;
        }
    }
}
