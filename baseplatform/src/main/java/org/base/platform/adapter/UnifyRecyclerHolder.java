package org.base.platform.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by YinShengyi on 2016/12/26.
 * ViewHolder For UnifyRecyclerAdapter
 */
public class UnifyRecyclerHolder extends RecyclerView.ViewHolder {
    private SparseArrayCompat<View> mViews;

    public UnifyRecyclerHolder(View itemView) {
        super(itemView);
        mViews = new SparseArrayCompat<>();
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

}
