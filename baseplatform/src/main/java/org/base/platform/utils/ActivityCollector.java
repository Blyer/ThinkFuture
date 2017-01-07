package org.base.platform.utils;

import org.base.platform.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YinShengyi on 2015/4/18.
 * 所有打开Activity的集合工具类
 */
public class ActivityCollector {

    private static List<BaseActivity> activities = new ArrayList<>();

    public static void put(BaseActivity activity) {
        activities.add(activity);
    }

    public static void remove(BaseActivity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (BaseActivity activity : activities) {
            if (!activity.isDestroyed()) {
                activity.finish();
            }
        }
        activities.clear();
    }

    /**
     * 获取最后一个activity
     */
    public static BaseActivity getCurrentActivity() {
        if (!activities.isEmpty()) {
            return activities.get(activities.size() - 1);
        } else {
            return null;
        }
    }

    public static List<BaseActivity> getAllActivity() {
        return activities;
    }
}
