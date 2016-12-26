package com.ysy.thinkfuture.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ysy.thinkfuture.R;
import com.ysy.thinkfuture.activity.base.FutureBaseActivity;
import com.ysy.thinkfuture.adapter.SingleTypeAdapter;
import com.ysy.thinkfuture.divider.HorizontalLineItemDivider;

import org.base.platform.adapter.UnifyAdapter;
import org.base.platform.utils.ToastUtils;
import org.base.platform.view.UnifyButton;

import java.util.ArrayList;
import java.util.List;

public class SingleTypeListActivity extends FutureBaseActivity {

    private UnifyButton btn_load;
    private UnifyButton btn_copy;
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
        btn_load = (UnifyButton) findViewById(R.id.btn_load);
        btn_copy = (UnifyButton) findViewById(R.id.btn_copy);
        rv_data = (RecyclerView) findViewById(R.id.rv_data);
    }

    @Override
    protected void setListener() {
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

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.clearTo(mData);
            }
        });
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.append(mData);
            }
        });
    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        mData.add("1");
        mData.add("2");
        mAdapter = new SingleTypeAdapter(this, R.layout.item_data_1);

        rv_data.setLayoutManager(new LinearLayoutManager(this));
        rv_data.setAdapter(mAdapter);
        rv_data.addItemDecoration(new HorizontalLineItemDivider(this, R.color.red_1, 1));
    }

}
