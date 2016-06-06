package com.netease.zylchartcore.shape;

import android.opengl.GLES20;

import com.netease.zylchartcore.ChartsCore;
import com.netease.zylchartcore.data.operator.VectorUtil;
import com.netease.zylchartcore.matrix.MatrixState;
import com.netease.zylchartcore.shader.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by zyl06 on 6/6/16.
 */
public class ConeSide extends Shape {

    private int vCount = 0;

    private float mScale = 1;
    private float mRadius = 0.8f;
    private float mHeight = 1f;
    private int mFaceCount = 20;

    public ConeSide(float scale, float r, float h, int n) {
        this.mScale = scale;
        this.mRadius = r;
        this.mHeight = h;
        this.mFaceCount = n;
    }

    public void initVertexData(float scale, float r, float h, int n) {
        r = scale * r;
        h = scale * h;
        float angdegSpan = 360.0f / n;
        vCount = 3 * n * 4;

        float[] vertices = new float[vCount * 3];
        float[] normals = new float[vertices.length];
        int count = 0;
        int norCount = 0;
        for (float angdeg = 0; Math.ceil(angdeg) < 360; angdeg += angdegSpan)
        {
            double angrad = Math.toRadians(angdeg);
            double angradNext = Math.toRadians(angdeg + angdegSpan);
            vertices[count++] = 0;
            vertices[count++] = h;
            vertices[count++] = 0;

            vertices[count++] = (float) (-r * Math.sin(angrad));
            vertices[count++] = 0;
            vertices[count++] = (float) (-r * Math.cos(angrad));

            vertices[count++] = (float) (-r * Math.sin(angradNext));
            vertices[count++] = 0;
            vertices[count++] = (float) (-r * Math.cos(angradNext));
        }

        for (int i = 0; i < vertices.length; i = i + 3) {
            if (vertices[i] == 0 && vertices[i + 1] == h && vertices[i + 2] == 0) {
                normals[norCount++] = 0;
                normals[norCount++] = 1;
                normals[norCount++] = 0;
            } else {
                float[] norXYZ = VectorUtil.calConeNormal(0, 0, 0,
                        vertices[i], vertices[i + 1], vertices[i + 2],
                        0, h, 0);
                normals[norCount++] = norXYZ[0];
                normals[norCount++] = norXYZ[1];
                normals[norCount++] = norXYZ[2];
            }
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
        nbb.order(ByteOrder.nativeOrder());
        mNormalBuffer = nbb.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);
    }

    private void initShader() {
        mVertexShader = ShaderUtil.loadFromAssetsFile("coneside_vertex_1.glsl", ChartsCore.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("coneside_fragment_1.glsl", ChartsCore.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
        maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
        maLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
    }

    @Override
    public void initInSurfaceViewCreated() {
        initVertexData(mScale, mRadius, mHeight, mFaceCount);
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
        GLES20.glVertexAttribPointer(maNormalHandle, 4, GLES20.GL_FLOAT, false, 3 * 4, mNormalBuffer);

        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maNormalHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}
