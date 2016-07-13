package com.netease.zylchartcore.shape;

import com.netease.zylchartcore.matrix.MatrixState;

import java.nio.FloatBuffer;

/**
 * Created by zyl06 on 6/6/16.
 */
public abstract class Shape {
    protected int mProgram;               // 自定义渲染管线着色器程序id
    protected int muMVPMatrixHandle;      // 总变换矩阵引用
    protected int muMMatrixHandle;        //位置、旋转变换矩阵引用
    protected int muRHandle;              // 球的半径属性引用
    protected int maPositionHandle;       // 顶点位置属性引用
    protected int maNormalHandle;         //顶点法向量属性引用
    protected int maLightLocationHandle;  //光源位置属性引用
    protected int maCameraHandle;         //摄像机位置属性引用
    protected int maColorHandle;         //颜色属性引用


    protected String mVertexShader;       // 顶点着色器
    protected String mFragmentShader;     // 片元着色器

    protected FloatBuffer mColorBuffer; // 顶点颜色数据缓冲
    protected FloatBuffer mVertexBuffer;  // 顶点坐标数据缓冲
    protected FloatBuffer mNormalBuffer;  //顶点法向量数据缓冲

    protected float mXAngle = 0;
    protected float mYAngle = 0;
    protected float mZAngle = 0;

    protected boolean mIsInitialized = false;

    public void setXAngle(float XAngle) {
        this.mXAngle = XAngle;
    }

    public void setYAngle(float YAngle) {
        this.mYAngle = YAngle;
    }

    public void setZAngle(float ZAngle) {
        this.mZAngle = ZAngle;
    }

    public void drawSelf() {
        MatrixState.pushMatrix();

        MatrixState.rotate(mXAngle, 1, 0, 0);
        MatrixState.rotate(mYAngle, 0, 1, 0);
        MatrixState.rotate(mZAngle, 0, 0, 1);

        onDrawSelf();

        MatrixState.popMatrix();
    }

    public void initInSurfaceViewCreated() {
        if (!mIsInitialized) {
            mIsInitialized = true;
            onInitInSurfaceViewCreated();
        }
    }

    protected abstract void onInitInSurfaceViewCreated();
    protected abstract void onDrawSelf();
}
