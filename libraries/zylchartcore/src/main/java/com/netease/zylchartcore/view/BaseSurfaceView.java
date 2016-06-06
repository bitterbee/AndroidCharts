package com.netease.zylchartcore.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.netease.zylchartcore.c.Constant;
import com.netease.zylchartcore.matrix.MatrixState;
import com.netease.zylchartcore.shape.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 6/3/16.
 */
public class BaseSurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;//角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器

    private List<Shape> mShapes = new ArrayList<>();

    float xAngle = 0;// 绕x轴旋转的角度
    float yAngle = 0;// 绕y轴旋转的角度
    float zAngle = 0;// 绕z轴旋转的角度

    float mLightOffset = 0; //灯光的位置或方向的偏移量
    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标

    public BaseSurfaceView(Context context) {
        this(context, null);
    }

    public BaseSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
        mRenderer = new SceneRenderer();    //创建场景渲染器
        setRenderer(mRenderer);             //设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
    }

    public BaseSurfaceView(Context context, AttributeSet attrs, List<Shape> shapes) {
        this(context, null);
        if (shapes != null) {
            mShapes = shapes;
        }
    }

    public void addShape(Shape s) {
        mShapes.add(s);
        postInvalidate();
    }

    public void setShape(Shape s) {
        mShapes.clear();
        if (s != null) {
            mShapes.add(s);
        }
        postInvalidate();
    }

    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;//计算触控笔Y位移
                float dx = x - mPreviousX;//计算触控笔X位移

                incrYAngle(dx * TOUCH_SCALE_FACTOR); //设置填充椭圆绕y轴旋转的角度
                incrXAngle(dy * TOUCH_SCALE_FACTOR);//设置填充椭圆绕x轴旋转的角度
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {

        public void onDrawFrame(GL10 gl) {
            //清除深度缓冲与颜色缓冲
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            //初始化光源位置
            MatrixState.setLightLocation(mLightOffset, 0, 1.5f);
            //保护现场
            MatrixState.pushMatrix();

            MatrixState.rotate(xAngle, 1, 0, 0);
            MatrixState.rotate(yAngle, 0, 1, 0);
            MatrixState.rotate(zAngle, 0, 0, 1);

            for (Shape shape : mShapes) {
                MatrixState.pushMatrix();
                shape.drawSelf();
                MatrixState.popMatrix();
            }

            //恢复现场
            MatrixState.popMatrix();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置
            GLES20.glViewport(0, 0, width, height);
            //计算GLSurfaceView的宽高比
            Constant.ratio = (float) width / height;
            // 调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-Constant.ratio, Constant.ratio, -1, 1, 20, 100);
            // 调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0, 0f, 30, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            //初始化变换矩阵
            MatrixState.setInitStack();
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES20.glClearColor(0f, 0f, 0f, 1.0f);
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //打开背面剪裁
            GLES20.glEnable(GLES20.GL_CULL_FACE);
        }
    }

    public void setLightOffset(float lightOffset) {
        this.mLightOffset = lightOffset;
    }

    public float incrYAngle(float delta) {
        yAngle += delta;
        return yAngle;
    }

    public float incrXAngle(float delta) {
        xAngle += delta;
        return xAngle;
    }

    public float incrZAngle(float delta) {
        zAngle += delta;
        return zAngle;
    }
}