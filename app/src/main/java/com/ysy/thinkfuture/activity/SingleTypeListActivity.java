package com.ysy.thinkfuture.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.activity.base.FutureBaseActivity;
import com.ysy.thinkfuture.adapter.SingleTypeAdapter;
import com.ysy.thinkfuture.divider.HorizontalLineItemDivider;

import org.base.platform.adapter.UnifyAdapter;
import org.base.platform.utils.ToastUtils;
import org.base.platform.utils.pulltorefresh.PullToRefreshContainer;
import org.base.platform.utils.pulltorefresh.RefreshListener;
import org.base.platform.utils.pulltorefresh.State;

import java.util.ArrayList;
import java.util.List;

public class SingleTypeListActivity extends FutureBaseActivity {

    private PullToRefreshContainer container_refresh;
    private RecyclerView rv_data;

    private List<String> mData;
    private SingleTypeAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_single_type_list;
    }

    @Override
    protected void resolveIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        container_refresh = (PullToRefreshContainer) findViewById(R.id.container_refresh);
        rv_data = (RecyclerView) findViewById(R.id.rv_data);
    }

    @Override
    protected void setListener() {
        container_refresh.setRefreshListener(new RefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.append(mData);
                        container_refresh.setFinish(State.REFRESH);
                    }
                }, 1000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.append(mData);
                        container_refresh.setFinish(State.LOADMORE);
                    }
                }, 500);
            }
        });
        mAdapter.setOnClickListener(new UnifyAdapter.OnClickListener() {
            @Override
            public void onClickListener(View view, int position) {
                String item = mAdapter.getItem(position);
                ToastUtils.show("Click:" + item);
            }
        });
        mAdapter.setOnLongClickListener(new UnifyAdapter.OnLongClickListener() {
            @Override
            public void onLongClickListener(View view, int position) {
                String item = mAdapter.getItem(position);
                ToastUtils.show("Long Click:" + item);
            }
        });

    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        for (int i = 0; i < 1; ++i) {
            mData.add("1");
            mData.add("2");
        }
        mAdapter = new SingleTypeAdapter(this, R.layout.item_data_1);

        rv_data.setLayoutManager(new LinearLayoutManager(this));
        rv_data.setAdapter(mAdapter);
//        mAdapter.clearTo(mData);
        rv_data.addItemDecoration(new HorizontalLineItemDivider(this, R.color.red_1, 1));

//        container_refresh.autoRefresh();
    }

    @Override
    protected int getStatusBarColor() {
        return getResources().getColor(R.color.blue_1);
    }
}
