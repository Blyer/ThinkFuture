package com.ysy.thinkfuture.activity;

import android.content.Intent;
import android.widget.ListView;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.activity.base.FutureBaseActivity;
import com.ysy.thinkfuture.adapter.ListViewTestAdapter;
import com.ysy.thinkfuture.constants.UrlConstants;
import com.ysy.thinkfuture.utils.PullToRefreshHelper;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.MessageEvent;
import org.base.platform.bean.ResponseResult;
import org.base.platform.enums.HttpMethod;
import org.base.platform.utils.JsonUtils;
import org.base.platform.utils.StatusBarCompat;
import org.base.platform.utils.pulltorefresh.PullToRefreshContainer;
import org.base.platform.view.EmptyView;

import java.util.List;

public class TestListViewActivity extends FutureBaseActivity {

    private PullToRefreshContainer rf_container;
    private ListView lv_data;
    private EmptyView ev_no_data;

    private ListViewTestAdapter mAdapter;

    private PullToRefreshHelper mPullToRefreshHelper;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_test_list_view;
    }

    @Override
    protected void resolveIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        rf_container = (PullToRefreshContainer) findViewById(R.id.rf_container);
        lv_data = (ListView) findViewById(R.id.lv_data);
        ev_no_data = (EmptyView) findViewById(R.id.ev_no_data);
    }

    @Override
    protected void initData() {
        StatusBarCompat.compat(this, getResources().getColor(org.base.platform.R.color.blue_1));

        mAdapter = new ListViewTestAdapter(mActivity, R.layout.item_data_1);
        mPullToRefreshHelper = new PullToRefreshHelper(rf_container, mAdapter);

        lv_data.setAdapter(mAdapter);
        rf_container.autoRefresh();
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
            case org.base.platform.constants.MsgEventConstants.NET_REQUEST_ERROR:
                if ((int) event.extraData == 111)
                    mPullToRefreshHelper.processEmptyList();
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
