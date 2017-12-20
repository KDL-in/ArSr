package com.arsr.arsr;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动控制类
 * 1. 所有活动都需要经过它的管理
 * 2. 可以用于结束所有活动，定义退出按钮
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity a :
                activities) {
            a.finish();
        }
        activities.clear();
    }
}
