package org.base.platform.utils;

import org.base.platform.bean.MessageEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by YinShengyi on 2016/12/20.
 * 总线消息传递工具类
 */
public class MessageEventUtils {

    private OnProcessMessageEvent mOnProcessMessageEvent;

    public MessageEventUtils(OnProcessMessageEvent listener) {
        mOnProcessMessageEvent = listener;
    }

    public void register() {
        EventBus.getDefault().register(this);
    }

    public void unregister() {
        EventBus.getDefault().unregister(this);
    }

    public static void post(MessageEvent event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 接收总线消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageReceived(MessageEvent event) {
        if (mOnProcessMessageEvent != null) {
            mOnProcessMessageEvent.onProcessMessageEvent(event);
        }
    }

    public interface OnProcessMessageEvent {
        void onProcessMessageEvent(MessageEvent event);
    }
}
