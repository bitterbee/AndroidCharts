package com.netease.zylchartcore.shape;

import com.netease.zylchartcore.data.Point3;
import com.netease.zylchartcore.matrix.MatrixState;

/**
 * Created by zyl06 on 6/6/16.
 */
public class Arrow extends Shape {
    private LineSegment mLineSegment;
    private Cone mCone;
    private float mLength;

    public Arrow(float scale, float raduis, float coneHeight, int coneFaceCount, int length) {
        this.mLength = length;
        this.mCone = new Cone(scale, raduis, coneHeight, coneFaceCount);
        this.mLineSegment = new LineSegment(new Point3(0, 0, 0), new Point3(0, length, 0));
    }

    @Override
    public void initInSurfaceViewCreated() {
        mCone.initInSurfaceViewCreated();
        mLineSegment.initInSurfaceViewCreated();
    }

    @Override
    protected void onDrawSelf() {
        MatrixState.pushMatrix();
        mLineSegment.drawSelf();
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.translate(0, mLength, 0);
        mCone.drawSelf();
        MatrixState.popMatrix();
    }
}
