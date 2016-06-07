package com.netease.zylchartcore.shape;

import com.netease.zylchartcore.data.Point3;
import com.netease.zylchartcore.matrix.MatrixState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 6/6/16.
 */
public class PointClouds extends Shape {
    private Ball mBall = Ball.sInstance;
    private List<Point3> mLocations = new ArrayList<>();

    public PointClouds(List<Point3> points) {
        setPoints(points);
    }

    public void setPoints(List<Point3> points) {
//        if (mBall == null) {
//            mBall = new Ball(Point3.ORIGIN3, 0.01f, 60);
//        }
        mLocations.clear();
        if (points != null) {
            mLocations.addAll(points);
        }
    }

    @Override
    protected void onInitInSurfaceViewCreated() {
        mBall.initInSurfaceViewCreated();
    }

    @Override
    protected void onDrawSelf() {
        int size = mLocations.size();
        for (int i=0; i<size; i++) {
            Point3 location = mLocations.get(i);

            MatrixState.pushMatrix();
            MatrixState.translate(location.x, location.y, location.z);
            mBall.drawSelf();
            MatrixState.popMatrix();
        }
    }
}
