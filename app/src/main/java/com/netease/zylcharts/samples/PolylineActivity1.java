package com.netease.zylcharts.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.netease.zylchartcore.activity.BaseChartActivity;
import com.netease.zylchartcore.data.Point3;
import com.netease.zylchartcore.shape.Polyline;
import com.netease.zylchartcore.view.BaseRotateSurfaceView;
import com.netease.zylcharts.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 6/7/16.
 */
public class PolylineActivity1 extends BaseChartActivity implements SeekBar.OnSeekBarChangeListener {
    private SeekBar mSeekBar;
    private Polyline mPolyline;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.activity_rotate_point_clouds, mContainer);

        mSurfaceView = (BaseRotateSurfaceView) vg.findViewById(R.id.surface_view);
        setPointCount(10);

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
        List<Point3> points = new ArrayList<Point3>();
        for (int i=0; i<count; i++) {
            points.add(new Point3((float)Math.random(), (float)Math.random(), (float)Math.random()));
        }

        if (mPolyline == null) {
            mPolyline = new Polyline(points, true, false);
            mSurfaceView.setShape(mPolyline);
        }
        mPolyline.setPoints(points);
        mSurfaceView.requestRender();
    }
}
