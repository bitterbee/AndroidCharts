package com.netease.zylchartcore.shape;

import com.netease.zylchartcore.data.Point3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 6/6/16.
 */
public class PointClouds extends Shape {
    List<Ball> mBalls = new ArrayList<>();

    public PointClouds(List<Point3> points) {
        int count = points.size();
        for (int i=0; i<count; ++i) {
            Ball ball = new Ball(points.get(i), 0.8f, 45);
            mBalls.add(ball);
        }
    }

    @Override
    public void drawSelf() {
        for (Ball ball : mBalls) {
            ball.drawSelf();
        }
    }
}
