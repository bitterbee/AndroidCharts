package com.netease.zylcharts;

import android.app.Activity;

/**
 * Created by zyl06 on 6/3/16.
 */
public class ContentItem {
    String name;
    String desc;
    Class<? extends Activity> activityClass;
    boolean isNew = false;

    public ContentItem(Class<? extends Activity> activityClass, String n, String d) {
        this.activityClass = activityClass;
        name = n;
        desc = d;
    }

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }
}
