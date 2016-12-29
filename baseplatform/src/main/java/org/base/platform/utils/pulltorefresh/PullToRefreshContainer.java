package org.base.platform.utils.pulltorefresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.base.platform.utils.ViewUtils;
import org.base.platform.view.EmptyView;

public class PullToRefreshContainer extends FrameLayout {

    private static final long ANIM_TIME = 250; // 动画执行的时间
    private static int header_height; // 下拉刷新的布局本身高度
    private static int current_header_height; // 当前下拉刷新的布局高度
    private static int footer_height; // 上拉加载的布局本身高度
    private static int current_footer_height; // 当前上拉加载的布局高度

    private View mEmptyView; // 没有数据时的空View
    private View mChildView; // 核心组件，为ListView或RecycleView或其他控件
    private View mCurrentView;
    private BaseView mHeaderView; // 下拉刷新的布局
    private BaseView mFooterView; // 上拉加载的布局
    private boolean isRefresh; // 是否正在刷新中
    private boolean isLoadMore; // 是否正在加载更多种
    private float mTouchY; // 手指按下时的初始Y轴坐标

    private boolean canLoadMore = true; // 是否开启了加载更多
    private boolean canRefresh = true; // 是否开启了下拉刷新

    private BaseRefreshListener refreshListener;

    public void setRefreshListener(BaseRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public PullToRefreshContainer(Context context) {
        this(context, null);
    }

    public PullToRefreshContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mEmptyView = new EmptyView(getContext());
        ViewUtils.setOnClickListener(mEmptyView, new ViewUtils.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoRefresh();
            }
        });
    }

    public void setHeaderView(BaseView view) {
        mHeaderView = view;
    }

    public void setFooterView(BaseView view) {
        mFooterView = view;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mChildView = getChildAt(0);
        mCurrentView = mChildView;
        mChildView.post(new Runnable() {
            @Override
            public void run() {
                if (!canChildScrollDown() && !canChildScrollUp()) {
                    setLoadMore(false);
                } else {
                    setLoadMore(true);
                }
            }
        });
        addEmptyView();
        addHeaderView();
        addFooterView();
    }

    private void addEmptyView() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.TOP;
        mEmptyView.setLayoutParams(layoutParams);
        mEmptyView.setVisibility(GONE);
        addView(mEmptyView);
    }

    public void showEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(VISIBLE);
            mChildView.setVisibility(GONE);
            mCurrentView = mEmptyView;
            resetHeaderAndFooter();
        }
    }

    public void showDataView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(GONE);
            mChildView.setVisibility(VISIBLE);
            mCurrentView = mChildView;
            resetHeaderAndFooter();
        }
    }

    private void addFooterView() {
        if (mFooterView == null) {
            mFooterView = new FooterView(getContext());
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM;
        mFooterView.setLayoutParams(layoutParams);
        if (mFooterView.getParent() != null)
            ((ViewGroup) mFooterView.getParent()).removeAllViews();
        mFooterView.post(new Runnable() {
            @Override
            public void run() {
                footer_height = mFooterView.getMeasuredHeight();
            }
        });
        mFooterView.setVisibility(INVISIBLE);
        addView(mFooterView);
    }

    private void addHeaderView() {
        if (mHeaderView == null) {
            mHeaderView = new HeaderView(getContext());
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mHeaderView.setLayoutParams(layoutParams);
        if (mHeaderView.getParent() != null)
            ((ViewGroup) mHeaderView.getParent()).removeAllViews();
        mHeaderView.post(new Runnable() {
            @Override
            public void run() {
                header_height = mHeaderView.getMeasuredHeight();
            }
        });
        mHeaderView.setVisibility(INVISIBLE);
        addView(mHeaderView, 0);
    }

    public void setLoadMore(boolean enable) {
        canLoadMore = enable;
    }

    public void setRefresh(boolean enable) {
        canRefresh = enable;
    }

    private void resetHeaderAndFooter() {
        boolean flag = false;
        if (mHeaderView.getVisibility() != VISIBLE) {
            mHeaderView.getLayoutParams().height = 0;
            mHeaderView.setVisibility(VISIBLE);
            flag = true;
        }
        if (mFooterView.getVisibility() != VISIBLE) {
            mFooterView.getLayoutParams().height = 0;
            mFooterView.setVisibility(VISIBLE);
            flag = true;
        }
        if (flag) {
            requestLayout();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        resetHeaderAndFooter();
        if (!canLoadMore && !canRefresh) return false;
        if (isRefresh || isLoadMore) return false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = ev.getY() - mTouchY;
                if (canRefresh) {
                    if (dy > 0 && !canChildScrollUp()) {
                        mHeaderView.begin();
                        return true;
                    }
                }
                if (canLoadMore) {
                    if (dy < 0 && !canChildScrollDown()) {
                        mFooterView.begin();
                        return true;
                    }
                }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dura = event.getY() - mTouchY;
                if (dura == 0) {
                    mHeaderView.getLayoutParams().height = 0;
                    mHeaderView.progress(0);
                    mFooterView.getLayoutParams().height = 0;
                    mFooterView.progress(0);
                    ViewCompat.setTranslationY(mCurrentView, 0);
                    requestLayout();
                } else if (dura > 0) {
                    if (canRefresh && !canChildScrollUp()) {
                        dura = dura * 0.5f;
                        mHeaderView.getLayoutParams().height = (int) dura;
                        mHeaderView.progress(dura);
                        ViewCompat.setTranslationY(mCurrentView, dura);
                        requestLayout();
                    } else {
                        mHeaderView.getLayoutParams().height = 0;
                        mHeaderView.progress(0);
                        mFooterView.getLayoutParams().height = 0;
                        mFooterView.progress(0);
                        ViewCompat.setTranslationY(mCurrentView, 0);
                        requestLayout();
                    }
                } else {
                    if (canLoadMore && !canChildScrollDown()) {
                        dura = Math.abs(dura * 0.5f);
                        mFooterView.getLayoutParams().height = (int) dura;
                        mFooterView.progress(dura);
                        ViewCompat.setTranslationY(mCurrentView, -dura);
                        requestLayout();
                    } else {
                        mHeaderView.getLayoutParams().height = 0;
                        mHeaderView.progress(0);
                        mFooterView.getLayoutParams().height = 0;
                        mFooterView.progress(0);
                        ViewCompat.setTranslationY(mCurrentView, 0);
                        requestLayout();
                    }
                }
                current_header_height = (int) dura;
                current_footer_height = (int) dura;
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                final int dy = (int) (event.getY() - mTouchY);
                if (dy > 0) {
                    if (canRefresh && !canChildScrollUp()) {
                        if (current_header_height >= header_height) {
                            createAnimatorTranslationY(State.REFRESH, current_header_height, header_height, new CallBack() {
                                @Override
                                public void onSuccess() {
                                    isRefresh = true;
                                    if (refreshListener != null) {
                                        refreshListener.refresh();
                                    }
                                    mHeaderView.loading();
                                }
                            });
                        } else {
                            setFinish(current_header_height, State.REFRESH);
                            mHeaderView.normal();
                        }
                    }
                } else {
                    if (canLoadMore && !canChildScrollDown()) {
                        if (current_footer_height >= footer_height) {
                            createAnimatorTranslationY(State.LOADMORE, current_footer_height, footer_height, new CallBack() {
                                @Override
                                public void onSuccess() {
                                    isLoadMore = true;
                                    if (refreshListener != null) {
                                        refreshListener.loadMore();
                                    }
                                    mFooterView.loading();
                                }
                            });
                        } else {
                            setFinish(current_footer_height, State.LOADMORE);
                            mFooterView.normal();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);

    }

    public boolean canChildScrollUp() {
        if (mCurrentView == null) {
            return false;
        }
        return ViewCompat.canScrollVertically(mCurrentView, -1);
    }

    public boolean canChildScrollDown() {
        if (mCurrentView == null) {
            return false;
        }
        return ViewCompat.canScrollVertically(mCurrentView, 1);
    }

    /**
     * 创建动画
     */
    public void createAnimatorTranslationY(@State.REFRESH_STATE final int state, final int start, final int purpose, final CallBack calllBack) {
        final ValueAnimator anim;
        anim = ValueAnimator.ofInt(start, purpose);
        anim.setDuration(ANIM_TIME);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();

                if (state == State.REFRESH) {
                    mHeaderView.getLayoutParams().height = value;
                    ViewCompat.setTranslationY(mCurrentView, value);
                    if (purpose == 0) { //代表结束加载
                        mHeaderView.finishing(value);
                    }
                } else {
                    mFooterView.getLayoutParams().height = value;
                    ViewCompat.setTranslationY(mCurrentView, -value);
                    if (purpose == 0) { //代表结束加载
                        mFooterView.finishing(value);
                    }
                }
                if (value == purpose) {
                    if (calllBack != null)
                        calllBack.onSuccess();
                }
                requestLayout();
            }

        });
        anim.start();
    }

    /**
     * 用于自动刷新的动画
     */
    public void createAutoAnimatorTranslationY(final View v, final int start, final int purpose, final CallBack calllBack) {
        final ValueAnimator anim = ValueAnimator.ofInt(start, purpose);
        anim.setDuration(ANIM_TIME);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                v.getLayoutParams().height = value;
                ViewCompat.setTranslationY(mCurrentView, value);
                requestLayout();
                if (value == 0) {
                    mHeaderView.begin();
                } else if (value == purpose) {
                    mHeaderView.loading();
                    if (calllBack != null)
                        calllBack.onSuccess();
                } else {
                    mHeaderView.progress(value);
                }
            }
        });
        anim.start();
    }

    /**
     * 自动下拉刷新
     */
    public void autoRefresh() {
        if (canRefresh) {
            post(new Runnable() {
                @Override
                public void run() {
                    resetHeaderAndFooter();
                    mHeaderView.post(new Runnable() {
                        @Override
                        public void run() {
                            createAutoAnimatorTranslationY(mHeaderView, 0, header_height, new CallBack() {
                                @Override
                                public void onSuccess() {
                                    isRefresh = true;
                                    if (refreshListener != null) {
                                        refreshListener.refresh();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    /**
     * 结束下拉刷新
     */
    private void setFinish(int height, @State.REFRESH_STATE final int state) {
        createAnimatorTranslationY(state, height, 0, new CallBack() {
            @Override
            public void onSuccess() {
                if (state == State.REFRESH) {
                    isRefresh = false;
                    if (refreshListener != null) {
                        refreshListener.finish();
                    }
                } else {
                    isLoadMore = false;
                    if (refreshListener != null) {
                        refreshListener.finishLoadMore();
                    }
                }
            }
        });
    }

    public void setFinish(@State.REFRESH_STATE int state) {
        if (state == State.REFRESH) {
            if (mHeaderView != null && mHeaderView.getLayoutParams().height > 0 && isRefresh) {
                setFinish(header_height, state);
            }
        } else {
            if (mFooterView != null && mFooterView.getLayoutParams().height > 0 && isLoadMore) {
                setFinish(footer_height, state);
            }
        }
        mChildView.post(new Runnable() {
            @Override
            public void run() {
                if (!canChildScrollDown() && !canChildScrollUp()) {
                    setLoadMore(false);
                } else {
                    setLoadMore(true);
                }
            }
        });
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
    }

    public interface CallBack {
        void onSuccess();
    }

}
