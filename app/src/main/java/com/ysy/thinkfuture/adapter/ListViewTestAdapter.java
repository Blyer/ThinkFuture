package com.ysy.thinkfuture.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ysy.thinkfuture.R;

import org.base.platform.adapter.UnifyListAdapter;
import org.base.platform.adapter.UnifyListHolder;

/**
 * Created by YinShengyi on 2016/12/29.
 */
public class ListViewTestAdapter extends UnifyListAdapter<String> {
    public ListViewTestAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void bindData(UnifyListHolder holder, String item) {
        TextView tv_level = holder.getView(R.id.tv_level);
        TextView tv_time = holder.getView(R.id.tv_time);

        tv_level.setText(item);
        tv_time.setText(item);
    }
}
