package org.base.platform.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * Created by YinShengyi on 2016/11/7.
 * 精确高度类似于ListView的控件
 */
public class ExactListView extends LinearLayout {

    private BaseAdapter mAdapter;
    private OnItemClickListener mOnItemClickListener;
    private DataChangeObserver mObserver;

    public ExactListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void drawLayout() {
        if (mAdapter == null) {
            return;
        }

        this.removeAllViews();

        for (int i = 0; i < mAdapter.getCount(); i++) {
            final View view = mAdapter.getView(i, null, null);
            if (mOnItemClickListener != null) {
                final int position = i;
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(ExactListView.this, view, position);
                    }
                });
            }
            this.addView(view);
        }
    }

    public void setAdapter(BaseAdapter adapter) {
        if (mAdapter != null && mObserver != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        if (adapter != null) {
            mAdapter = adapter;
            if (mObserver == null) {
                mObserver = new DataChangeObserver();
            }
            mAdapter.registerDataSetObserver(mObserver);
            drawLayout();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ExactListView parent, View view, int position);
    }

    class DataChangeObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            ExactListView.this.drawLayout();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }

}
