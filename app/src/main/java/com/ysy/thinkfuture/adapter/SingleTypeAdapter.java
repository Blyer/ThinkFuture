package com.ysy.thinkfuture.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ysy.thinkfuture.R;

import org.base.platform.adapter.UnifyAdapter;
import org.base.platform.adapter.UnifyHolder;

/**
 * Created by YinShengyi on 2016/12/26.
 */
public class SingleTypeAdapter extends UnifyAdapter<String> {

    public SingleTypeAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void bindData(UnifyHolder holder, String item) {
        TextView tv_level = holder.getView(R.id.tv_level);
        TextView tv_time = holder.getView(R.id.tv_time);

        tv_level.setText(item);
        tv_time.setText(item);
    }
}
