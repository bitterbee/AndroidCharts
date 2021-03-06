package com.netease.zylcharts.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.netease.zylchartcore.shape.SplineMode;

/**
 * Created by zyl06 on 7/11/16.
 */
public class BezierActivity extends BaseSplineActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSeekBar.setVisibility(View.GONE);
    }

    protected int getSplineMode() {
        return SplineMode.SPLMODE_BEZIER;
    }
}