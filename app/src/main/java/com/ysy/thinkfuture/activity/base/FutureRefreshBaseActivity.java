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

    protected PullToRefreshContainer container_refresh;
    protected UnifyAdapter mAdapter;

    @Override
    protected void initView() {
        container_refresh = (PullToRefreshContainer) findViewById(R.id.container_refresh);
    }

    @Override
    protected void setListener() {
        container_refresh.setRefreshListener(new RefreshListener() {
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

    public void processListData(List data) {
        if (mAdapter != null) {
            if (mPageIndex == 1) {
                if (data != null && data.size() > 0) {
                    mAdapter.clearTo(data);
                } else if (mAdapter.getItemCount() == 0) {
                    // 没有数据显示缺省页面
                }
                container_refresh.setFinish(State.REFRESH);
            } else {
                if (data != null && data.size() > 0) {
                    mAdapter.append(data);
                }
                container_refresh.setFinish(State.LOADMORE);
            }
        }
    }

}
