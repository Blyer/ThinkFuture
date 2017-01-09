package com.ysy.thinkfuture.core.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.adapter.SingleTypeRecyclerAdapter;
import com.ysy.thinkfuture.constants.UrlConstants;
import com.ysy.thinkfuture.core.activity.base.FutureBaseActivity;
import com.ysy.thinkfuture.core.helper.activityhelper.SingleTypeListHelper;
import com.ysy.thinkfuture.divider.HorizontalLineItemDivider;

import org.base.platform.adapter.UnifyRecyclerAdapter;
import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.PullToRefreshHelper;
import org.base.platform.utils.StatusBarUtils;
import org.base.platform.utils.ToastUtils;

import java.util.List;


public class SingleTypeListActivity extends FutureBaseActivity {

    private RecyclerView rv_data;

    private SingleTypeRecyclerAdapter mAdapter;

    private PullToRefreshHelper mPullToRefreshHelper;
    private SingleTypeListHelper mActivityHelper;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_single_type_list;
    }

    @Override
    protected void initView() {
        rv_data = (RecyclerView) findViewById(R.id.rv_data);
    }

    @Override
    protected void initData() {
        StatusBarUtils.compat(this, getResources().getColor(org.base.platform.R.color.blue_1));
        mAdapter = new SingleTypeRecyclerAdapter(this, R.layout.item_data_1);
        mPullToRefreshHelper = new PullToRefreshHelper(rv_data, mAdapter);
        mActivityHelper = new SingleTypeListHelper(this);

        rv_data.setLayoutManager(new LinearLayoutManager(this));
        rv_data.setAdapter(mAdapter);
        rv_data.addItemDecoration(new HorizontalLineItemDivider(this, R.color.red_1, 1));

    }

    @Override
    protected void setListener() {
        mPullToRefreshHelper.setOnRequestDataListener(new PullToRefreshHelper.OnRequestDataListener() {
            @Override
            public void onRequestData() {
                mActivityHelper.getList(generateListRequest(), false);
            }
        });
        mAdapter.setOnItemClickListener(new UnifyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String item = mAdapter.getItem(position);
                ToastUtils.show("Click:" + item);
            }
        });
        mAdapter.setOnItemLongClickListener(new UnifyRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                String item = mAdapter.getItem(position);
                ToastUtils.show("Long Click:" + item);
            }
        });
        rv_data.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!rv_data.canScrollVertically(1)) {
                        mPullToRefreshHelper.autoLoadMore();
                    }
                }
            }
        });
    }

    @Override
    protected void begin() {
        mPullToRefreshHelper.autoRefresh();
    }

    public void getListSuccess(List<String> list) {
        mPullToRefreshHelper.processListData(list);
    }

    public void getListFailed() {
        mPullToRefreshHelper.processEmptyList();
    }

    private HttpRequestPackage generateListRequest() {
        HttpRequestPackage request = new HttpRequestPackage();
        request.method = HttpMethod.GET;
        request.url = UrlConstants.host + "/list.txt";
        request.params.put("id", "111");
        request.params.put("page", mPullToRefreshHelper.mPageIndex);
        request.params.put("pageCount", mPullToRefreshHelper.mPageCount);
        request.params.put("time", System.currentTimeMillis());
        return request;
    }
}
