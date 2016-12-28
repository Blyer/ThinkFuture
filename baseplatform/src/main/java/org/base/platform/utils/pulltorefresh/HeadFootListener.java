package org.base.platform.utils.pulltorefresh;

public interface HeadFootListener {

    /**
     * 开始下拉
     */
    void begin();

    /**
     * 回调的精度,单位为px
     *
     * @param progress 当前高度
     */
    void progress(float progress);

    void finishing(float progress);

    /**
     * 下拉完毕
     */
    void loading();

    /**
     * 看不见的状态
     */
    void normal();

}
