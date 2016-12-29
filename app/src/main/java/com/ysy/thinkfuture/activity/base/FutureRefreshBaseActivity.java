package com.ysy.thinkfuture.activity.base;

import com.ysy.thinkfuture.R;

import org.base.platform.adapter.UnifyAdapter;
import org.base.platform.utils.pulltorefresh.PullToRefreshContainer;
import org.base.platform.utils.pulltorefresh.RefreshListener;
import org.base.platform.utils.pulltorefresh.State;

import java.util.List;

/**
 * Created by YinShengyi on 2016/12/28.
 */
public abstract class FutureRefreshBaseActivity extends FutureBaseActivity {
    protected int mPageIndex;
    protected int mPageCount = 20;

    protected PullToRefreshContainer refresh_container;
    protected UnifyAdapter mAdapter;

    @Override
    protected void initView() {
        refresh_container = (PullToRefreshContainer) findViewById(R.id.refresh_container);
    }

    @Override
    protected void setListener() {
        refresh_container.setRefreshListener(new RefreshListener() {
            @Override
            public void refresh() {
                mPageIndex = 1;
                requestListData();
            }

            @Override
            public void loadMore() {
                ++mPageIndex;
                requestListData();
            }
        });
    }

    public abstract void requestListData();

    public void processListData(List data, boolean isCache) {
        if (mAdapter != null) {
            if (mPageIndex == 1) {
                if (data != null && data.size() > 0) {
                    mAdapter.clearTo(data);
                    refresh_container.showDataView();
                } else if (mAdapter.getItemCount() == 0) {
                    // 没有数据显示缺省页面
                    refresh_container.showEmptyView();
                }
                refresh_container.setFinish(State.REFRESH);
            } else {
                if (data != null && data.size() > 0 && !isCache) {
                    mAdapter.append(data);
                }
                refresh_container.setFinish(State.LOADMORE);
            }
        }
    }

    public void processEmptyList() {
        processListData(null, true);
    }

}
