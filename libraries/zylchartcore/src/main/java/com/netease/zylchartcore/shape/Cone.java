package com.netease.zylchartcore.shape;

import com.netease.zylchartcore.matrix.MatrixState;

/**
 * Created by zyl06 on 6/6/16.
 */
public class Cone extends Shape {
    private Circle mBottomCircle;
    private ConeSide mConeSide;

    private float mHeight = 1.0f;
    private float mScale = 1.0f;

    public Cone(float scale, float r, float h, int n) {
        this.mScale = scale;
        this.mHeight = h;

        mBottomCircle = new Circle(scale, r, n);
        mConeSide = new ConeSide(scale, r, h, n);
    }

    @Override
    protected void onInitInSurfaceViewCreated() {
        mBottomCircle.initInSurfaceViewCreated();
        mConeSide.initInSurfaceViewCreated();
    }

    @Override
    protected void onDrawSelf() {
        MatrixState.pushMatrix();
        MatrixState.translate(0, -mHeight / 2 * mScale, 0);
        MatrixState.rotate(90, 1, 0, 0);
        MatrixState.rotate(180, 0, 0, 1);
        mBottomCircle.drawSelf();
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.translate(0, -mHeight / 2 * mScale, 0);
        mConeSide.drawSelf();
        MatrixState.popMatrix();
    }
}
