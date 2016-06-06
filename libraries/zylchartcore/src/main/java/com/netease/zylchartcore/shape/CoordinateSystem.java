package com.netease.zylchartcore.shape;

import com.netease.zylchartcore.matrix.MatrixState;

/**
 * Created by zyl06 on 6/6/16.
 */
public class CoordinateSystem extends Shape {
    private Arrow mYArrow;

    public CoordinateSystem() {
        mYArrow = new Arrow(1.0f, 0.015f, 0.05f, 6, 1);
    }

    @Override
    public void initInSurfaceViewCreated() {
        mYArrow.initInSurfaceViewCreated();
    }

    @Override
    protected void onDrawSelf() {
        // draw x axis
        MatrixState.pushMatrix();
        MatrixState.rotate(-90, 0, 0, 1);
        mYArrow.drawSelf();
        MatrixState.popMatrix();

        // draw y axis
        mYArrow.drawSelf();

        // draw z axis
        MatrixState.pushMatrix();
        MatrixState.rotate(90, 1, 0, 0);
        mYArrow.drawSelf();
        MatrixState.popMatrix();
    }
}
