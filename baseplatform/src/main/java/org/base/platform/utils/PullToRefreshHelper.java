package org.base.platform.utils;

import android.view.View;

import org.base.platform.callback.BaseAdapterCallback;
import org.base.platform.utils.pulltorefresh.PullToRefreshContainer;
import org.base.platform.utils.pulltorefresh.RefreshListener;
import org.base.platform.utils.pulltorefresh.State;

import java.util.List;

/**
 * Created by YinShengyi on 2016/12/29.
 */
public class PullToRefreshHelper {
    public int mPageIndex = 1;
    public int mPageCount = 20;

    private PullToRefreshContainer refresh_container;
    private BaseAdapterCallback mRefreshBaseAdapter;
    private OnRequestDataListener mOnRequestDataListener;

    public PullToRefreshHelper(View refreshContainerChild, BaseAdapterCallback refreshBaseAdapter) {
        this.refresh_container = (PullToRefreshContainer) refreshContainerChild.getParent();
        this.mRefreshBaseAdapter = refreshBaseAdapter;
        init();
    }

    private void init() {
        refresh_container.setRefreshListener(new RefreshListener() {
            @Override
            public void refresh() {
                mPageIndex = 1;
                if (mOnRequestDataListener != null) {
                    mOnRequestDataListener.onRequestData();
                }
            }

            @Override
            public void loadMore() {
                ++mPageIndex;
                if (mOnRequestDataListener != null) {
                    mOnRequestDataListener.onRequestData();
                }
            }
        });
    }

    public void processListData(List data, boolean isCache) {
        if (mRefreshBaseAdapter != null) {
            if (mPageIndex == 1) {
                if (data != null && data.size() > 0) {
                    mRefreshBaseAdapter.clearTo(data);
                    refresh_container.showDataView();
                } else if (mRefreshBaseAdapter.getData().size() == 0) {
                    // 没有数据显示缺省页面
                    refresh_container.showEmptyView();
                }
                refresh_container.setFinish(State.REFRESH);
            } else {
                if (data != null && data.size() > 0 && !isCache) {
                    mRefreshBaseAdapter.append(data);
                }
                refresh_container.setFinish(State.LOADMORE);
            }
        }
    }

    public void processEmptyList() {
        processListData(null, true);
    }

    public void autoRefresh() {
        refresh_container.autoRefresh();
    }

    public void setOnRequestDataListener(OnRequestDataListener listener) {
        mOnRequestDataListener = listener;
    }

    public interface OnRequestDataListener {
        void onRequestData();
    }
}
