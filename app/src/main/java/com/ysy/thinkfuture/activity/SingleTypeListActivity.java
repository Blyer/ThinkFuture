package com.ysy.thinkfuture.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.activity.base.FutureRefreshBaseActivity;
import com.ysy.thinkfuture.adapter.SingleTypeAdapter;
import com.ysy.thinkfuture.divider.HorizontalLineItemDivider;

import org.base.platform.adapter.UnifyAdapter;
import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.MessageEvent;
import org.base.platform.bean.ResponseResult;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.JsonUtils;
import org.base.platform.utils.StatusBarCompat;
import org.base.platform.utils.ToastUtils;
import org.base.platform.view.EmptyView;

import java.util.ArrayList;
import java.util.List;

import static org.base.platform.constants.MsgEventConstants.NET_REQUEST_ERROR;

public class SingleTypeListActivity extends FutureRefreshBaseActivity {

    private RecyclerView rv_data;
    private EmptyView ev_no_data;

    private List<String> mData;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_single_type_list;
    }

    @Override
    protected void resolveIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        super.initView();
        rv_data = (RecyclerView) findViewById(R.id.rv_data);
        ev_no_data = (EmptyView) findViewById(R.id.ev_no_data);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mAdapter.setOnClickListener(new UnifyAdapter.OnClickListener() {
            @Override
            public void onClickListener(View view, int position) {
                String item = (String) mAdapter.getItem(position);
                ToastUtils.show("Click:" + item);
            }
        });
        mAdapter.setOnLongClickListener(new UnifyAdapter.OnLongClickListener() {
            @Override
            public void onLongClickListener(View view, int position) {
                String item = (String) mAdapter.getItem(position);
                ToastUtils.show("Long Click:" + item);
            }
        });
        ev_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateListRequest().isSilentRequest = false;
                mHttpUtils.request();
            }
        });
    }

    @Override
    public void requestListData() {
        generateListRequest();
        mHttpUtils.request();
    }

    @Override
    protected void initData() {
        StatusBarCompat.compat(this, getResources().getColor(org.base.platform.R.color.blue_1));
        mData = new ArrayList<>();
        for (int i = 0; i < 1; ++i) {
            mData.add("1");
            mData.add("2");
        }
        mAdapter = new SingleTypeAdapter(this, R.layout.item_data_1);

        rv_data.setLayoutManager(new LinearLayoutManager(this));
        rv_data.setAdapter(mAdapter);
        rv_data.addItemDecoration(new HorizontalLineItemDivider(this, R.color.red_1, 1));

        refresh_container.autoRefresh();
    }

    @Override
    public void processNetRequest(int id, ResponseResult result, boolean isCache) {
        super.processNetRequest(id, result, isCache);
        switch (id) {
            case 111:
                if (result.getCode() == 0) {
                    List<String> list = JsonUtils.jsonToList(result.getData(), String.class);
                    processListData(list, isCache);
                } else {
                    processEmpty();
                }
                break;
        }
    }

    @Override
    protected void processMessageEvent(MessageEvent event) {
        super.processMessageEvent(event);
        switch (event.id) {
            case NET_REQUEST_ERROR:
                processEmpty();
                break;
        }
    }

    private HttpRequestPackage generateListRequest() {
        HttpRequestPackage request = new HttpRequestPackage();
        request.id = 111;
        request.isSilentRequest = true;
        request.method = HttpMethod.GET;
        request.url = "http://192.168.2.79/list.txt";
        request.params.put("id", "111");
        request.params.put("page", mPageIndex);
        request.params.put("pageCount", mPageCount);
        request.params.put("time", System.currentTimeMillis());
        mHttpUtils.addRequest(request);
        return request;
    }
}
