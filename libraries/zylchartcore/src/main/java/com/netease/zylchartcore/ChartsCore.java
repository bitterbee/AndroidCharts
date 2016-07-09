package com.netease.zylchartcore;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by zyl06 on 6/6/16.
 */
public class ChartsCore {
    private static Context sAppContext;

    public static void init(Context ctx) {
        sAppContext = ctx;
    }

    public static Context getAppContext() {
        return sAppContext;
    }

    public static Resources getResources() {
        return sAppContext.getResources();
    }
}
