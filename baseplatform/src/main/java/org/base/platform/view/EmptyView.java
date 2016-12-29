package org.base.platform.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.base.platform.R;

/**
 * Created by YinShengyi on 2016/12/28.
 */
public class EmptyView extends RelativeLayout {

    private ImageView img_no_data;
    private UnifyTextView tv_no_data;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.empty_view, this, true);
        img_no_data = (ImageView) findViewById(R.id.img_no_data);
        tv_no_data = (UnifyTextView) findViewById(R.id.tv_no_data);
        setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void setImageSrc(int resId) {
        img_no_data.setImageResource(resId);
    }

    public void setText(String txt) {
        tv_no_data.setText(txt);
    }

    public void setText(int resId) {
        tv_no_data.setText(resId);
    }

}
