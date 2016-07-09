package com.netease.zylcharts.application;

import android.app.Application;

import com.netease.zylchartcore.ChartsCore;

/**
 * Created by zyl06 on 6/6/16.
 */
public class ChartsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ChartsCore.init(this);
    }
}
