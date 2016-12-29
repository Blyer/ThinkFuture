package com.ysy.thinkfuture.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.activity.base.FutureBaseActivity;
import com.ysy.thinkfuture.adapter.SingleTypeRecyclerAdapter;
import com.ysy.thinkfuture.constants.UrlConstants;
import com.ysy.thinkfuture.divider.HorizontalLineItemDivider;

import org.base.platform.adapter.UnifyRecyclerAdapter;
import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.MessageEvent;
import org.base.platform.bean.ResponseResult;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.JsonUtils;
import org.base.platform.utils.PullToRefreshHelper;
import org.base.platform.utils.StatusBarCompat;
import org.base.platform.utils.ToastUtils;

import java.util.List;

import static org.base.platform.constants.MsgEventConstants.NET_REQUEST_ERROR;


public class SingleTypeListActivity extends FutureBaseActivity {

    private RecyclerView rv_data;

    private SingleTypeRecyclerAdapter mAdapter;

    private PullToRefreshHelper mPullToRefreshHelper;

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
        StatusBarCompat.compat(this, getResources().getColor(org.base.platform.R.color.blue_1));
        mAdapter = new SingleTypeRecyclerAdapter(this, R.layout.item_data_1);
        mPullToRefreshHelper = new PullToRefreshHelper(rv_data, mAdapter);

        rv_data.setLayoutManager(new LinearLayoutManager(this));
        rv_data.setAdapter(mAdapter);
        rv_data.addItemDecoration(new HorizontalLineItemDivider(this, R.color.red_1, 1));

    }

    @Override
    protected void setListener() {
        mPullToRefreshHelper.setOnRequestDataListener(new PullToRefreshHelper.OnRequestDataListener() {
            @Override
            public void onRequestData() {
                generateListRequest();
                mHttpUtils.request();
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

    @Override
    public void processNetRequest(int id, ResponseResult result, boolean isCache) {
        super.processNetRequest(id, result, isCache);
        switch (id) {
            case 111:
                if (result.getCode() == 0) {
                    List<String> list = JsonUtils.jsonToList(result.getData(), String.class);
                    mPullToRefreshHelper.processListData(list, isCache);
                } else {
                    mPullToRefreshHelper.processEmptyList();
                }
                break;
        }
    }

    @Override
    protected void processMessageEvent(MessageEvent event) {
        super.processMessageEvent(event);
        switch (event.id) {
            case NET_REQUEST_ERROR:
                if ((int) event.extraData == 111) {
                    mPullToRefreshHelper.processEmptyList();
                }
                break;
        }
    }

    private HttpRequestPackage generateListRequest() {
        HttpRequestPackage request = new HttpRequestPackage();
        request.id = 111;
        request.isSilentRequest = true;
        request.method = HttpMethod.GET;
        request.url = UrlConstants.host + "/list.txt";
        request.params.put("id", "111");
        request.params.put("page", mPullToRefreshHelper.mPageIndex);
        request.params.put("pageCount", mPullToRefreshHelper.mPageCount);
        request.params.put("time", System.currentTimeMillis());
        mHttpUtils.addRequest(request);
        return request;
    }
}
