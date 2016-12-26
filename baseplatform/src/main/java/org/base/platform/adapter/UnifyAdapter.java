package org.base.platform.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by YinShengyi on 2016/12/26.
 * UnifyAdapter For RecycleView
 */
public abstract class UnifyAdapter<T> extends RecyclerView.Adapter<UnifyHolder> {

    private Context mContext;
    private HashMap<Integer, Integer> mLayoutIds;
    private List<T> mData;

    private OnClickListener mOnClickListener;
    private OnLongClickListener mOnLongClickListener;

    /**
     * 多类型Item布局文件时，使用此构造方法生成Adapter对象
     * 而后调用addItemLayoutId方法添加Item布局文件的ID
     * 并且需要重写getViewType方法
     */
    public UnifyAdapter(Context context) {
        mContext = context;
        mLayoutIds = new HashMap<>();
        mData = new ArrayList<>();
    }

    /**
     * 单类型的Item布局文件使用此构造方法
     * 不需要调用addItemLayoutId方法添加Item布局文件
     * 也不需要在之类中重写getViewType方法
     */
    public UnifyAdapter(Context context, int layoutId) {
        this(context);
        mLayoutIds.put(0, layoutId);
    }

    /**
     * 根据不同的布局类型指定不同的布局
     *
     * @param type     布局的类型标识
     * @param layoutId 对应的布局资源ID
     */
    public void addItemLayoutId(int type, int layoutId) {
        mLayoutIds.put(type, layoutId);
    }

    /**
     * 将RecycleView的数据源改为data后绘制界面
     */
    public void clearTo(List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 添加数据至RecycleView的数据源中并绘制界面
     */
    public void append(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public void onBindViewHolder(UnifyHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClickListener(v, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongClickListener != null) {
                    mOnLongClickListener.onLongClickListener(v, position);
                    return true;
                }
                return false;
            }
        });
        bindData(holder, mData.get(position));
    }

    @Override
    public UnifyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(mLayoutIds.get(viewType), parent, false);
        return new UnifyHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    /**
     * 绑定数据至控件上
     *
     * @param holder 含有控件的ViewHolder
     * @param item   数据项
     */
    public abstract void bindData(UnifyHolder holder, T item);

    /**
     * 获取当前数据项应对应的布局的标识
     */
    public int getViewType(int position) {
        return 0;
    }

    public interface OnClickListener {
        void onClickListener(View view, int position);
    }

    public interface OnLongClickListener {
        void onLongClickListener(View view, int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }
}