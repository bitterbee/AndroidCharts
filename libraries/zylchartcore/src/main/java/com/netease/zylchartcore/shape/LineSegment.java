package com.netease.zylchartcore.shape;

import android.opengl.GLES20;

import com.netease.zylchartcore.ChartsCore;
import com.netease.zylchartcore.data.Point3;
import com.netease.zylchartcore.matrix.MatrixState;
import com.netease.zylchartcore.shader.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by zyl06 on 6/6/16.
 */
public class LineSegment extends Shape {
    private int vCount = 2;
    private Point3 mStart;
    private Point3 mEnd;

    public LineSegment(Point3 start, Point3 end) {
        mStart = start;
        mEnd = end;
    }

    private void initVertexData() {
        float[] vertices = new float[6];
        int count = 0;
        vertices[count++] = mStart.x;
        vertices[count++] = mStart.y;
        vertices[count++] = mStart.z;

        vertices[count++] = mEnd.x;
        vertices[count++] = mEnd.y;
        vertices[count++] = mEnd.z;

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
        initVertexData();
        initShader();
    }

    @Override
    protected void onDrawSelf() {
        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);

        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);

        GLES20.glEnableVertexAttribArray(maPositionHandle);

        GLES20.glLineWidth(2.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vCount);
    }

}
