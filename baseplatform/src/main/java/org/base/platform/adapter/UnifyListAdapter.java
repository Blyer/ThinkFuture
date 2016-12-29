package org.base.platform.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.base.platform.callback.BaseAdapterCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YinShengyi on 2016/12/29.
 */
public abstract class UnifyListAdapter<T> extends BaseAdapter implements BaseAdapterCallback<T> {
    private Context mContext;
    private int mLayoutId;
    private List<T> mData;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public UnifyListAdapter(Context context, int layoutId) {
        mContext = context;
        mLayoutId = layoutId;
        mData = new ArrayList<>();
    }

    @Override
    public void clearTo(List<T> data) {
        mData.clear();
        if (data != null && data.size() > 0) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public void append(List<T> data) {
        if (data != null && data.size() > 0) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public List<T> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UnifyListHolder holder = UnifyListHolder.getHolder(mContext, convertView, mLayoutId, parent, position);
        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, holder.getPosition());
                }
            }
        });
        holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemLongClick(v, holder.getPosition());
                    return true;
                }
                return false;
            }
        });
        bindData(holder, getItem(position));
        return holder.getItemView();
    }

    public abstract void bindData(UnifyListHolder holder, T item);

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }
}
