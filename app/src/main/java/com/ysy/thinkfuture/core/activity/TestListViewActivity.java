package com.ysy.thinkfuture.core.activity;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.core.activity.base.FutureBaseActivity;
import com.ysy.thinkfuture.adapter.ListViewTestAdapter;
import com.ysy.thinkfuture.constants.UrlConstants;
import com.ysy.thinkfuture.core.activity.helper.TestListViewHelper;

import org.base.platform.adapter.UnifyListAdapter;
import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.PullToRefreshHelper;
import org.base.platform.utils.StatusBarUtils;
import org.base.platform.utils.ToastUtils;

import java.util.List;

public class TestListViewActivity extends FutureBaseActivity {

    private ListView lv_data;

    private ListViewTestAdapter mAdapter;

    private PullToRefreshHelper mPullToRefreshHelper;
    private TestListViewHelper mActivityHelper;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_test_list_view;
    }

    @Override
    protected void initView() {
        lv_data = (ListView) findViewById(R.id.lv_data);
    }

    @Override
    protected void initData() {
        StatusBarUtils.compat(this, getResources().getColor(org.base.platform.R.color.blue_1));

        mAdapter = new ListViewTestAdapter(mActivity, R.layout.item_data_1);
        mPullToRefreshHelper = new PullToRefreshHelper(lv_data, mAdapter);
        mActivityHelper = new TestListViewHelper(this);

        lv_data.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mPullToRefreshHelper.setOnRequestDataListener(new PullToRefreshHelper.OnRequestDataListener() {
            @Override
            public void onRequestData() {
                mActivityHelper.getList(generateListRequest());
            }
        });
        mAdapter.setOnItemClickListener(new UnifyListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtils.show("Click: " + mAdapter.getItem(position));
            }
        });
        mAdapter.setOnItemLongClickListener(new UnifyListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                ToastUtils.show("Long Click: " + mAdapter.getItem(position));
            }
        });

        lv_data.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && !lv_data.canScrollVertically(1)) {
                    mPullToRefreshHelper.autoLoadMore();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
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
