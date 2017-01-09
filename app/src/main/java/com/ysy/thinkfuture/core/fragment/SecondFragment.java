package com.ysy.thinkfuture.core.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.adapter.SingleTypeRecyclerAdapter;
import com.ysy.thinkfuture.constants.UrlConstants;
import com.ysy.thinkfuture.core.fragment.base.FutureBaseFragment;
import com.ysy.thinkfuture.core.helper.fragmenthelper.SecondFragmentHelper;
import com.ysy.thinkfuture.divider.HorizontalLineItemDivider;

import org.base.platform.adapter.UnifyRecyclerAdapter;
import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.PullToRefreshHelper;
import org.base.platform.utils.ToastUtils;

import java.util.List;

public class SecondFragment extends FutureBaseFragment {

    private RecyclerView rv_data;

    private SingleTypeRecyclerAdapter mAdapter;
    private PullToRefreshHelper mPullToRefreshHelper;
    private SecondFragmentHelper mFragmentHelper;

    public static FutureBaseFragment get() {
        SecondFragment fragment = new SecondFragment();
        Bundle bundle = new Bundle();
        bundle.putString("he", "Hello world");
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_second;
    }

    @Override
    protected void initView() {
        rv_data = (RecyclerView) findViewById(R.id.rv_data);
    }

    @Override
    protected void initData() {
        mAdapter = new SingleTypeRecyclerAdapter(mActivity, R.layout.item_data_1);
        mPullToRefreshHelper = new PullToRefreshHelper(rv_data, mAdapter);
        mFragmentHelper = new SecondFragmentHelper(this);

        rv_data.setLayoutManager(new LinearLayoutManager(mActivity));
        rv_data.setAdapter(mAdapter);
        rv_data.addItemDecoration(new HorizontalLineItemDivider(mActivity, R.color.red_1, 1));
    }

    @Override
    protected void setListener() {
        mPullToRefreshHelper.setOnRequestDataListener(new PullToRefreshHelper.OnRequestDataListener() {
            @Override
            public void onRequestData() {
                mFragmentHelper.getList(generateListRequest(), false);
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
