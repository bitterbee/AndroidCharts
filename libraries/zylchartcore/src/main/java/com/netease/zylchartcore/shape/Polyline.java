package com.netease.zylchartcore.shape;

import android.opengl.GLES20;

import com.netease.zylchartcore.ChartsCore;
import com.netease.zylchartcore.data.Point3;
import com.netease.zylchartcore.matrix.MatrixState;
import com.netease.zylchartcore.shader.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 6/7/16.
 */
public class Polyline extends Shape {
    private Ball mBall = Ball.sInstance;
    private List<Point3> mLocations = new ArrayList<>();
    private boolean mIsShowPoints = false;
    private boolean mIsClosed = false;
    protected int mVertexCount = 0; // 顶点数量

    public Polyline(List<Point3> points, boolean isShowPoints, boolean isClosed) {
        this.mIsShowPoints = isShowPoints;
        this.mIsClosed = isClosed;
        setPoints(points);
    }

    public void setPoints(List<Point3> points) {
//        if (mBall == null) {
//            mBall = new Ball(Point3.ORIGIN3, 0.01f, 60);
//        }
        mLocations.clear();
        if (points != null) {
            mLocations.addAll(points);

            mVertexCount = mLocations.size();
            initVertexData();
        }
    }

    private void initVertexData() {
        float[] vertices = new float[3 * mVertexCount];
        int count = 0;
        for (int i=0; i<mVertexCount; i++) {
            Point3 location = mLocations.get(i);
            vertices[count++] = location.x;
            vertices[count++] = location.y;
            vertices[count++] = location.z;
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
    }

    private void initShader() {
        mVertexShader = ShaderUtil.loadFromAssetsFile("line_vertex_1.glsl", ChartsCore.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("line_fragment_1.glsl", ChartsCore.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
        maLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
    }

    @Override
    protected void onInitInSurfaceViewCreated() {
        mBall.initInSurfaceViewCreated();

        initVertexData();
        initShader();
    }

    @Override
    protected void onDrawSelf() {
        if (mIsShowPoints) {
            int size = mLocations.size();
            for (int i=0; i<size; i++) {
                Point3 location = mLocations.get(i);

                MatrixState.pushMatrix();
                MatrixState.translate(location.x, location.y, location.z);
                mBall.drawSelf();
                MatrixState.popMatrix();
            }
        }

        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);

        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(maPositionHandle);

        GLES20.glLineWidth(1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, mVertexCount);
    }
}
