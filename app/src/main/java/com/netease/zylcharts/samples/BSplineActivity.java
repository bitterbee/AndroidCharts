package com.netease.zylcharts.samples;

import com.netease.zylchartcore.shape.SplineMode;

/**
 * Created by zyl06 on 7/10/16.
 */
public class BSplineActivity extends BaseSplineActivity {
    protected int getSplineMode() {
        return SplineMode.SPLMODE_BSPLINE;
    }
}
