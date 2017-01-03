package org.base.platform.utils;

import android.view.View;

import org.base.platform.callback.BaseAdapterCallback;
import org.base.platform.utils.pulltorefresh.PullToRefreshChildContainer;
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

    private PullToRefreshContainer mRefreshContainer;
    private BaseAdapterCallback mRefreshBaseAdapter;
    private OnRequestDataListener mOnRequestDataListener;

    public PullToRefreshHelper(View refreshContainerChild, BaseAdapterCallback refreshBaseAdapter) {
        PullToRefreshChildContainer refreshChildContainer = (PullToRefreshChildContainer) refreshContainerChild.getParent();
        refreshChildContainer.setScrollView(refreshContainerChild);
        mRefreshContainer = (PullToRefreshContainer) refreshChildContainer.getParent();
        mRefreshBaseAdapter = refreshBaseAdapter;
        init();
    }

    private void init() {
        mRefreshContainer.setRefreshListener(new RefreshListener() {
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
                    mRefreshContainer.showDataView();
                } else {
                    // 没有数据显示缺省页面
                    mRefreshContainer.showEmptyView();
                }
                mRefreshContainer.setFinish(State.REFRESH);
            } else {
                if (data != null && data.size() > 0 && !isCache) {
                    mRefreshBaseAdapter.append(data);
                }
                mRefreshContainer.setFinish(State.LOADMORE);
            }
        }
    }

    public void processEmptyList() {
        if (mPageIndex == 1) {
            if (mRefreshBaseAdapter == null || mRefreshBaseAdapter.getData().size() == 0) {
                mRefreshContainer.showEmptyView();
            }
            mRefreshContainer.setFinish(State.REFRESH);
        } else {
            mRefreshContainer.setFinish(State.LOADMORE);
        }
    }

    public void autoRefresh() {
        mRefreshContainer.autoRefresh();
    }

    public void autoLoadMore() {
        mRefreshContainer.autoLoadMore();
    }

    public PullToRefreshContainer getRefreshContainer() {
        return mRefreshContainer;
    }

    public void setOnRequestDataListener(OnRequestDataListener listener) {
        mOnRequestDataListener = listener;
    }

    public interface OnRequestDataListener {
        void onRequestData();
    }
}
