package com.netease.zylcharts.samples;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.netease.zylchartcore.data.ControlPoint3;
import com.netease.zylchartcore.data.Point3;
import com.netease.zylchartcore.shape.Spline;
import com.netease.zylchartcore.shape.SplineMode;

import java.util.ArrayList;

/**
 * Created by zyl06 on 7/13/16.
 */
public class LHSplineActivity extends BaseSplineActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSeekBar.setVisibility(View.GONE);
    }

    @Override
    protected int getSplineMode() {
        return SplineMode.SPLMODE_SPLINE;
    }

    @Override
    protected void setPointCount(int count) {
        mPoints = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            mPoints.add(new ControlPoint3(i / 30.0f, (float) Math.random() / 3.0f, 0));
        }

        if (mSpline == null) {
            mSpline = new Spline(mPoints, true, getSplineMode());
            mSurfaceView.setShape(mSpline);
        }
        mSpline.setPoints(mPoints);
        mSurfaceView.requestRender();
    }

    @Override
    public void setPoint(int idx, ControlPoint3 point) {
        if (mSpline != null && mPoints != null && idx >= 0 && idx < mPoints.size()) {
            point.isSelected = true;
            point.color = Color.GREEN;
            mPoints.set(idx, point);

            Point3 oldPoint = mPoints.get(idx);
            float dy = point.y - oldPoint.y;
            float factor = 0.5f; // 参数1

            int size = mPoints.size();
            for (int i=idx+1; i<size; i++) {
                ControlPoint3 cpt = mPoints.get(i);
                if (cpt.color == Color.GREEN) {
                    break;
                }
                cpt.y += dy * Math.pow(factor, i-idx);
                cpt.color = Color.YELLOW;
            }

            for (int i=idx-1; i>=0; i--) {
                ControlPoint3 cpt = mPoints.get(i);
                if (cpt.color == Color.GREEN) {
                    break;
                }
                cpt.y += dy * Math.pow(factor, i-idx);
                cpt.color = Color.YELLOW;
            }

            mSpline.setPoints(mPoints);
            mSurfaceView.requestRender();
        }
    }
}
