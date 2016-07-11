package com.netease.zylcharts.samples;

import com.netease.zylchartcore.shape.SplineMode;

/**
 * Created by zyl06 on 7/11/16.
 */
public class BezierActivity extends BaseSplineActivity {
    protected int getSplineMode() {
        return SplineMode.SPLMODE_BEZIER;
    }
}