package org.base.platform.utils.pulltorefresh;

import android.animation.Animator;
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

import static android.R.attr.value;

public class PullToRefreshContainer extends FrameLayout {

    private static final long ANIM_TIME = 250; // 动画执行的时间
    private static int header_height; // 下拉刷新的布局本身高度
    private static int current_header_height; // 当前下拉刷新的布局高度
    private static int footer_height; // 上拉加载的布局本身高度
    private static int current_footer_height; // 当前上拉加载的布局高度

    private View mEmptyView; // 没有数据时的空View
    private View mChildView; // 核心组件，为ListView或RecycleView或其他控件
    private View mCurrentView; // 当前显示的是哪个View
    private BaseView mPullDownRefreshView; // 下拉刷新的布局
    private BaseView mPullUpLoadMoreView; // 上拉加载的布局
    private float mTouchY; // 手指按下时的初始Y轴坐标

    private boolean isRefresh; // 是否正在刷新中
    private boolean isLoadMore; // 是否正在加载更多中
    private boolean canLoadMore = false; // 是否开启了加载更多
    private boolean canRefresh = true; // 是否开启了下拉刷新

    private BaseRefreshListener refreshListener;

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
        addPullDownRefreshView();
        addPullUpLoadMoreView();
    }

    /**
     * 添加无数据时显示界面
     */
    private void addEmptyView() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.TOP;
        mEmptyView.setLayoutParams(layoutParams);
        mEmptyView.setVisibility(GONE);
        addView(mEmptyView);
    }

    /**
     * 无数据时显示无数据提示界面
     */
    public void showEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(VISIBLE);
            mChildView.setVisibility(GONE);
            mCurrentView = mEmptyView;
            resetPullUpAndDownView();
        }
    }

    /**
     * 有数据时显示相关数据界面
     */
    public void showDataView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(GONE);
            mChildView.setVisibility(VISIBLE);
            mCurrentView = mChildView;
            resetPullUpAndDownView();
        }
    }

    /**
     * 添加上拉加载更多时的提示View
     */
    private void addPullUpLoadMoreView() {
        if (mPullUpLoadMoreView == null) {
            mPullUpLoadMoreView = new PullUpLoadMoreView(getContext());
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM;
        mPullUpLoadMoreView.setLayoutParams(layoutParams);
        if (mPullUpLoadMoreView.getParent() != null)
            ((ViewGroup) mPullUpLoadMoreView.getParent()).removeAllViews();
        mPullUpLoadMoreView.post(new Runnable() {
            @Override
            public void run() {
                footer_height = mPullUpLoadMoreView.getMeasuredHeight();
            }
        });
        mPullUpLoadMoreView.setVisibility(INVISIBLE);
        addView(mPullUpLoadMoreView);
    }

    /**
     * 添加下拉刷新的提示View
     */
    private void addPullDownRefreshView() {
        if (mPullDownRefreshView == null) {
            mPullDownRefreshView = new PullDownRefreshView(getContext());
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPullDownRefreshView.setLayoutParams(layoutParams);
        if (mPullDownRefreshView.getParent() != null)
            ((ViewGroup) mPullDownRefreshView.getParent()).removeAllViews();
        mPullDownRefreshView.post(new Runnable() {
            @Override
            public void run() {
                header_height = mPullDownRefreshView.getMeasuredHeight();
            }
        });
        mPullDownRefreshView.setVisibility(INVISIBLE);
        addView(mPullDownRefreshView, 0);
    }

    /**
     * 设置是否可以上拉加载更多
     *
     * @param enable true 可以，false 禁用
     */
    public void setLoadMore(boolean enable) {
        canLoadMore = enable;
    }

    /**
     * 设置是否可以下拉刷新
     *
     * @param enable true 可以，false 禁用
     */
    public void setPullDownRefresh(boolean enable) {
        canRefresh = enable;
    }

    /**
     * 重置上拉加载和下拉刷新的View状态
     */
    private void resetPullUpAndDownView() {
        boolean flag = false;
        if (mPullDownRefreshView.getVisibility() != VISIBLE) {
            mPullDownRefreshView.getLayoutParams().height = 0;
            mPullDownRefreshView.setVisibility(VISIBLE);
            flag = true;
        }
        if (mPullUpLoadMoreView.getVisibility() != VISIBLE) {
            mPullUpLoadMoreView.getLayoutParams().height = 0;
            mPullUpLoadMoreView.setVisibility(VISIBLE);
            flag = true;
        }
        if (flag) {
            requestLayout();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        resetPullUpAndDownView();
        if (!canLoadMore && !canRefresh) return false;
        if (isRefresh || isLoadMore) return false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = ev.getY() - mTouchY;
                if (canRefresh) {
                    if (dy > 20 && !canChildScrollUp()) {
                        mPullDownRefreshView.begin();
                        return true;
                    }
                }
                if (canLoadMore) {
                    if (dy < -20 && !canChildScrollDown()) {
                        mPullUpLoadMoreView.begin();
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
                    current_header_height = 0;
                    current_footer_height = 0;
                    mPullDownRefreshView.getLayoutParams().height = 0;
                    mPullUpLoadMoreView.getLayoutParams().height = 0;
                    ViewCompat.setTranslationY(mCurrentView, 0);
                    requestLayout();
                } else if (dura > 0) {
                    if (canRefresh && !canChildScrollUp()) {
                        dura = dura * 0.5f;
                        mPullDownRefreshView.getLayoutParams().height = (int) dura;
                        mPullDownRefreshView.progress(dura);
                        ViewCompat.setTranslationY(mCurrentView, dura);
                        current_header_height = (int) dura;
                        current_footer_height = 0;
                        requestLayout();
                    } else {
                        mPullDownRefreshView.getLayoutParams().height = 0;
                        mPullUpLoadMoreView.getLayoutParams().height = 0;
                        ViewCompat.setTranslationY(mCurrentView, 0);
                        current_header_height = 0;
                        current_footer_height = 0;
                        requestLayout();
                    }
                } else {
                    if (canLoadMore && !canChildScrollDown()) {
                        dura = Math.abs(dura * 0.5f);
                        mPullUpLoadMoreView.getLayoutParams().height = (int) dura;
                        mPullUpLoadMoreView.progress(dura);
                        ViewCompat.setTranslationY(mCurrentView, -dura);
                        current_header_height = 0;
                        current_footer_height = (int) dura;
                        requestLayout();
                    } else {
                        mPullDownRefreshView.getLayoutParams().height = 0;
                        mPullUpLoadMoreView.getLayoutParams().height = 0;
                        ViewCompat.setTranslationY(mCurrentView, 0);
                        current_header_height = 0;
                        current_footer_height = 0;
                        requestLayout();
                    }
                }
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
                                    mPullDownRefreshView.loading();
                                }
                            });
                        } else {
                            setFinish(current_header_height, State.REFRESH);
                            mPullDownRefreshView.normal();
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
                                    mPullUpLoadMoreView.loading();
                                }
                            });
                        } else {
                            setFinish(current_footer_height, State.LOADMORE);
                            mPullUpLoadMoreView.normal();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);

    }

    /**
     * 当前的核心View是否可以向上滑动
     *
     * @return false，已到顶部不可再滑动；true，仍可以滑动
     */
    public boolean canChildScrollUp() {
        if (mCurrentView == null) {
            return false;
        }
        return ViewCompat.canScrollVertically(mCurrentView, -1);
    }

    /**
     * 当前的核心View是否可以向下滑动
     *
     * @return false，已到底部不可再滑动；true，仍可以滑动
     */
    public boolean canChildScrollDown() {
        if (mCurrentView == null) {
            return false;
        }
        return ViewCompat.canScrollVertically(mCurrentView, 1);
    }

    /**
     * 创建上拉和下拉过程中运行的动画
     */
    public void createAnimatorTranslationY(@State.REFRESH_STATE final int state, final int start, final int purpose, final CallBack callBack) {
        final ValueAnimator anim;
        anim = ValueAnimator.ofInt(start, purpose);
        anim.setDuration(ANIM_TIME);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (callBack != null) {
                    callBack.onSuccess();
                }
                if (purpose == 0) { //代表结束加载
                    mPullDownRefreshView.finishing(value);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                if (state == State.REFRESH) {
                    mPullDownRefreshView.getLayoutParams().height = value;
                    ViewCompat.setTranslationY(mCurrentView, value);
                } else {
                    mPullUpLoadMoreView.getLayoutParams().height = value;
                    ViewCompat.setTranslationY(mCurrentView, -value);
                }
                requestLayout();
            }

        });
        anim.start();
    }

    /**
     * 用于自动刷新的动画
     */
    public void createAutoAnimatorTranslationY(final BaseView v, final int start, final int purpose, final CallBack calllBack) {
        final ValueAnimator anim = ValueAnimator.ofInt(start, purpose);
        anim.setDuration(ANIM_TIME);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                v.begin();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                v.loading();
                if (calllBack != null)
                    calllBack.onSuccess();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                v.getLayoutParams().height = value;
                if (v == mPullDownRefreshView) {
                    ViewCompat.setTranslationY(mCurrentView, value);
                } else if (v == mPullUpLoadMoreView) {
                    ViewCompat.setTranslationY(mCurrentView, -value);
                }
                requestLayout();
                v.progress(value);
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
                    resetPullUpAndDownView();
                    mPullDownRefreshView.post(new Runnable() {
                        @Override
                        public void run() {
                            createAutoAnimatorTranslationY(mPullDownRefreshView, 0, header_height, new CallBack() {
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
     * 自动加载更多
     */
    public void autoLoadMore() {
        if (canLoadMore && !isLoadMore && !isRefresh) {
            post(new Runnable() {
                @Override
                public void run() {
                    resetPullUpAndDownView();
                    mPullUpLoadMoreView.post(new Runnable() {
                        @Override
                        public void run() {
                            createAutoAnimatorTranslationY(mPullUpLoadMoreView, 0, footer_height, new CallBack() {
                                @Override
                                public void onSuccess() {
                                    isLoadMore = true;
                                    if (refreshListener != null) {
                                        refreshListener.loadMore();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

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

    /**
     * 结束下拉刷新
     */
    public void setFinish(@State.REFRESH_STATE int state) {
        if (state == State.REFRESH) {
            if (mPullDownRefreshView != null && mPullDownRefreshView.getLayoutParams().height > 0 && isRefresh) {
                setFinish(header_height, state);
            }
        } else {
            if (mPullUpLoadMoreView != null && mPullUpLoadMoreView.getLayoutParams().height > 0 && isLoadMore) {
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

    /**
     * 获取空数据时展示的空界面
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * 设置空数据时展示空数据提示的View
     *
     * @param view
     */
    public void setEmptyView(View view) {
        mEmptyView = view;
    }

    /**
     * 设置刷新监听
     *
     * @param refreshListener
     */
    public void setRefreshListener(BaseRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    /**
     * 设置下拉刷新时显示的提示View
     */
    public void setPullDownRefreshView(BaseView view) {
        mPullDownRefreshView = view;
    }

    /**
     * 设置上拉加载时显示的提示View
     */
    public void setPullUpLoadMoreView(BaseView view) {
        mPullUpLoadMoreView = view;
    }

    public interface CallBack {
        void onSuccess();
    }

}
