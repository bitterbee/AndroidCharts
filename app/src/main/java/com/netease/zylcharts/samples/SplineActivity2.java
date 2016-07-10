package com.netease.zylcharts.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.netease.zylchartcore.activity.BaseChartActivity;
import com.netease.zylchartcore.data.Point3;
import com.netease.zylchartcore.shape.Spline;
import com.netease.zylchartcore.shape.SplineMode;
import com.netease.zylchartcore.view.BaseTranslateSurfaceView;
import com.netease.zylcharts.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 7/10/16.
 */
public class SplineActivity2 extends BaseChartActivity implements SeekBar.OnSeekBarChangeListener {
    private SeekBar mSeekBar;
    private Spline mSpline;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.activity_translate_point_clouds, mContainer);

        mSurfaceView = (BaseTranslateSurfaceView) vg.findViewById(R.id.surface_view);
        setPointCount(30);

        //普通拖拉条被拉动的处理代码
        mSeekBar = (SeekBar) this.findViewById(R.id.SeekBar01);
        mSeekBar.setMax(100);
        mSeekBar.setProgress(10);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = mSeekBar.getProgress();
        setPointCount(progress);
    }

    private void setPointCount(int count) {
        List<Point3> points = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            points.add(new Point3(i / 30.0f, (float) Math.random() / 3.0f, 0));
        }

        if (mSpline == null) {
            mSpline = new Spline(points, true, SplineMode.SPLMODE_SPLINE);
            mSurfaceView.setShape(mSpline);
        }
        mSpline.setPoints(points);
        mSurfaceView.requestRender();
    }
}
