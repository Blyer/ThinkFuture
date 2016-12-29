package org.base.platform.adapter;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by YinShengyi on 2016/12/29.
 */
public class UnifyListHolder {
    private SparseArrayCompat<View> mViews;
    private View mItemView;
    private int mPosition;

    public static UnifyListHolder getHolder(Context context, View itemView, int layoutId, ViewGroup parent, int position) {
        if (itemView == null) {
            return new UnifyListHolder(context, layoutId, parent, position);
        } else {
            UnifyListHolder holder = (UnifyListHolder) itemView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    private UnifyListHolder(Context context, int layoutId, ViewGroup parent, int position) {
        mItemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mPosition = position;
        mViews = new SparseArrayCompat<>();
        mItemView.setTag(this);
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getItemView() {
        return mItemView;
    }

    public int getPosition() {
        return mPosition;
    }

}
